package poll.com.zjd.fragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.HttpRequestApi;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.manager.UrlSchemeManager;
import poll.com.zjd.model.SignBean;
import poll.com.zjd.model.SignCouponBean;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.utils.WebViewUtils;
import poll.com.zjd.wxapi.WXshareUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:58
 * 包名:     poll.com.zjd.fragment
 * 项目名:   zjd
 */

public class ZjzFragment extends BaseFragment implements View.OnClickListener {

    @FmyViewView(R.id.fra_web)
    private WebView mWebView;
    @FmyViewView(R.id.home_share_r)
    private RelativeLayout mShareR;
    @FmyViewView(R.id.home_share_b)
    private Button mShareB;
    @FmyViewView(R.id.home_sign_pic)
    private ImageView mHomeSignPic;
    @FmyViewView(R.id.home_sign_text)
    private TextView mSignText;
    @FmyViewView(R.id.home_sign_coupon)
    private RelativeLayout mSignCoupon;
    @FmyViewView(R.id.home_coupon_yuan)
    private TextView mYuan;
    @FmyViewView(R.id.home_coupon_up)
    private TextView mUp;
    @FmyViewView(R.id.home_coupon_min)
    private TextView mMin;
    @FmyViewView(R.id.home_coupon_time)
    private TextView mTime;
    @FmyViewView(R.id.fra_nonetwork)
    private RelativeLayout mNonetwork;
    private HttpRequestDao httpRequestDao;
    private Map<String, String > header;
    private boolean isZjzHome=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_thr);

        initView();

        return view;
    }


    private void initView() {
        if (AppContext.isNetworkAvailable(mActivity)){
            //网络可用
            initWebView(mWebView);
            httpRequestDao = new HttpRequestDao();
            header = new HashMap<>() ;
            header.put("token", AppContext.getInstance().token);
            LogUtils.e("header-->"+header.toString());
            mWebView.loadUrl(Urls.ZSH,header);
        }else {
            mNonetwork.setVisibility(View.VISIBLE);
        }


    }


    private void initWebView(WebView webView){
        WebViewUtils.initwebView(gContext,webView);

        webView.setWebViewClient(new MyWebViewClient());


    }

    @FmyClickView({R.id.home_share_b,R.id.home_share_r})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_share_b://邀请好友
                if(AppContext.getInstance().isLogin()){
                    ShareUtils.shareDialog(mActivity, res, String.format(WXshareUtils.addFriendTitle,AppContext.getUserName(mActivity)),
                            WXshareUtils.addFriendDescription,String.format(WXshareUtils.addFriendUrl,AppContext.getUserId(mActivity)),null);
                }else {
                    ToastUtils.showToast(mActivity,res.getString(R.string.login_notExistError),1);
                }
                break;
            case R.id.home_share_r:
                mShareR.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("shouldOverrideUrlLoading-->"+url);
            toDo(view,url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.e("onPageFinished-->"+url);
            isZjzHome = url.contains(Urls.ZSH);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.e("onPageStarted-->"+url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            LogUtils.e("onLoadResource-->"+url);
            super.onLoadResource(view, url);
        }
    }

    //各种跳转这里处理
    public void toDo(WebView view, String url){
        switch (UrlSchemeManager.urlAnalysisAction(UrlSchemeManager.FRAGMENT,url)){
            case UrlSchemeManager.NONE:
                view.loadUrl(url,header);
                break;
            case UrlSchemeManager.DOANDDONOTTH:

                break;
            case UrlSchemeManager.DOANDFINISH:
                mWebView.goBack();
                break;

            case UrlSchemeManager.FRG_TOHOME:
                if (mainActivityCallBack != null) {
                    mainActivityCallBack.switchPage(0);
                }
                break;
            case UrlSchemeManager.FRG_SHARE://邀请好友
                if(AppContext.getInstance().isLogin()){
                    ShareUtils.shareDialog(mActivity, res, String.format(WXshareUtils.addFriendTitle,AppContext.getUserName(mActivity)),
                            WXshareUtils.addFriendDescription,String.format(WXshareUtils.addFriendUrl,AppContext.getUserId(mActivity)),null);
                }else {
                    ToastUtils.showToast(mActivity,res.getString(R.string.login_notExistError),1);
                }
                break;
            case UrlSchemeManager.FRG_SIGN:

                mSignCoupon.setVisibility(View.GONE);
                mSignText.setText("");
                httpRequestDao.getSignDetail(mActivity, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {

                        SignBean signBean = new Gson().fromJson(result,SignBean.class);
                        if (signBean.getFlag()==0){
                            mSignText.setText("您今天已经签到");
                            httpRequestDao.getiSSignCoupon(gContext, new OkGoStringCallback() {
                                @Override
                                public void onSuccessEvent(String result) {
                                    SignBean signBean = new Gson().fromJson(result,SignBean.class);
                                    if (signBean.getFlag()==0){
                                        //已经领过券
                                        Glide.with(mActivity).load(R.drawable.sign_done).centerCrop().into(mHomeSignPic);
                                    }else {
                                        getCoupon();
                                    }
                                }
                            });
                        }else {
                            mSignText.setText("签到成功，+5积分");
                            Glide.with(mActivity).load(R.drawable.sign_gift).centerCrop().into(mHomeSignPic);
                            getCoupon();
                        }
                        mShareR.setVisibility(View.VISIBLE);  //每次点击都出来
                    }
                });
                break;
        }

    }
    private void getCoupon(){
        //点击领券
        mHomeSignPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpRequestDao.getSignCoupon(mActivity, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        mSignText.setText("您今天已经签到");
                        SignCouponBean signCouponBean = new Gson().fromJson(result,SignCouponBean.class);
                        if (signCouponBean.getFlag()==1){
                            Glide.with(mActivity).load(R.drawable.sign_coupon).centerCrop().into(mHomeSignPic);

                            mYuan.setText("￥"+ signCouponBean.getAmount());
                            mUp.setText(signCouponBean.getAmount() + "元代金券");
                            mMin.setText(signCouponBean.getRule());
                            mTime.setText("有效期至:"+signCouponBean.getEndTime());

                            mSignCoupon.setVisibility(View.VISIBLE);
                            mHomeSignPic.setEnabled(false);
                        }
                    }
                });
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isZjzHome&& (keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        if (mShareR!=null){
            int status = mShareR.getVisibility();
            switch (status){
                case View.VISIBLE:
                    mShareR.setVisibility(View.GONE);
                    return true;
                case View.GONE:
                    return false;
                case View.INVISIBLE:
                    return false;
                default:
                    break;
            }
        }
        return false;
    }
}
