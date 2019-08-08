package poll.com.zjd.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.Urls;
import poll.com.zjd.utils.LogUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/9/9
 * 文件描述：支付订单成功提示页面
 */
@FmyContentView(R.layout.activity_order_pay_result)
public class OrderPayResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OrderPayResultActivity.class.getSimpleName();
    public static final String EXTRA_BUY_INFO = "poll.com.zjd.activity.buy.info";
    public static final String EXTRA_PAY_INFO = "poll.com.zjd.activity.pay.info";
    public static final String EXTRA_PAY_MODE = "poll.com.zjd.activity.pay.mode";
    public static final String EXTRA_PAY_RESULT = "poll.com.zjd.activity.pay.result";
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.buy_info_txt)
    private TextView buyInfoTxt;
    @FmyViewView(R.id.pay_info_txt)
    private TextView payInfoTxt;
    @FmyViewView(R.id.pay_mode_txt)
    private TextView payModeTxt;
    @FmyViewView(R.id.trade_result_img)
    private ImageView tradeResultImageView;
    @FmyViewView(R.id.trade_result_txt)
    private TextView tradeResultTxt;

    public interface TradeResult{
        int success=1;
        int fail=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式

        titleTextView.setText(R.string.pay_success);


        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                String buyInfo = bundle.getString(EXTRA_BUY_INFO);
                String payInfo = bundle.getString(EXTRA_PAY_INFO);
                String payMode = bundle.getString(EXTRA_PAY_MODE);
//                buyInfoTxt.setText(buyInfo);
                payInfoTxt.setText(payInfo);
                payModeTxt.setText(payMode);

                int result = bundle.getInt(EXTRA_PAY_RESULT);
                if(result==TradeResult.success){
                    tradeResultImageView.setImageResource(R.drawable.pay_success);
                    tradeResultTxt.setText(R.string.trade_success);
                }else{
                    tradeResultImageView.setImageResource(R.drawable.pay_fail);
                    tradeResultTxt.setText(R.string.trade_fail);
                }
            }
        }
    }

    @FmyClickView({R.id.head_back, R.id.order_check_txt, R.id.browser_goods_txt})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.order_check_txt:
                this.finish();
                appContext.goWeb(this, Urls.ORDERS, null);
                break;
            case R.id.browser_goods_txt:
                this.finish();
                appContext.toMainActivity(this, 0);
                break;
            default:
                break;
        }
    }
}
