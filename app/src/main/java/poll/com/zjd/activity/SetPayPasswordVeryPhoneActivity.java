package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.view.ClearEditText;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：设置支付密码-验证手机号
 */
@FmyContentView(R.layout.activity_set_pay_password_very_phone)
public class SetPayPasswordVeryPhoneActivity extends AccountBindBaseActivity implements View.OnClickListener {
    private static final String TAG = ChangePhoneNumberActivity.class.getSimpleName();
    public static final int RESULT_CODE_SAVE_PASSWROD_SUCCESS=333;
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.login_account)
    private ClearEditText accountEditText;
    @FmyViewView(R.id.login_very_code)
    private ClearEditText codeEditText;
    @FmyViewView(R.id.get_verify_code)
    private TextView getCodeTextView;
    @FmyViewView(R.id.login_button)
    private TextView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.accountEditText = accountEditText;
        viewHolder.backImageView = backImageView;
        viewHolder.codeEditText = codeEditText;
        viewHolder.getCodeTextView = getCodeTextView;
        viewHolder.loginButton = nextButton;
        viewHolder.titleTextView = titleTextView;
        setViewHolder(viewHolder);

        initData();
    }

    private void initData() {
        if (appContext.hasPayPassword) {
            titleTextView.setText(R.string.modify_pay_password);
        } else {
            titleTextView.setText(R.string.set_pay_password);
        }
        nextButton.setText(R.string.next);
        titleBarManager.setStatusBarView();  //设置沉浸式

        accountEditText.setText(appContext.bindPhoneNumber);
        accountEditText.setEnabled(false);
        accountEditText.setTextColor(getResources().getColor(R.color.gray_929292));
        codeEditText.requestFocus();

    }

    @FmyClickView({R.id.head_back, R.id.get_verify_code, R.id.login_button})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.get_verify_code:
                getVeryCode("0");
                break;
            case R.id.login_button:
                HashMap<String, String> map = new HashMap<>();
                map.put("phone", accountEditText.getText().toString().trim());
                map.put("code", codeEditText.getText().toString().trim());
                JSONObject jsonObject = JsonUtils.convertJSONObject(map);
                DialogUtil.showProgressDialog(this, null, null);
                httpRequestDao.veryPhoneNumber(this, jsonObject, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        DialogUtil.hideProgressDialog();
                        Bundle bundle=new Bundle();
                        bundle.putString(SetPayRealPasswordActivity.EXTRA_VERY_CODE,codeEditText.getText().toString().trim());
                        appContext.startActivityForResult(SetPayPasswordVeryPhoneActivity.this, SetPayRealPasswordActivity.class,0, bundle);
                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_CODE_SAVE_PASSWROD_SUCCESS){
            finish();
        }
    }
}
