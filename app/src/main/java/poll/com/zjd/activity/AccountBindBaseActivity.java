package poll.com.zjd.activity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import poll.com.zjd.R;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.CheckPhone;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MacUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.ClearEditText;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/12
 * 文件描述：有手机号和验证码输入的基类Activity
 */
public class AccountBindBaseActivity extends BaseActivity {
    private static final String TAG = AccountBindBaseActivity.class.getSimpleName();

    /**
     * 公共view集合
     */
    private ViewHolder viewHolder;

    protected CountDownTimer timer;
    // 验证码获取时间间隔
    public static final int GET_CODE_INTERVAL = 60 * 1000;
    /**
     * 是否正在倒计时
     */
    protected boolean isCounting = false;

    protected HttpRequestDao httpRequestDao;


    public void setViewHolder(ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
        initListener();
        initData();
    }

    private void initListener() {
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
        viewHolder.accountEditText.addTextChangedListener(textWatcher);
        viewHolder.codeEditText.addTextChangedListener(textWatcher);
    }

    private void initData() {
        viewHolder.loginButton.setEnabled(false);
        viewHolder.getCodeTextView.setEnabled(false);
        viewHolder.getCodeTextView.setSelected(false);

        httpRequestDao = new HttpRequestDao();
    }

    private void checkButtonStatus() {
        String code = viewHolder.codeEditText.getText().toString().trim();
        String phone = viewHolder.accountEditText.getText().toString().trim();

        if (StringUtils.isCode(code) && StringUtils.isMobile(phone)) {
            viewHolder.loginButton.setEnabled(true);
        } else {
            viewHolder.loginButton.setEnabled(false);
        }

        if (!isCounting) {
            if (StringUtils.isMobile(phone)) {
                viewHolder.getCodeTextView.setEnabled(true);
            } else {
                viewHolder.getCodeTextView.setEnabled(false);
            }
        }
    }

    protected void getVeryCode() {
        if (StringUtils.isBlank(viewHolder.accountEditText.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_your_phone_number, Toast.LENGTH_LONG);
            return;
        }
        if (!StringUtils.isMobile(viewHolder.accountEditText.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_valid_phone_number, Toast.LENGTH_LONG);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("phone", viewHolder.accountEditText.getText().toString().trim());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(this, null, "");

        httpRequestDao.sendCode(this, jsonObject, new OkGoStringCallback() {
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


    // sendValidateCode
    protected void getVeryCode(String type) {
        if (StringUtils.isBlank(viewHolder.accountEditText.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_your_phone_number, Toast.LENGTH_LONG);
            return;
        }
        if (!StringUtils.isMobile(viewHolder.accountEditText.getText().toString().trim())) {
            ToastUtils.showToast(this, R.string.input_valid_phone_number, Toast.LENGTH_LONG);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("phone", viewHolder.accountEditText.getText().toString().trim());
        map.put("templeCode",type);
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
                    viewHolder.getCodeTextView.setText(str);
                }

                @Override
                public void onFinish() {
                    viewHolder.getCodeTextView.setEnabled(true);
                    viewHolder.getCodeTextView.setSelected(false);
                    viewHolder.getCodeTextView.setText(R.string.login_get_very_code);
                    isCounting = false;
                }
            };
        }
        viewHolder.getCodeTextView.setEnabled(false);
        viewHolder.getCodeTextView.setSelected(true);
        timer.start();
        isCounting = true;
    }

    protected void registerPushId() {
        HashMap<String, String> map = new HashMap<>();
        map.put("device", "0");
        map.put("deviceId", MacUtil.getAdresseMAC(this));
        if (StringUtils.isBlank(AppContext.JpushId)) { // 由于网络权限问题，导致启动应用时，没有拿到id,登录时再次注册极光
            //极光推送初始化
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);
            AppContext.JpushId = JPushInterface.getRegistrationID(this);
            LogUtils.e("极光推送id"+AppContext.JpushId);
        }
        map.put("registrationId", AppContext.JpushId);


        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        httpRequestDao.registerPushId(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
            }
        });
    }


    class ViewHolder {
        public ImageView backImageView;
        public TextView titleTextView;
        public ClearEditText accountEditText;
        public ClearEditText codeEditText;
        public TextView getCodeTextView;
        public TextView loginButton;
    }
}
