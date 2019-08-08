package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.utils.LogUtils;


/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：账户与安全->绑定的手机号
 */
@FmyContentView(R.layout.activity_account_safe)
public class AccountSafeActivity extends BaseActivity implements View.OnClickListener {
    public static final int RESULT_CODE_CHANGE_PHONE = 222;
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.phone_number_relative_layout)
    private RelativeLayout phoneNumberRelativeLayout;
    @FmyViewView(R.id.pay_password_relative_layout)
    private RelativeLayout payPasswordRelativeLayout;
    @FmyViewView(R.id.bind_phone_number_txt)
    private TextView bindPhoneNumberTxt;
    @FmyViewView(R.id.bind_wx_name_txt)
    private TextView bindWXNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleTextView.setText(R.string.account_and_safe);
        titleBarManager.setStatusBarView();  //设置沉浸式

        bindPhoneNumberTxt.setText(appContext.bindPhoneNumber);
        bindWXNameTxt.setText(appContext.bindWxName);

    }

    @FmyClickView({R.id.head_back, R.id.phone_number_relative_layout, R.id.pay_password_relative_layout})
    @Override
    public void onClick(View view) {
        LogUtils.i("view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.phone_number_relative_layout:
                appContext.startActivityForResult(this, ChangePhoneNumberActivity.class, 0, null);
                break;
            case R.id.pay_password_relative_layout:
                appContext.startActivity(this, SetPayPasswordVeryPhoneActivity.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE_CHANGE_PHONE) {
            bindPhoneNumberTxt.setText(appContext.bindPhoneNumber);
        }
    }
}
