/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package poll.com.zjd.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import poll.com.zjd.R;
import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.activity.LoginActivity;
import poll.com.zjd.activity.MainActivity;
import poll.com.zjd.activity.RegLoginActivity;
import poll.com.zjd.activity.WebViewActivity;
import poll.com.zjd.fragment.MyFragment;
import poll.com.zjd.manager.ActivityManager;
import poll.com.zjd.model.ProductBean;
import poll.com.zjd.utils.ActivityEffectUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.SPUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  10:17
 * 包名:     poll.com.zjd.app
 * 项目名:   zjd
 */
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
public class AppContext extends android.app.Application {
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
//    public static final String APP_ID = "wx8f9874ecc7b92d84";
//    public static final String APP_ID = "wx99f59e21dc05108b"; //大咖秀秀的，临时使用
    public static final String APP_ID = "wxb57ed4513a1be814";   //臻酒到 微信ID
    public static final String APP_ID_BUGLY = "653c665f55";     //BUGLY自动更新
    public static final String APP_ID_WEIBO = "354925749";      //微博APPID
    public static Handler handler;
    private static AppContext context;
    private ActivityManager activityManager;


    // IWXAPI 是第三方app和微信通信的openapi接口
    public static IWXAPI mWxApi;

    public boolean isGuest = true;

    // 与平台维护session 的token
    public String token = "";
    //之前登录过的手机号
    public String phoneNumber;
    //当前定位到的城市
    public  String city = null;
    //用户选择的城市
    public String chooseCity = null;

    //购物车中的商品列表
    public List<ProductBean> productBeanList;

    public static String JpushId = "";  //极光推送机器id //170976fa8a84ba7db2b
    /**
     * 绑定的微信名
     */
    public String bindWxName;
    /**
     * 绑定的手机号
     */
    public String bindPhoneNumber;

    // 是否设置了支付密码
    public boolean hasPayPassword = false;

    private static SPUtils userSP;

    public static AppContext getInstance() {
        return context;
    }

