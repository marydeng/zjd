package poll.com.zjd.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jpay.JPay;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.fragment.MyFragment;
import poll.com.zjd.model.AliModel;
import poll.com.zjd.model.CardBean;
import poll.com.zjd.model.CreateRechargePayInfoResponse;
import poll.com.zjd.model.WxBackModel;
import poll.com.zjd.model.WxModel;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.DoubleCalculateUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.UrlCodeUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：充值页面
 */
@FmyContentView(R.layout.activity_recharge)
public class MyRechargeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MyRechargeActivity.class.getSimpleName();
    public static String EXTRA_CARD_BEAN = "MyRechargeActivity.CardBean";

    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.head_text_right)
    private TextView rightTextView;
    @FmyViewView(R.id.top_linear_layout)
    private LinearLayout topLinearLayout;
    @FmyViewView(R.id.weixin_pay_text_view)
    private TextView weixinPayTextView;
    @FmyViewView(R.id.zhifubao_pay_text_view)
    private TextView zhifubaoPayTextView;
    @FmyViewView(R.id.balance_charge)
    private TextView balanceCharge;
    @FmyViewView(R.id.card_relative_layout)
    private RelativeLayout cardRelativeLayout;
    @FmyViewView(R.id.cost_value)
    private TextView costValueTextView;
    @FmyViewView(R.id.monkey_get)
    private TextView monkeyGetTextView;

    private CardBean cardBean;
    private HttpRequestDao httpRequestDao;
    private DecimalFormat decimalFormat = new DecimalFormat("0.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setTransparentStatusBar(topLinearLayout);
        titleTextView.setText(R.string.my_balance_rechare);
        rightTextView.setText(R.string.my_balance_detail);

        weixinPayTextView.setSelected(true);
        zhifubaoPayTextView.setSelected(false);

        httpRequestDao = new HttpRequestDao();

        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                String cardBeanJson = bundle.getString(EXTRA_CARD_BEAN);
                if (!TextUtils.isEmpty(cardBeanJson)) {
                    cardBean = JSON.parseObject(cardBeanJson, CardBean.class);
                }
            }
        }

        if (null != cardBean) {

            cardRelativeLayout.setBackgroundResource(cardBean.getDrawableId());
            costValueTextView.setText(getResources().getString(R.string.yuan, decimalFormat.format(cardBean.getRechargeAmount())));

            double gettingValue = DoubleCalculateUtil.add(cardBean.getRechargeAmount(), cardBean.getGiftAmount());
            String gettingValueTxt = getString(R.string.monkey_get, decimalFormat.format(gettingValue));
            int start = gettingValueTxt.indexOf(decimalFormat.format(gettingValue));
            int end = start + decimalFormat.format(gettingValue).length();
            SpannableString spannableString = new SpannableString(gettingValueTxt);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_e54956)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            monkeyGetTextView.setText(spannableString);
        }
    }

    @FmyClickView({R.id.head_back, R.id.head_text_right, R.id.weixin_pay_text_view, R.id.zhifubao_pay_text_view, R.id.balance_charge})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.head_text_right:
                appContext.startActivity(this, MyBalanceHistoryActivity.class, null);
                break;
            case R.id.weixin_pay_text_view:
                weixinPayTextView.setSelected(true);
                zhifubaoPayTextView.setSelected(false);
                break;
            case R.id.zhifubao_pay_text_view:
                weixinPayTextView.setSelected(false);
                zhifubaoPayTextView.setSelected(true);
                break;
            case R.id.balance_charge:
                HashMap<String, String> map = new HashMap<>();
                map.put("rechargeSchemeId", cardBean.getId());
                JSONObject jsonObject = JsonUtils.convertJSONObject(map);
                DialogUtil.showProgressDialog(this, null, null);
                httpRequestDao.createRechargePayInfo(this, jsonObject, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        DialogUtil.hideProgressDialog();
                        Gson gson = new Gson();
                        CreateRechargePayInfoResponse createRechargePayInfoResponse = gson.fromJson(result, CreateRechargePayInfoResponse.class);
                        if (zhifubaoPayTextView.isSelected()) {
                            ALIpay(createRechargePayInfoResponse);
                        } else {
                            WXpay(createRechargePayInfoResponse);
                        }
                    }
                });

                break;
            default:
                break;
        }
    }

    //微信支付
    private void WXpay(final CreateRechargePayInfoResponse createRechargePayInfoResponse) {

        if (httpRequestDao == null) httpRequestDao = new HttpRequestDao();
        HashMap<String, String> map = new HashMap<>();
        map.put("amount", String.valueOf(createRechargePayInfoResponse.getRechargeAmount()));         //支付金额
        map.put("attach", "100");      //支付方式
        map.put("tradeNo", createRechargePayInfoResponse.getId());        //支付单号
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);

        LogUtils.i(jsonObject.toString());

        httpRequestDao.getWXPayDetail(mContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                Gson gson = new Gson();
                WxBackModel backModel = gson.fromJson(result, WxBackModel.class);

                WxModel wxModel = new WxModel();
                wxModel.appId = backModel.appid;
                wxModel.sign = backModel.sign;
                wxModel.nonceStr = backModel.noncestr;
                wxModel.prepayId = backModel.prepayid;
                wxModel.partnerId = backModel.partnerid;
                wxModel.timeStamp = backModel.timestamp;

                String wxPay = gson.toJson(wxModel);
                LogUtils.i(wxPay);

                JPay.getIntance(MyRechargeActivity.this).toPay(JPay.PayMode.WXPAY, wxPay, new JPay.JPayListener() {
                    @Override
                    public void onPaySuccess() {
                        LogUtils.e("成功");
                        Bundle bundle = new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO, getString(R.string.recharge));
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO, "-" + createRechargePayInfoResponse.getRechargeAmount());
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE, getString(R.string.weixin_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT, RechargePayResultActivity.TradeResult.success);
                        appContext.startActivity(MyRechargeActivity.this, RechargePayResultActivity.class, bundle);

                        setResult(MyFragment.ResultCode.backFromRecharge);
                        MyRechargeActivity.this.finish();
                    }

                    @Override
                    public void onPayError(int i, String s) {
                        LogUtils.e("错误码:" + i + "--->" + s);
                        Bundle bundle = new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO, getString(R.string.recharge));
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO, "-" + createRechargePayInfoResponse.getRechargeAmount());
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE, getString(R.string.weixin_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT, RechargePayResultActivity.TradeResult.fail);
                        appContext.startActivity(MyRechargeActivity.this, RechargePayResultActivity.class, bundle);
                    }

                    @Override
                    public void onPayCancel() {
                        LogUtils.e("取消");
                    }
                });
            }
        });
    }

    //支付宝支付
    private void ALIpay(final CreateRechargePayInfoResponse createRechargePayInfoResponse) {

        if (httpRequestDao == null) httpRequestDao = new HttpRequestDao();
        HashMap<String, String> map = new HashMap<>();
        map.put("amount", String.valueOf(createRechargePayInfoResponse.getRechargeAmount()));         //支付金额
        map.put("attach", "100");
        map.put("tradeNo", createRechargePayInfoResponse.getId());        //支付单号
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        LogUtils.e("tradeNo-->" + map.get("tradeNo"));
        httpRequestDao.getALIPayDetail(mContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                AliModel aliModel = new Gson().fromJson(result, AliModel.class);
                result = aliModel.body;
                result = UrlCodeUtils.toURLDecoded(result);  //转码一次
                JPay.getIntance(MyRechargeActivity.this).toPay(JPay.PayMode.ALIPAY, result, new JPay.JPayListener() {
                    @Override
                    public void onPaySuccess() {
                        LogUtils.e("成功");
                        Bundle bundle = new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO, getString(R.string.recharge));
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO, "-" + createRechargePayInfoResponse.getRechargeAmount());
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE, getString(R.string.zhifubao_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT, RechargePayResultActivity.TradeResult.success);
                        appContext.startActivity(MyRechargeActivity.this, RechargePayResultActivity.class, bundle);

                        setResult(MyFragment.ResultCode.backFromRecharge);
                        MyRechargeActivity.this.finish();
                    }

                    @Override
                    public void onPayError(int i, String s) {
                        LogUtils.e("错误码:" + i + "--->" + s);
                        Bundle bundle = new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO, getString(R.string.recharge));
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO, "-" + createRechargePayInfoResponse.getRechargeAmount());
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE, getString(R.string.zhifubao_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT, RechargePayResultActivity.TradeResult.fail);
                        appContext.startActivity(MyRechargeActivity.this, RechargePayResultActivity.class, bundle);
                    }

                    @Override
                    public void onPayCancel() {
                        LogUtils.e("取消");
                    }
                });
            }
        });
    }


}
