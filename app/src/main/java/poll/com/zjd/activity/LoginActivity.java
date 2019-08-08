package poll.com.zjd.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

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
import poll.com.zjd.app.AppContext;
import poll.com.zjd.fragment.MyFragment;
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
 * 创建时间: 2017/7/9
 * 文件描述：登录页面
 */
@FmyContentView(R.layout.activity_login)
public class LoginActivity extends AccountBindBaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
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
    @FmyViewView(R.id.login_other_mode)
    private ImageView otherModeImageView;

    private SPUtils accessTokenSp;
    private SPUtils userSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBarManager.setStatusBarView();  //设置沉浸式
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.accountEditText = accountEditText;
        viewHolder.backImageView = backImageView;
        viewHolder.codeEditText = codeEditText;
        viewHolder.getCodeTextView = getCodeTextView;
        viewHolder.loginButton = loginButton;
        viewHolder.titleTextView = titleTextView;
        setViewHolder(viewHolder);

        initData();
    }


    private void initData() {
        titleTextView.setText(R.string.login);
        userSp = new SPUtils(LoginActivity.this, SPUtils.CACHE_USER_INFO);
        accessTokenSp = new SPUtils(this, SPUtils.CACHE_ACCESSTOKEN_INFO);
    }

    @FmyClickView({R.id.head_back, R.id.get_verify_code, R.id.login_button, R.id.login_other_mode})
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
            case R.id.login_other_mode:
                loginWithWeiXin();
                break;
            case R.id.login_button:
                doLogin();
                break;
            default:
                break;
        }

    }


    private void loginWithWeiXin() {
        if (!AppContext.mWxApi.isWXAppInstalled()) {
            ToastUtils.showToast(this, R.string.wx_not_installed, Toast.LENGTH_LONG);
            return;
        }
        DialogUtil.showProgressDialog(this,null,null);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        AppContext.mWxApi.sendReq(req);
    }

    private void doLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", accountEditText.getText().toString().trim());
        map.put("validateCode", codeEditText.getText().toString().trim());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(LoginActivity.this, null, getResources().getString(R.string.wait_for_login));
        httpRequestDao.login(this, jsonObject, new OkGoStringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                Headers headers = response.headers();
                if (!StringUtils.isBlank(headers.get(Urls.TOKEN))) {
                    accessTokenSp.put(LoginActivity.this, AppConfig.AccessTokenSPKey.AccessToken, headers.get(Urls.TOKEN));
                    appContext.token = headers.get(Urls.TOKEN);
                }
                super.onSuccess(response);
            }

            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson=new Gson();
                LoginResponse loginResponse=gson.fromJson(result,LoginResponse.class);
                accessTokenSp.put(LoginActivity.this,AppConfig.AccessTokenSPKey.LoginId,loginResponse.getPhone());
                if (StringUtils.isNotEmpty(loginResponse.getLogName())){
                    userSp.put(LoginActivity.this, MyFragment.SP_USER_HEAD_NAME,loginResponse.getLogName());
                }
                if (loginResponse.getId()!=null){
                    userSp.put(LoginActivity.this, MyFragment.SP_USER_ID,loginResponse.getId());
                }
                appContext.isGuest = false;
                ShoppingCartUtil.clearInstance();
                registerPushId();
                activityManager.refreshData();
                appContext.startActivity(LoginActivity.this, MainActivity.class, null);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });
    }
}
