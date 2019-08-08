package poll.com.zjd.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Headers;
import poll.com.zjd.R;
import poll.com.zjd.activity.BindPhoneActivity;
import poll.com.zjd.activity.GoodsDetailActivity;
import poll.com.zjd.activity.LoginActivity;
import poll.com.zjd.activity.MainActivity;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.ActivityManager;
import poll.com.zjd.model.LoginResponse;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MacUtil;
import poll.com.zjd.utils.SPUtils;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/10
 * 文件描述：微信和应用交互页面
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXEntryActivity.class.getSimpleName();
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private HttpRequestDao httpRequestDao;
    private SPUtils accessTokenSp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_entry);
        httpRequestDao = new HttpRequestDao();
        accessTokenSp = new SPUtils(WXEntryActivity.this, SPUtils.CACHE_ACCESSTOKEN_INFO);
        //如果没回调onResp，八成是这句没有写
        AppContext.mWxApi.handleIntent(getIntent(), this);

    }



    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法 //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e(TAG + "," + resp.errStr);
        LogUtils.e(TAG + "," + "错误码 : " + resp.errCode + "");
        DialogUtil.hideProgressDialog();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) {
                    ToastUtils.showToast(this, "取消分享", Toast.LENGTH_LONG);
                } else {
                    ToastUtils.showToast(this, "取消登陆", Toast.LENGTH_LONG);
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN: //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        LogUtils.d("code = " + code); //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        HashMap<String, String> map = new HashMap<>();
                        map.put("code", code);
                        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
                        DialogUtil.showProgressDialog(this, null, null);
                        httpRequestDao.login(this, jsonObject, new OkGoStringCallback() {
                            @Override
                            public void onSuccessEvent(String result) {
                                DialogUtil.hideProgressDialog();
                                Gson gson = new Gson();
                                LoginResponse loginResponse = gson.fromJson(result, LoginResponse.class);
                                if (StringUtils.isBlank(loginResponse.getPhone())) {
                                    Bundle bundle=new Bundle();
                                    bundle.putString(BindPhoneActivity.EXTRA_WX_OPENID,loginResponse.getOpenId());
                                    AppContext.getInstance().startActivity(WXEntryActivity.this, BindPhoneActivity.class, bundle);
                                } else {
                                    accessTokenSp.put(WXEntryActivity.this, AppConfig.AccessTokenSPKey.LoginId, loginResponse.getPhone());
                                    AppContext.getInstance().isGuest = false;
                                    ShoppingCartUtil.clearInstance();
                                    registerPushId();
                                    ActivityManager.getAppManager().refreshData();
                                    AppContext.getInstance().startActivity(WXEntryActivity.this, MainActivity.class, null);
                                }
                                finish();

                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                Headers headers = response.headers();
                                if (!StringUtils.isBlank(headers.get(Urls.TOKEN))) {
                                    accessTokenSp.put(WXEntryActivity.this, AppConfig.AccessTokenSPKey.AccessToken, headers.get(Urls.TOKEN));
                                    AppContext.getInstance().token = headers.get(Urls.TOKEN);
                                }
                                super.onSuccess(response);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                DialogUtil.hideProgressDialog();
                                finish();
                            }
                        });
                    break;
                    case RETURN_MSG_TYPE_SHARE:
                        ShareUtils.dismissShareDialog();
                        ToastUtils.showToast(this,"分享成功",1);
                        finish();
                    break;
                }
                break;
        }
    }

    private void registerPushId(){
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
}

