package poll.com.zjd.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.fragment.MyFragment;
import poll.com.zjd.manager.UrlSchemeManager;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.utils.UrlCodeUtils;
import poll.com.zjd.utils.WebViewUtils;
import poll.com.zjd.wxapi.WXshareUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/20  18:08
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */
@FmyContentView(R.layout.activity_webview)
public class WebViewActivity extends BaseActivity implements View.OnClickListener  {
    public static final String LOADURL = "loadUrl";
    public static final String DATA = "data";

    private String url;
    private Map<String, String > header;

    private static final int FILE_SELECT_CODE = 0;  //图片上传
    private static final int SELECT_ADDRESS = 1;  //选择地址

    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上

    @FmyViewView(R.id.web_activity)
    private WebView mWebView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_icon_right)
    private ImageView rightHeadIcon;
    @FmyViewView(R.id.fra_nonetwork)
    private RelativeLayout mNonetwork;

    private String initUrl = "";     //初始url
    private String currentUrl = "";  //当前url
    private String orderNo;          //订单号
    private boolean needClearHistory = false; //是否需要清空历史
    public static boolean isLoading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBarManager.setStatusBarView();  //设置沉浸式
        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                url = bundle.getString(LOADURL);
                initUrl = url;
            }
        }
        if (AppContext.isNetworkAvailable(mContext)){
            initWebView(mWebView);
            loadUrl(url);
        }else {
            mNonetwork.setVisibility(View.VISIBLE);
        }
    }

    //加载
    private void loadUrl(String url){
        header = new HashMap<>() ;
        header.put("token", AppContext.getInstance().token);
        LogUtils.e("header-web->"+header.toString());
        LogUtils.i(url);
        mWebView.loadUrl(url,header);
    }

    private void initWebView(WebView webView){

        WebViewUtils.initwebView(mContext,webView);

        webView.setWebChromeClient(new MyWebChromeClient());//设置可以打开图片管理器

        webView.setWebViewClient(new MyWebViewClient());

    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("shouldOverrideUrlLoading-->"+url);
            if (!currentUrl.contains(AppConfig.HOST_URL.substring(0,10))&&isLoading){
                isLoading = false;
                goWeb(url,null);
                return true;
//                finish();
            }



            //判断用户单击的是那个超连接
            String tag="tada:tel";
//            String tag="http";
            if (url.contains(tag)){
                PackageManager pm = getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.CALL_PHONE", "poll.com.zjd"));
                if (permission) {
                    String mobile=url.substring(url.lastIndexOf("/")+1);
                    LogUtils.e("电话号码:"+mobile);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+ mobile.trim()));//mobile为要拨打的号码
                    intent.addCategory("android.intent.category.DEFAULT");
//                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+mobile.trim()));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    try{
                        WebViewActivity.this.startActivity(intent);
                    }catch (Exception e){
                        ToastUtils.showToast("拨号异常");
                    }
                }else {
                    ToastUtils.showToast("您还没开启打电话的权限哦");
//                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
//                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//                    }
                }
                return true;
            }

            toDo(view,url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.e("网页加载完成时-->"+url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.e("开始加载网页时-->"+url);
            currentUrl = url;
            if (!currentUrl.contains(AppConfig.HOST_URL.substring(0,10))&&isLoading){
                isLoading = false;
                goWeb(url,null);
                return;
//                finish();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            LogUtils.e("页面资源加载时-->"+url);
            super.onLoadResource(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            if (needClearHistory) {
                needClearHistory = false;
                mWebView.clearHistory();//清除历史记录
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e(requestCode+"-----"+permissions+"----"+grantResults);
    }

    private class MyWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILE_SELECT_CODE);
            return true;
        }
    }

    private void goWeb(String url, String json) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.LOADURL, url);
        if (StringUtils.isNotEmpty(json)) {
            bundle.putString(WebViewActivity.DATA, json);
        }
        appContext.startActivityForResult(this, LogisticsWebActivity.class, MainActivity.GOWHERE, bundle);
        if (mWebView!=null){
            mWebView.goBack();
        }
    }

    @FmyClickView({R.id.head_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == SELECT_ADDRESS){
            if(data.getStringExtra(SelectorLocationActivity.USERNAME)==null){
                ToastUtils.showToast(this,"请选择详细地址");
                return;
            }
            String userName = UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(data.getStringExtra(SelectorLocationActivity.USERNAME)));
            String phone = UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(data.getStringExtra(SelectorLocationActivity.PHONE)));
            String province = UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(data.getStringExtra(SelectorLocationActivity.PROVINCE)));
            String city = UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(data.getStringExtra(SelectorLocationActivity.CITY)));
            String district = UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(data.getStringExtra(SelectorLocationActivity.DISTRICT)));
            String address = UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(data.getStringExtra(SelectorLocationActivity.LOCATIONINFO)));

            loadUrl(String.format(Urls.CHOOSEADDRESS,userName,phone, province,city,district,address));
            return;
        }

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case FILE_SELECT_CODE: {
                if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                    Uri uri = data.getData();
                    Uri[] uris = new Uri[]{uri};
                    ClipData clipData = data.getClipData();  //选择多张
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri_x = item.getUri();
                            uris[i]=uri_x;
                        }
                    }
                    mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
                } else {//4.4以下处理
                    Uri uri = data.getData();
                    mUploadMessage.onReceiveValue(uri);
                }
            }
            break;
        }
    }


    //各种跳转这里处理
    public void toDo(WebView view, String url){
        Intent intent = new Intent();
        switch (UrlSchemeManager.urlAnalysisAction(UrlSchemeManager.ACTIVITY,url)){

            case UrlSchemeManager.NONE:

                view.loadUrl(url,header);
                break;
            case UrlSchemeManager.DOANDDONOTTH:

                break;
            case UrlSchemeManager.DOANDFINISH:
                onBackPressed();
                break;
            case UrlSchemeManager.FRG_TOHOME:
                intent.putExtra(MainActivity.GOWHERETYPE, 0);
                this.setResult(MainActivity.GOWHERE, intent);
                appContext.startActivity(mContext,MainActivity.class,null);
//                ActivityManager.getAppManager().finishAllActivity(MainActivity.class);
                onBackPressed();
                break;
            case UrlSchemeManager.ACT_TOSHOP:
                intent.putExtra(MainActivity.GOWHERETYPE, 3);
                this.setResult(MainActivity.GOWHERE, intent);
                onBackPressed();
                break;
            case UrlSchemeManager.ACT_MY:
                intent.putExtra(MainActivity.GOWHERETYPE, 4);
                this.setResult(MainActivity.GOWHERE, intent);
                onBackPressed();
                break;
            case UrlSchemeManager.FRG_SHARE://邀请好友
                if(AppContext.getInstance().isLogin()){
                    ShareUtils.shareDialog(mContext, res, String.format(WXshareUtils.addFriendTitle,AppContext.getUserName(mContext)),
                            WXshareUtils.addFriendDescription,String.format(WXshareUtils.addFriendUrl,AppContext.getUserId(mContext)),null);
                }else {
                    ToastUtils.showToast(mContext,res.getString(R.string.login_notExistError),1);
                }
                break;
            case UrlSchemeManager.FRG_SIGN:
                //本来是签到的,先用来测试(获取地址)
                appContext.startActivityForResult(WebViewActivity.this,SelectorLocationActivity.class,SELECT_ADDRESS,null);
                break;
        }
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!currentUrl.contains(AppConfig.HOST_URL)){
            //如果是物流查寻页面,直接退出
//            if (initUrl.equals(Urls.ORDERS)){
//                needClearHistory = true;
//                loadUrl(Urls.ORDERS);  //跳转到我的订单
//            }else {
//                onBackPressed();
//            }
//            return true;
        }
        //判断是否是这里跳转到的订单,不然back键返回不了
//        if ()

        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public synchronized void onBackPressed() {
        if(Urls.COUPONS.equals(url)){
            setResult(MyFragment.ResultCode.backFromCouponsList);
        }
        super.onBackPressed();
    }
}
