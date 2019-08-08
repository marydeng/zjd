package poll.com.zjd.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.jpay.JPay;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.AliModel;
import poll.com.zjd.model.QueryVbankResponse;
import poll.com.zjd.model.WxBackModel;
import poll.com.zjd.model.WxModel;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.DoubleCalculateUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.utils.UrlCodeUtils;
import poll.com.zjd.view.PasswordView;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/23
 * 文件描述：04-2-2-1购物车-提交订单-支付
 */
@FmyContentView(R.layout.activity_order_choose_pay_way)
public class OrderChoosePayWayActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OrderChoosePayWayActivity.class.getSimpleName();
    public static final String EXTRA_ORDER_ID = "order.id";
    public static final String EXTRA_ORDER_MONEY = "order.money";
    public static final String EXTRA_VERY_PHONE_RESULT = "very.phone.result";
    public static final String VERY_PHONE_SUCCESS = "very.phone.success";
    public static final int RESULT_CODE_VERY_PHONE = 300;
    private static final int YUER = 0;    //余额支付
    private static final int WEIXIN = 1;  //微信支付
    private static final int ZHIFUBAO = 2;//支付宝支付

    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.card_pay_image_view)
    private ImageView cardPayImageView;
    @FmyViewView(R.id.weixin_pay_image_view)
    private ImageView weixinPayImageView;
    @FmyViewView(R.id.zhifubao_pay_image_view)
    private ImageView zhifubaoPayImageView;
    @FmyViewView(R.id.pay_monkey)
    private TextView payMonkeyTxt;
    @FmyViewView(R.id.balance_value)
    private TextView balanceValueTxt;

    private int payWay = YUER;  //默认余额支付
    private HttpRequestDao httpRequestDao;
    private String orderId;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式

        titleTextView.setText(R.string.pay);
        changePayWay(R.id.card_pay_linear_layout);


        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                orderId = bundle.getString(EXTRA_ORDER_ID);
                totalAmount = bundle.getDouble(EXTRA_ORDER_MONEY);
                LogUtils.i("orderId="+orderId);
            }
        }

        payMonkeyTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(totalAmount)));

        // 查询余额
        httpRequestDao = new HttpRequestDao();
        DialogUtil.showProgressDialog(this, null, null);
        httpRequestDao.queryVBankInfo(this, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                QueryVbankResponse queryVbankResponse = gson.fromJson(result, QueryVbankResponse.class);
                balanceValueTxt.setText(getString(R.string.card_balance_value, String.valueOf(queryVbankResponse.getAvailableAmt())));
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });
    }

    @FmyClickView({R.id.head_back, R.id.confirm_pay, R.id.card_pay_linear_layout, R.id.weixin_pay_linear_layout, R.id.zhifubao_pay_linear_layout})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.card_pay_linear_layout:
            case R.id.weixin_pay_linear_layout:
            case R.id.zhifubao_pay_linear_layout:
                changePayWay(view.getId());
                break;
            case R.id.confirm_pay:

                if (payWay == WEIXIN) {
                    WXpay();
                } else if (payWay == ZHIFUBAO) {
                    ALIpay();
                } else {
                    showPasswordDialog();
                }
                break;
            default:
                break;
        }
    }

    private void changePayWay(int viewId) {
        if (R.id.card_pay_linear_layout == viewId) {
            cardPayImageView.setSelected(true);
            weixinPayImageView.setSelected(false);
            zhifubaoPayImageView.setSelected(false);
            payWay = YUER;
        } else if (R.id.weixin_pay_linear_layout == viewId) {
            cardPayImageView.setSelected(false);
            weixinPayImageView.setSelected(true);
            zhifubaoPayImageView.setSelected(false);
            payWay = WEIXIN;
        } else if (R.id.zhifubao_pay_linear_layout == viewId) {
            cardPayImageView.setSelected(false);
            weixinPayImageView.setSelected(false);
            zhifubaoPayImageView.setSelected(true);
            payWay = ZHIFUBAO;
        }
    }

    private void showPasswordDialog() {
        if (appContext.hasPayPassword) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_pay_password, null);
            final Dialog dialog = DialogUtil.showDialogBottom(this, view);
            final PasswordView passwordView = (PasswordView) view.findViewById(R.id.password_view);
            passwordView.setInputType(InputType.TYPE_CLASS_NUMBER);
            view.findViewById(R.id.cancel_txt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtils.i("view.getId=" + view.toString());
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.complete_txt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtils.i("view.getId=" + view.toString());
                    if (passwordView.isComplete()) {
                        String password = passwordView.getPassword();
                        checkPassword(password);
                        dialog.dismiss();
                    }
                }
            });
            passwordView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    passwordView.showInputMethod();
                }
            }, 300);
        } else {
            if (totalAmount > 500) {
                appContext.startActivityForResult(OrderChoosePayWayActivity.this, OrderPayVerifyActivity.class, 0, null);
            } else {
                VBankPay();
            }
        }

    }

    private void checkPassword(String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("payPassword", password);
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(OrderChoosePayWayActivity.this, null, null);
        httpRequestDao.checkPassword(OrderChoosePayWayActivity.this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                hideSoftInput();
                if (totalAmount > 500) {
                    appContext.startActivityForResult(OrderChoosePayWayActivity.this, OrderPayVerifyActivity.class, 0, null);
                } else {
                    VBankPay();
                }
            }
        });
    }

    private void VBankPay() {
        HashMap<String, String> map = new HashMap<>();
        map.put("combineId", orderId);
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(OrderChoosePayWayActivity.this, null, null);
        httpRequestDao.payByVBank(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                LogUtils.i("支付成功");
                ToastUtils.showToast(OrderChoosePayWayActivity.this,"支付成功");
                OrderChoosePayWayActivity.this.finish();
                Bundle bundle=new Bundle();
                bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO,orderId);
                bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO,"-"+totalAmount);
                bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE,getString(R.string.card_pay_mode));
                bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT,OrderPayResultActivity.TradeResult.success);
                appContext.startActivity(OrderChoosePayWayActivity.this,OrderPayResultActivity.class,bundle);
            }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OrderChoosePayWayActivity.this.finish();
                        Bundle bundle=new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO,orderId);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO,"-"+totalAmount);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE,getString(R.string.card_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT,OrderPayResultActivity.TradeResult.fail);
                        appContext.startActivity(OrderChoosePayWayActivity.this,OrderPayResultActivity.class,bundle);
                    }
                }
        );
    }

    //微信支付
    private void WXpay() {

        if (httpRequestDao == null) httpRequestDao = new HttpRequestDao();
        HashMap<String, String> map = new HashMap<>();
        map.put("amount", String.valueOf(totalAmount));         //支付金额
        map.put("attach", "200");      //商品名称
        map.put("tradeNo", orderId);        //支付单号
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

                JPay.getIntance(OrderChoosePayWayActivity.this).toPay(JPay.PayMode.WXPAY, wxPay, new JPay.JPayListener() {
                    @Override
                    public void onPaySuccess() {
                        LogUtils.e("成功");
                        OrderChoosePayWayActivity.this.finish();
                        Bundle bundle=new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO,orderId);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO,"-"+totalAmount);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE,getString(R.string.weixin_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT,OrderPayResultActivity.TradeResult.success);
                        appContext.startActivity(OrderChoosePayWayActivity.this,OrderPayResultActivity.class,bundle);
                    }

                    @Override
                    public void onPayError(int i, String s) {
                        LogUtils.e("错误码:" + i + "--->" + s);
                        OrderChoosePayWayActivity.this.finish();
                        Bundle bundle=new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO,orderId);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO,"-"+totalAmount);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE,getString(R.string.weixin_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT,OrderPayResultActivity.TradeResult.fail);
                        appContext.startActivity(OrderChoosePayWayActivity.this,OrderPayResultActivity.class,bundle);
                    }

                    @Override
                    public void onPayCancel() {
                        LogUtils.e("取消");
                        OrderChoosePayWayActivity.this.finish();
                        appContext.goWeb(OrderChoosePayWayActivity.this, Urls.ORDERS,null);
                    }
                });
            }
        });
    }

    //支付宝支付
    private void ALIpay() {

        if (httpRequestDao == null) httpRequestDao = new HttpRequestDao();
        HashMap<String, String> map = new HashMap<>();
        map.put("amount", String.valueOf(totalAmount));         //支付金额
        map.put("attach", "200");
        map.put("tradeNo", orderId);        //支付单号
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        LogUtils.e("tradeNo-->" + map.get("tradeNo"));
        httpRequestDao.getALIPayDetail(mContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                AliModel aliModel = new Gson().fromJson(result, AliModel.class);
                result = aliModel.body;
                result = UrlCodeUtils.toURLDecoded(result);  //转码一次
                JPay.getIntance(OrderChoosePayWayActivity.this).toPay(JPay.PayMode.ALIPAY, result, new JPay.JPayListener() {
                    @Override
                    public void onPaySuccess() {
                        LogUtils.e("成功");
                        OrderChoosePayWayActivity.this.finish();
                        Bundle bundle=new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO,orderId);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO,"-"+totalAmount);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE,getString(R.string.zhifubao_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT,OrderPayResultActivity.TradeResult.success);
                        appContext.startActivity(OrderChoosePayWayActivity.this,OrderPayResultActivity.class,bundle);
                    }

                    @Override
                    public void onPayError(int i, String s) {
                        LogUtils.e("错误码:" + i + "--->" + s);
                        OrderChoosePayWayActivity.this.finish();
                        Bundle bundle=new Bundle();
                        bundle.putString(OrderPayResultActivity.EXTRA_BUY_INFO,orderId);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_INFO,"-"+totalAmount);
                        bundle.putString(OrderPayResultActivity.EXTRA_PAY_MODE,getString(R.string.zhifubao_pay_mode));
                        bundle.putInt(OrderPayResultActivity.EXTRA_PAY_RESULT,OrderPayResultActivity.TradeResult.fail);
                        appContext.startActivity(OrderChoosePayWayActivity.this,OrderPayResultActivity.class,bundle);
                    }

                    @Override
                    public void onPayCancel() {
                        LogUtils.e("取消");
                        appContext.goWeb(OrderChoosePayWayActivity.this, Urls.ORDERS,null);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_VERY_PHONE) {
            if (data != null && null != data.getStringExtra(EXTRA_VERY_PHONE_RESULT)) {
                String veryPhoneResult = data.getStringExtra(EXTRA_VERY_PHONE_RESULT);
                if (VERY_PHONE_SUCCESS.equals(veryPhoneResult)) {
                    VBankPay();
                }
            }
        }
    }

    private void hideSoftInput(){
        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(null!=imm){
            imm.hideSoftInputFromWindow(titleTextView.getWindowToken(),0);
        }
    }

}
