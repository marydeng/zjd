package poll.com.zjd.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;

import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.activity.CommitOrderActivity;
import poll.com.zjd.activity.GoodsDetailActivity;
import poll.com.zjd.activity.MyBalanceActivity;
import poll.com.zjd.activity.OrderChoosePayWayActivity;
import poll.com.zjd.activity.SearchActivity;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.CreatePayInfoResponse;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.ShoppingCartUtil;

/**
 * 内部外部跳转APP处理
 * Created by Tommy on 2017/7/24.
 */
/**
 * zjd://zj.com?url_action={"from":"webview","url":"","appUrl":5,"data":}
 * zjd://zj.com?url_action={"from":"webview","url":"","appUrl":1,"data":297ead195df9a24e015df9b199f10020}
 * # 内部跳转链接格式v1.0
 * <p>
 * zjd://zj.com?url_action= {
 * "from":"webview",
 * "url":"",
 * "appUrl":"5",
 * "data":""
 * }
 */
public class UrlSchemeManager {

    public static final int NONE = 0;          //无作为
    public static final int DOANDFINISH = 1;   //干了并且要关闭掉页面
    public static final int DOANDDONOTTH = 2;  //干了,但不关闭页面

    public static final int FRG_SHARE = 7;     //分享
    //返回首页
    public static final int FRG_TOHOME = 8;    //去首页
    //返回购物车
    public static final int FRG_TOSHOP = 9;    //去购物车
    //返回购物车
    public static final int FRG_SIGN = 10;     //签到

    public static final int ACT_TOSHOP = 11;   //act购物车
    public static final int ACT_MY = 12;       //act我的

    public static final int ACTIVITY = 0;      //activity
    public static final int FRAGMENT = 1;      //fragment



    public static final String URL_HEADER = "zjd://zj.com?url_action=";
    private static final int HOME = 5;                                 //今日首页
    private static final int BACK = 0;                                 //返回
    private static final int PRODUCT_DETAIL = 1;                       //商品详情
    private static final int SEARCH_PRODUCT = 2;                       //商品搜索
    private static final int STORED_VALUE = 3;                         //储值有礼
    private static final int HOME_SIGN1 =4;                            //首页签到
    private static final int TO_USE_COUPON =6;                         //优惠券去使用
    private static final int TO_PAY =7;                                //去支付
    private static final int HOME_SIGN =8;                             //首页签到
    private static final int INVITE =9;                                //邀请有礼(立即邀请)
    private static final int MY =11;                                   //我的

    private static final String HOME_ACTION_FRAGMENT_DATA = "HOME_ACTION_FRAGMENT_DATA";

    /**
     *
     * 解析URL方法,进行跳转
     * 外部内部跳转APP统一判断
     *
     * @param from  0 webActivity 1 webFragment
     * @param url
     * @return 0 无操作 1 操作了需要结束掉webView 2操作了,不要结束掉webView
     */
    public static int urlAnalysisAction(int from,String url) {

        String action = "";

        String trim = url.trim();
        if (!trim.startsWith(URL_HEADER)) {
            return NONE;
        }

        String actionData = trim.substring(URL_HEADER.length(), trim.length());

        int i=actionData.lastIndexOf(":");

        if(actionData.substring(i,actionData.length()).length()==2){
            StringBuilder sb=new StringBuilder(actionData);
            actionData = sb.insert(i+1,"\"\"").toString();
        }

        if (TextUtils.isEmpty(actionData)) {
            return NONE;
        } else {
            UrlActionModel urlActionModel;
            try {
                urlActionModel = new Gson().fromJson(actionData, UrlActionModel.class);
            } catch (Throwable throwable) {
                LogUtils.e("解析异常");
                throwable.printStackTrace();
                return NONE;
            }

            if (urlActionModel == null) {
                return NONE;
            }

            final AppContext instance = AppContext.getInstance();
            final BaseActivity baseActivity = ActivityManager.getAppManager().currentActivity();
            Bundle bundle = new Bundle();
            switch (urlActionModel.getAppUrl()) {
                case BACK:           //返回
                    return DOANDFINISH;
                case HOME:           //去首页
                    if (from==ACTIVITY){
                        return FRG_TOHOME;
                    }else if(from==FRAGMENT){
                        return FRG_TOHOME;
                    }
                    break;
                case SEARCH_PRODUCT: //商品搜索
                    bundle.putString(SearchActivity.SEARCHDATA, urlActionModel.getData());
                    action = SearchActivity.class.getName();
                    break;
                case STORED_VALUE:   //我的余额
                    bundle.putString(MyBalanceActivity.EXTRA_MY_BALANCE_VALUE, urlActionModel.getData());
                    action = MyBalanceActivity.class.getName();
                    break;
                case HOME_SIGN:      //首页的签到
                    return FRG_SIGN;
                case PRODUCT_DETAIL: //商品详情
                    bundle.putString(GoodsDetailActivity.GOODS_ID_EXTRA,urlActionModel.getData());
                    action = GoodsDetailActivity.class.getName();
                    break;
                case TO_USE_COUPON:  //优惠券去使用(去购物车)
                    if (from==ACTIVITY){
                        return ACT_TOSHOP;
                    }else if(from==FRAGMENT){
                        return FRG_TOSHOP;
                    }
                    break;
                case MY://我的
                    return  ACT_MY;
                case TO_PAY:         //我的订单(去支付)
                    String orderMainNo = urlActionModel.getData();
                    if (orderMainNo.contains("_")){
                        int j =orderMainNo.lastIndexOf("_");
                        orderMainNo = orderMainNo.substring(0,j);
                    }
                    HashMap<String, String> map = new HashMap<>();
                    map.put("orderMainNo", orderMainNo);

                    JSONObject jsonObject = JsonUtils.convertJSONObject(map);
                    DialogUtil.showProgressDialog(baseActivity, null, null);
                    new HttpRequestDao().createPayInfo(baseActivity, jsonObject, new OkGoStringCallback() {
                        @Override
                        public void onSuccessEvent(String result) {
                            DialogUtil.hideProgressDialog();
                            Gson gson = new Gson();
                            CreatePayInfoResponse createPayInfoResponse = gson.fromJson(result, CreatePayInfoResponse.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(OrderChoosePayWayActivity.EXTRA_ORDER_ID, createPayInfoResponse.getId());
                            bundle.putDouble(OrderChoosePayWayActivity.EXTRA_ORDER_MONEY, createPayInfoResponse.getOrderPayTotalAmont());
                            instance.startActivity(baseActivity, OrderChoosePayWayActivity.class, bundle);
                            ShoppingCartUtil.getInstance().clearAllGoods();
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            DialogUtil.hideProgressDialog();
                        }
                    });

                    return DOANDDONOTTH;
                case INVITE:         //立即邀请(分享)
                    return FRG_SHARE;
                case FRG_SIGN:
                    return FRG_SIGN;
            }
            if (TextUtils.isEmpty(action)) return NONE;
            instance.startActivity(baseActivity, action, bundle);
            return DOANDDONOTTH;
        }
    }

    @SuppressLint("ParcelCreator")
    public static class UrlActionModel{
        private String from;
        private String url;
        private int appUrl;
        private String data;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(int appUrl) {
            this.appUrl = appUrl;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
