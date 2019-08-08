package poll.com.zjd.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.PasswordView;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：设置支付密码-设置支付密码
 */
@FmyContentView(R.layout.activity_set_pay_real_password)
public class SetPayRealPasswordActivity extends BaseActivity implements View.OnClickListener, PasswordView.callBack {
    private static final String TAG = SetPayRealPasswordActivity.class.getSimpleName();
    public static final String EXTRA_VERY_CODE = "password.very.code";
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.password_view_first)
    private PasswordView firstPasswordView;
    @FmyViewView(R.id.password_view_confirm)
    private PasswordView confirmPasswordView;
    @FmyViewView(R.id.text_view_prompt)
    private TextView promptTextView;
    @FmyViewView(R.id.submit_button)
    private TextView submitTextView;
    @FmyViewView(R.id.password_view_notice_one)
    private TextView noticeOneTxt;
    @FmyViewView(R.id.password_view_notice_two)
    private TextView noticeTwoTxt;

    private HttpRequestDao httpRequestDao;
    private String veryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式
        if (appContext.hasPayPassword) {
            titleTextView.setText(R.string.modify_pay_password);
        } else {
            titleTextView.setText(R.string.set_pay_password);
        }

        submitTextView.setEnabled(false);
        firstPasswordView.setCallBack(this);
        firstPasswordView.setInputType(InputType.TYPE_CLASS_NUMBER);
        confirmPasswordView.setCallBack(this);
        confirmPasswordView.setInputType(InputType.TYPE_CLASS_NUMBER);


        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                veryCode = bundle.getString(EXTRA_VERY_CODE);
            }
        }
        httpRequestDao = new HttpRequestDao();

    }

    @FmyClickView({R.id.head_back, R.id.submit_button})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.submit_button:
                modifyPassword();
                break;
            default:
                break;
        }
    }

    private void modifyPassword() {
        if (appContext.hasPayPassword) {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", appContext.bindPhoneNumber);
            map.put("payPassword", confirmPasswordView.getPassword());
            JSONObject jsonObject = JsonUtils.convertJSONObject(map);
            DialogUtil.showProgressDialog(this, null, null);
            httpRequestDao.resetPassword(this, jsonObject, new OkGoStringCallback() {
                @Override
                public void onSuccessEvent(String result) {
                    DialogUtil.hideProgressDialog();
                    ToastUtils.showToast(R.string.update_password_success);
                    setResult(SetPayPasswordVeryPhoneActivity.RESULT_CODE_SAVE_PASSWROD_SUCCESS);
                    finish();
                }

                @Override
                public void onError(Response<String> response) {
                    super.onError(response);
                    DialogUtil.hideProgressDialog();
                }
            });
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("payPassword", confirmPasswordView.getPassword());
//            map.put("validateCode", veryCode);
            JSONObject jsonObject = JsonUtils.convertJSONObject(map);
            DialogUtil.showProgressDialog(this, null, null);
            httpRequestDao.savePassword(this, jsonObject, new OkGoStringCallback() {
                @Override
                public void onSuccessEvent(String result) {
                    DialogUtil.hideProgressDialog();
                    appContext.hasPayPassword = true;
                    ToastUtils.showToast(R.string.save_password_success);
                    setResult(SetPayPasswordVeryPhoneActivity.RESULT_CODE_SAVE_PASSWROD_SUCCESS);
                    finish();
                }

                @Override
                public void onError(Response<String> response) {
                    super.onError(response);
                    DialogUtil.hideProgressDialog();
                }
            });
        }
    }

    @Override
    public void complete(String password) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkSetPassword();
    }

    private void checkSetPassword() {
        if (firstPasswordView.isComplete() && confirmPasswordView.isComplete()) {
            if (firstPasswordView.getPassword().equals(confirmPasswordView.getPassword())) {
                submitTextView.setEnabled(true);
                promptTextView.setVisibility(View.INVISIBLE);
                hideSoftInputMethod();

            } else {
                submitTextView.setEnabled(false);
                promptTextView.setVisibility(View.VISIBLE);
                confirmPasswordView.clearText();
            }
        } else {
            submitTextView.setEnabled(false);
            promptTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void checkModifyPassword() {
        if (firstPasswordView.isComplete() && confirmPasswordView.isComplete()) {
            submitTextView.setEnabled(true);
            promptTextView.setVisibility(View.INVISIBLE);
            hideSoftInputMethod();
        } else {
            submitTextView.setEnabled(false);
            promptTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(firstPasswordView.getWindowToken(), 0);
    }
}
