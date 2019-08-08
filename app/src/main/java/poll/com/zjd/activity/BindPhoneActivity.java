package poll.com.zjd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Headers;
import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.model.AddressBean;
import poll.com.zjd.model.CheckPhone;
import poll.com.zjd.model.LoginResponse;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.SPUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.ClearEditText;


/**
 * 创建者:   marydeng
 * 创建时间: 207/7/12
 * 文件描述：绑定手机号页面
 */
@FmyContentView(R.layout.activity_bind_phone)
public class BindPhoneActivity extends AccountBindBaseActivity implements View.OnClickListener {
    private static final String TAG = BindPhoneActivity.class.getSimpleName();
    public static final String EXTRA_WX_OPENID = "poll.com.zjd.activity.open.id";
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
    private TextView loginButton;
    private String openId;
    private SPUtils accessTokenSp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.accountEditText = accountEditText;
        viewHolder.backImageView = backImageView;
        viewHolder.codeEditText = codeEditText;
        viewHolder.getCodeTextView = getCodeTextView;
        viewHolder.loginButton = loginButton;
        viewHolder.titleTextView = titleTextView;
        setViewHolder(viewHolder);


        titleBarManager.setStatusBarView();  //设置沉浸式
        titleTextView.setText(R.string.login_bind_phone);
        loginButton.setText(R.string.login_Immediately_binding);


        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                openId = bundle.getString(EXTRA_WX_OPENID);
            }
        }

        accessTokenSp = new SPUtils(this, SPUtils.CACHE_ACCESSTOKEN_INFO);

    }

    @FmyClickView({R.id.head_back, R.id.get_verify_code, R.id.login_button})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.get_verify_code:
                getVeryCode();
                break;
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.login_button:
                checkPhone();
                break;
            default:
                break;
        }

    }

    private void checkPhone(){
        httpRequestDao.checkPhone(this, accountEditText.getText().toString().trim(), new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                CheckPhone checkPhone = new Gson().fromJson(result,CheckPhone.class);
                if (checkPhone.getIsBind()==0){
                    //未注册
                    doBind();
                }else {
                    //已注册
                    ToastUtils.showToast(BindPhoneActivity.this,"该手机号已绑定过",1);
                }
            }
        });
    }


    private void doBind() {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", accountEditText.getText().toString().trim());
        map.put("validateCode", codeEditText.getText().toString().trim());
        map.put("openId", openId);
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(BindPhoneActivity.this, null, getResources().getString(R.string.wait_for_login));
        httpRequestDao.login(this, jsonObject, new OkGoStringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                Headers headers = response.headers();
                if (!StringUtils.isBlank(headers.get(Urls.TOKEN))) {
                    accessTokenSp.put(BindPhoneActivity.this, AppConfig.AccessTokenSPKey.AccessToken, headers.get(Urls.TOKEN));
                    appContext.token = headers.get(Urls.TOKEN);
                }
                super.onSuccess(response);
            }

            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(result, LoginResponse.class);
                accessTokenSp.put(BindPhoneActivity.this, AppConfig.AccessTokenSPKey.LoginId, loginResponse.getPhone());
                appContext.isGuest = false;
                ShoppingCartUtil.clearInstance();
                registerPushId();
                activityManager.refreshData();
                appContext.startActivity(BindPhoneActivity.this, MainActivity.class, null);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });
    }

}