    public static Handler getMainHandler() {
        return handler;
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        handler = new Handler();
        activityManager = ActivityManager.getAppManager();
        //网络判断
        if (!isNetworkAvailable(this)){
            ToastUtils.showToast(getApplicationContext(),"您当前网络不可用");
        }

        WbSdk.install(this,new AuthInfo(this,  Constants.APP_KEY,  Constants.REDIRECT_URL, Constants.SCOPE));
        //网络请求框架初始化
        initOkHttp();
        userSP = new SPUtils(this, SPUtils.CACHE_USER_INFO);
        //友盟统计初始化
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);//设置统计场景
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计方式，这样将不会再自动统计Activity
        //极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JpushId = JPushInterface.getRegistrationID(this);
        LogUtils.e("极光推送id"+JpushId);
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        registerWeChat();
        SPUtils spUtils = new SPUtils(this, SPUtils.CACHE_ACCESSTOKEN_INFO);
        String token = (String) (spUtils.get(this, AppConfig.AccessTokenSPKey.AccessToken, ""));
        if (!StringUtils.isBlank(token)) {
            this.token = token;
            isGuest = false;
        }

//        CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APP
//                ID", false);
    }

    //获取用户名字
    public static String getUserName(Context context){
        return (String) userSP.get(context, MyFragment.SP_USER_HEAD_NAME,"");
    }

    public static String getUserId(Context context){
        return (String) userSP.get(context, MyFragment.SP_USER_ID,"");
    }

    /**
     * 跳转activity
     */
    public void startActivity(Context context, Class<?> toActivity, Bundle bundle) {
        this.startActivity(context, toActivity.getName(), bundle);
    }

    /**
     * 跳转activity
     */
    public void startActivity(Context context, String activityName, Bundle bundle) {
        activityName = activityName.trim();
        if (context == null) {
            Activity activity = activityManager.currentActivity();
            context = activity == null ? this : activity;
        }

        if (StringUtils.isEmpty(activityName)) {
            ToastUtils.showToast(context, R.string.function_not_open, Toast.LENGTH_SHORT);
            return;
        }

        //过滤登录权限
        activityName = loginPermissionFilter(context, activityName);

        startActivityForRoot(context, activityName, bundle);
    }

    /**
     * startActivityForResult 跳转
     */
    public void startActivityForResult(Activity context, Class<? extends BaseActivity> toActivity, int requestCode, Bundle bundle) {
        startActivityForResult(context, toActivity.getName(), requestCode, bundle);
    }

    /**
     * startActivityForResult 跳转
     */
    public void startActivityForResult(Activity context, String activityName, int requestCode, Bundle bundle) {

        if (StringUtils.isEmpty(activityName)) {
            ToastUtils.showToast(context, R.string.function_not_open, Toast.LENGTH_SHORT);
            return;
        }
        //过滤登录权限
        activityName = loginPermissionFilter(context, activityName);


        BaseActivity currentActivity = activityManager.currentActivity();
        if (currentActivity != null) {
            //阻止有些跳转的Activity就是当前的Activity
            if (activityName.equals(currentActivity.getClass().getName())
                    && (RegLoginActivity.class.getName().equals(activityName))) {
                return;
            }
        }

        Intent intent = new Intent();
        intent.setClassName(context, activityName);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);

        onActivityAnim(context, activityName, true);
    }

    public void startActivityForRoot(Context context, String str, Bundle bundle) {
        LogUtils.i("TO_ACTIVITY_" + str);

        Intent intent = new Intent();
//        String a="{\"url\":\"http://node.17donor.cn/1/check\",\"title\":\"健康自测\"}";
        if (str.startsWith("{") && str.endsWith("}")) {
            if (!isNetworkConnected()) {
                this.displayNotConnectedNetwork();
                return;
            }
            if (bundle == null) {
                bundle = new Bundle();
            }
//			bundle.putString(WebViewActivity.StateConfigModelJSON, str);
//			intent.setClass(context, WebViewActivity.class);
        } else {
            try {
                intent.setClass(context, Class.forName(str));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                ToastUtils.showToast(context, "找不到对应的界面", Toast.LENGTH_SHORT);
                return;
            }
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        BaseActivity activity = activityManager.currentActivity();
        if (activity != null) {
            //阻止有些跳转的Activity就是当前的Activity
            if (str.equals(activity.getClass().getName())
                    && (RegLoginActivity.class.getName().equals(str))) {
                return;
            } else {
//				activity.hideLoadingDialog();
            }
        }
        context.startActivity(intent);

        onActivityAnim(context, str, true);
    }

    /**
     * 用户是否已经登陆
     */
    public boolean isLogin() {
        return !isGuest;
    }

    /**
     * 是否用微信登录过
     */
    public boolean isWeixinLogin(Context context) {
        return false;
    }

    /**
     * 检测网络是否可用
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 显示未联网信息
     */
    public void displayNotConnectedNetwork() {
        ToastUtils.showToast(this, R.string.network_not_connected, Toast.LENGTH_LONG);
    }

    /**
     * 进入传入Activity的拦截器
     *
     * @param mContext
     * @param activityClassName
     * @return 返回过滤后的ActivityName
     */
    public String loginPermissionFilter(Context mContext, String activityClassName) {
        List<String> needLoginArr = Arrays.asList(getResources().getStringArray(R.array.array_need_login_mode));
        // 判断需要登陆权限
        if (activityClassName.equals(RegLoginActivity.class.getName())
                || (needLoginArr.contains(activityClassName) && !isLogin())) {
            return getLoginActivityName(mContext);
        }
        return activityClassName;
    }

    /**
     * @return 跳转登录的Activity名称, 前提是没有登录
     */
    public String getLoginActivityName(Context mContext) {
        //判断是否有微信第三方登录
        return LoginActivity.class.getName();
    }

    /**
     * Activity跳转动画
     */
    public void onActivityAnim(Context mContext, String activityName, boolean isEnter) {
        //无动画
        if (isEnter) {
            String[] noAnimArr = getResources().getStringArray(R.array.array_no_anim_enter);
            boolean find = false;
            for (String name : noAnimArr) {
                if (name.equals(activityName)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                ((Activity) mContext).overridePendingTransition(0, 0);
                return;
            }
        } else {
            String[] noAnimArr = getResources().getStringArray(R.array.array_no_anim_outer);
            boolean find = false;
            for (String name : noAnimArr) {
                if (name.equals(activityName)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                ((Activity) mContext).overridePendingTransition(0, 0);
                return;
            }
        }

        //有动画
        String[] needArr = getResources().getStringArray(R.array.array_enter_top_out_buttom_anim);
        boolean hasButtonActivityAnim = false;
        for (String name : needArr) {
            if (name.equals(activityName)) {
                hasButtonActivityAnim = true;
                break;
            }
        }
        if (hasButtonActivityAnim) {
            if (isEnter) {
                ActivityEffectUtils.setActivityEffect(mContext, ActivityEffectUtils.ENTERBOTTOMTOTOP);
            } else {
                ActivityEffectUtils.setActivityEffect(mContext, ActivityEffectUtils.OUTTOPTOBOTTOM);
            }
        } else {
            if (isEnter) {
                ActivityEffectUtils.setActivityEffect(mContext, ActivityEffectUtils.ENTERRIGHTTOLEFT);
            } else {
                ActivityEffectUtils.setActivityEffect(mContext, ActivityEffectUtils.OUTLEFTTORIGHT);
            }
        }
    }

    /**
     * 网络请求初始化
     */
    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间 10
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //证书
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
//-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数

    }

    private void registerWeChat() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        mWxApi.registerApp(APP_ID);
    }

    public static String getVersionName() {
        String versionName = null;

        try {
            String pkName = AppContext.getInstance().getApplicationContext().getPackageName();
            versionName = AppContext.getInstance().getApplicationContext().getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (Exception e) {
            LogUtils.i(e.toString());
        }
        return versionName;

    }

    public void toMainActivity(Context context, int pageIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MainActivity.EXTRA_PAGE_INDEX,pageIndex);
        context.startActivity(intent);

        onActivityAnim(context, MainActivity.class.getName(), true);
    }

    public void goWeb(Activity context,String url, String json) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.LOADURL, url);
        if (StringUtils.isNotEmpty(json)) {
            bundle.putString(WebViewActivity.DATA, json);
        }
        startActivityForResult(context, WebViewActivity.class, MainActivity.GOWHERE, bundle);
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()){
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED){
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

}
