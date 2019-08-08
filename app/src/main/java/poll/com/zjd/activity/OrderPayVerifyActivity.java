package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.ClearEditText;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：更改手机绑定手机号
 */
@FmyContentView(R.layout.activity_order_pay_verify)
public class OrderPayVerifyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OrderPayVerifyActivity.class.getSimpleName();
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.login_very_code)
    private ClearEditText codeEditText;
    @FmyViewView(R.id.get_verify_code)
    private TextView getCodeTextView;
    @FmyViewView(R.id.submit_button)
    private TextView submitButton;
    @FmyViewView(R.id.phone_number)
    private TextView phoneNumber;

    protected CountDownTimer timer;
    // 验证码获取时间间隔
    public static final int GET_CODE_INTERVAL = 60 * 1000;
    /**
     * 是否正在倒计时
     */
    protected boolean isCounting = false;

    private HttpRequestDao httpRequestDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleTextView.setText(R.string.pay_verfy);
        titleBarManager.setStatusBarView();  //设置沉浸式

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        codeEditText.addTextChangedListener(textWatcher);
        submitButton.setEnabled(false);
        phoneNumber.setText(appContext.bindPhoneNumber);
        httpRequestDao = new HttpRequestDao();

    }

    @FmyClickView({R.id.head_back, R.id.get_verify_code, R.id.submit_button})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.get_verify_code:
                getVerifyCode();
                break;
            case R.id.submit_button:
                veryPhone();
                break;
            default:
                break;
        }
    }

    private void getVerifyCode() {


        if (StringUtils.isBlank(phoneNumber.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_your_phone_number, Toast.LENGTH_LONG);
            return;
        }
        if (!StringUtils.isMobile(phoneNumber.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_valid_phone_number, Toast.LENGTH_LONG);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber.getText().toString().trim());
        map.put("templeCode","1");
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(this, null, "");

        httpRequestDao.sendValidateCode(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                startCodeTimer();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });

    }

    private void startCodeTimer() {
        if (timer == null) {
            timer = new CountDownTimer(GET_CODE_INTERVAL, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String str = getResources().getString(R.string.login_resend_code);
                    str = String.format(str, millisUntilFinished / 1000);
                    getCodeTextView.setText(str);
                }

                @Override
                public void onFinish() {
                    getCodeTextView.setEnabled(true);
                    getCodeTextView.setSelected(false);
                    getCodeTextView.setText(R.string.login_get_very_code);
                    isCounting = false;
                }
            };
        }
        getCodeTextView.setEnabled(false);
        getCodeTextView.setSelected(true);
        timer.start();
        isCounting = true;
    }

    private void checkButtonStatus() {
        if (!StringUtils.isBlank(codeEditText.getText().toString().trim())) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    private void veryPhone() {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phoneNumber.getText().toString().trim());
        map.put("code", codeEditText.getText().toString().trim());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(this, null, null);
        httpRequestDao.veryPhoneNumber(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Intent data = new Intent();
                data.putExtra(OrderChoosePayWayActivity.EXTRA_VERY_PHONE_RESULT, OrderChoosePayWayActivity.VERY_PHONE_SUCCESS);
                setResult(OrderChoosePayWayActivity.RESULT_CODE_VERY_PHONE, data);
                finish();
            }
        });

    }


}
