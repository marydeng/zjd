package poll.com.zjd.dao;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.api.HttpRequestApi;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.UrlCodeUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/12  20:30
 * 包名:     poll.com.zjd.dao
 * 项目名:   zjd
 */


public class HttpRequestDao {

    /**
     * 首页模块
     **/
    //获取已开通的城市列表
    public void getCityList(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.CITY, null, callback);
    }

    //获取启动广告图片(因为广告数据在首页的接口上,所以等同于请求首页)
    public void getStartAd(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.HOME, null, callback);
    }

    //获取首页所有数据
    public void getHomeAllDate(Context context,JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.HOME, jsonObject, callback);
    }
    //获取首页所有数据
    public void getHomeAllDate(Context context,String city, OkGoStringCallback callback) {
        String url = String.format(Urls.HOME_PARAM_ENCODE,UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(city)));
        HttpRequestApi.get(context, url, null, callback);
    }

    //获取用户收货地址
    public void getUserAddress(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SELECTADDRESS, null, callback);
    }

    //签到
    public void getSignDetail(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SIGN, null, callback);
    }
    //领取签到的券
    public void getSignCoupon(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SIGNINCOUPON, null, callback);
    }
    //领取签到的券
    public void getiSSignCoupon(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.ISSIGNINCOUPON, null, callback);
    }
    //秒杀头部图片
    public void getKillHeaderPic(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.KILLHEADERPIC, null, callback);
    }

    /**
     * 购物车模块
     **/
    //根据支付订单号,获取预付单数据(微信)
    public void getWXPayDetail(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.WXPAY, jsonObject, callback);
    }

    //根据支付订单号,获取预付单数据(支付宝)
    public void getALIPayDetail(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.ALIPAY, jsonObject, callback);
    }


    /**
     * 分类模块
     **/
    //获取所有分类
    public void getClass(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.CLASSLIST, null, callback);
    }
    //获取价格列表
    public void getPriceList(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.PRICELIST, null, callback);
    }
    //获取品牌
    public void getBrand(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.BRANDLIST, null, callback);
    }
    //获取品牌按照分类筛选
    public void getBrandWithClass(Context context, String structName, String catId, OkGoStringCallback callback) {
        String url = String.format(Urls.BRANDLISTWITHCLASS,structName,catId);
        HttpRequestApi.get(context, url, null, callback);
    }
    //一键选酒
    public void getFirstClass(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.FIRSTCLASS, null, callback);
    }
    //获取商品列表
    public void getProductList(Context context, List<String> params, OkGoStringCallback callback) {
        List<String> ecoList = new ArrayList<>();
        for (String s : params) {
            ecoList.add(UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(s)));
        }
        MyLocationManagerUtil myLocationManagerUtil = MyLocationManagerUtil.getInstance();
        String url = String.format(Urls.PRODUCTLIST, ecoList.get(0), ecoList.get(1), ecoList.get(2),
                ecoList.get(3), ecoList.get(4), ecoList.get(5), myLocationManagerUtil.getSubCompanyId(),
                myLocationManagerUtil.getProvinceNO(),myLocationManagerUtil.getCityNo(),myLocationManagerUtil.getStreetNo(),myLocationManagerUtil.getDistrictNo(),ecoList.get(6));

        HttpRequestApi.get(context, url, null, callback);
    }

    //搜索商品列表
    public void searchProductList(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SEARCHPRODUCTLIST, jsonObject, callback);
    }

    /**
     * 我的模块
     **/
    //登录
    public void login(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.LOGIN, jsonObject, callback);
    }

    public void sendCode(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SENDCODE, jsonObject, callback);
    }

    //支付密码发短信验证
    public void sendValidateCode(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SENDVALIDATECODE, jsonObject, callback);
    }

    public void checkPhone(Context context, String phone, OkGoStringCallback callback) {
        String url = String.format(Urls.CHECKPHONE,phone);
        HttpRequestApi.get(context, url, null, callback);
    }

    public void savePassword(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SAVEPASSWROD, jsonObject, callback);
    }

    public void updatePassword(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.UPDATEPASSWORD, jsonObject, callback);
    }
    public void resetPassword(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.RESETPASSWORD, jsonObject, callback);
    }

    public void getAddressList(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETADDRESSLIST, jsonObject, callback);
    }

    public void deleteAddress(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.DELETEADDRESS, jsonObject, callback);
    }

    public void setDefaultAddress(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.SETDEFAULTADDRESS, jsonObject, callback);
    }

    public void addAddress(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.post(context, Urls.AddAddress, jsonObject, callback);
    }

    public void updateAddress(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.post(context, Urls.UPDATEADDRESS, jsonObject, callback);
    }

    public void getBalanceHistory(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETBALANCEHISTORY, jsonObject, callback);
    }

    public void queryUserInfo(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.QUERYUSERINFO, jsonObject, callback);
    }

    public void modifyUserInfo(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.post(context, Urls.MODIFYUSERINFO, jsonObject, callback);
    }

    public void changePhoneNumber(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.CHANGEPHONENUMBER, jsonObject, callback);
    }


    /**
     * 商品详情接口
     **/
    public void getGoodDetail(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETGOODDETAIL, jsonObject, callback);
    }


    /**
     * 订单下相关接口
     **/
    //提交订单前的检查
    public void orderCheck(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.post(context, Urls.ORDERCHECK, jsonObject, callback);
    }

    //提交订单
    public void createOrder(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.post(context, Urls.CREATEORDER, jsonObject, callback);
    }

    public void updateProdcutInfo(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.post(context, Urls.UPDATEPRODUCTINFO, jsonObject, callback);
    }

    public void createPayInfo(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.CREATEPAYINFO, jsonObject, callback);
    }

    public void queryCoupons(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.QUERYCOUPONS, jsonObject, callback);
    }

    //获取可配送城市编码文件
    public void getCityListFile(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETCITYLIST, null, callback);
    }

    public void queryVBankInfo(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.QUERY_VBANK, null, callback);
    }

    public void payByVBank(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.VBANK_PAY, jsonObject, callback);
    }

    public void checkPassword(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.CHECK_PW, jsonObject, callback);
    }

    public void getRechargeList(Context context, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETRECHARGELIST, null, callback);
    }

    public void createRechargePayInfo(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETRECHARGEPAYINFO, jsonObject, callback);
    }

    public void veryPhoneNumber(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.VERY_PHONE_NUMBER, jsonObject, callback);
    }

    public void getCessorList(Context context,JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETCESSORLIST, jsonObject, callback);
    }

    public void getSubcompayId(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETSUBCOMPANYID, jsonObject, callback);
    }

    public void getCessorPoster(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.GETCESSORPOSTER, jsonObject, callback);
    }

    public void registerPushId(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.PUSH_REGISTER, jsonObject, callback);
    }

    public void subscriberPush(Context context, JSONObject jsonObject, OkGoStringCallback callback) {
        HttpRequestApi.get(context, Urls.PUSH_SUBSCRIBER, jsonObject, callback);
    }


}
