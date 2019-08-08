package poll.com.zjd.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;



/**
 * Created by hufei on 2016/11/17.
 * 初始化webView设置
 */

public class WebViewUtils {

    public static void initwebView(final Context mContext, WebView webView) {
        WebSettings websetting = webView.getSettings();

        // User settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            websetting.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        websetting.setJavaScriptEnabled(true);    //设置webView支持javascript
        // 设置允许JS弹窗
        websetting.setJavaScriptCanOpenWindowsAutomatically(true);
        websetting.setLoadsImagesAutomatically(true);    //支持自动加载图片
        websetting.setBlockNetworkImage(false);
        //设置网页自适应
        websetting.setUseWideViewPort(true);    //设置webView推荐使用的窗口，使html界面自适应屏幕
        websetting.setLoadWithOverviewMode(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            websetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
//        }

        websetting.setSaveFormData(true);    //设置webView保存表单数据
//        websetting.setSavePassword(true);	//设置webView保存密码
//        websetting.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);	//设置中等像素密度，medium=160dpi
        websetting.setSupportZoom(true);    //支持缩放
        websetting.setGeolocationEnabled(true);//启用地理定位

        CookieManager.getInstance().setAcceptCookie(true);
        websetting.setAppCacheEnabled(true);
        websetting.setDatabaseEnabled(true);
        websetting.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT > 8) {
            websetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Boolean.parseBoolean("true")) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Technical settings
        webView.setLongClickable(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setDrawingCacheEnabled(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }

    /**
     * 返回Chrome内核
     */
    public static WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            //定位  配置
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        };
    }

    /**
     * 返回玩个加载监听
     */
//    public static WebViewClient getWebViewClient() {
//        return new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                //handler.cancel(); 默认的处理方式，WebView变成空白页
//                handler.proceed();//接受证书
//                //handleMessage(Message msg); 其他处理
//            }
//
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                view.loadUrl("file:///android_asset/error.html");
//            }
//        };
//    }
}
