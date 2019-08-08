package poll.com.zjd.api;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  13:56
 * 包名:     poll.com.zjd.api
 * 项目名:   zjd
 */

import poll.com.zjd.app.AppConfig;

/**
 * %s 字符串
 * %d 整型数
 * %f 浮点数
 * 例子:
 * http://op.dafan.info/Service/SumbitMsg.aspx?appt=AlbumNews&MGID=45
 * Urls.MUSIC = AppConfig.HOST_API + "?appt=%s&MGID=%d"
 * String url = String.format(Urls.MUSIC,Album,Mgid);
 */
public class Urls {

    /**
     * 用于维护session 的token 字段名
     **/
    public static String TOKEN = "token";


    /***H5页面跳转地址***/
    //真实惠
    public static String ZSH = AppConfig.HOST_URL + "/wine/zhensh.sc";
    //新人专享
    public static String NEWONE = AppConfig.HOST_URL + "/wine/exclusive.sc";

    //邀请有礼
    public static String INVITE = AppConfig.HOST_URL + "/wine/invitation.sc";

    //我的所有订单
    public static String ORDERS = AppConfig.HOST_URL + "/wine/toOrders.sc";
    //订单详情
    public static String ORDER_DETAIL=AppConfig.HOST_URL + "/wine/toOrderDetails.sc?orderNo=%s";
    //我的优惠券列表
    public static String COUPONS = AppConfig.HOST_URL + "/wine/tocoupons.sc";
    //优惠券说明
    public static String COUPONSINSTRUCTION = AppConfig.HOST_URL + "/wine/couponsInstruction.sc";
    //代付款
    public static String WAITINGPAY = AppConfig.HOST_URL + "/wine/toOrders.sc?payMethod=1&payStatus=7&baseStatus=1";
    //已签收
    public static String RECEIVED = AppConfig.HOST_URL + "/wine/toOrders.sc?deliveryStatus=17";
    //代发货
    public static String WSEND = AppConfig.HOST_URL + "/wine/toOrders.sc?deliveryStatus=22";
    //配送中
    public static String SENDING = AppConfig.HOST_URL + "/wine/toOrders.sc?deliveryStatus=16";
    //退款中
    public static String REIMBURSING = AppConfig.HOST_URL + "/wine/toOrders.sc?deliveryStatus=10";
    //已退款
    public static String REFUNDED = AppConfig.HOST_URL + "/wine/toOrders.sc?payStatus=12";
    //发票入口
    public static String INVOICE = AppConfig.HOST_URL + "/wine/toApplyInvoice.sc";
    //客服与帮助
    public static String HELP = AppConfig.HOST_URL + "/wine/helpCenter.sc";
    //选择地址
    public static String CHOOSEADDRESS = AppConfig.HOST_URL + "/wine/toInvoiceApplyInfo.sc?userName=%s&phone=%s&province=%s&city=%s&county=%s&address=%s";
    /****H5分享****/
    //邀请注册
    public static String H5_LOGIN = AppConfig.HOST_URL + "/wine/register.sc?from=groupmessage&userId=%s";
    //商品详情的分享
    public static String SHAREPRODUCT = AppConfig.HOST_URL + "/wine/commodity.sc?commodityId=%s";

    /***首页接口***/

    //获取已开通的城市列表
    public static String CITY = AppConfig.HOST_API + "/store/list.sc";
    //首页所有的东西
    public static String HOME = AppConfig.HOST_API + "/store/index.sc";
    //首页所有的东西 参数转码
    public static String HOME_PARAM_ENCODE = AppConfig.HOST_API + "/store/index.sc?city=%s";
    //选择收货地址
    public static String SELECTADDRESS = AppConfig.HOST_API + "/user/address/list.sc";
    //签到
    public static String SIGN = AppConfig.HOST_API + "/sign/signIn.sc";
    //签到
    public static String ISGETCOUPON = AppConfig.HOST_API + "/sign/isRecieveCoupon.sc";
    //判断是否领过券
    public static String ISSIGNINCOUPON = AppConfig.HOST_API + "/sign/isRecieveCoupon.sc";
    //点击领券
    public static String SIGNINCOUPON = AppConfig.HOST_API + "/sign/signInCoupon.sc";
    //秒杀顶部图片请求接口
    public static String KILLHEADERPIC = AppConfig.HOST_API + "/store/getTags.sc?struc=root-23";

    /***分类接口***/

    //分类
    public static String CLASSLIST = AppConfig.HOST_API + "/goods/getCatb2c.sc";
    //价格列表
    public static String PRICELIST = AppConfig.HOST_API + "/goods/getPriceScopes.sc";
    //品牌所有
    public static String BRANDLIST = AppConfig.HOST_API + "/goods/getBrandList.sc";
    //品牌列表
    public static String BRANDLISTWITHCLASS = AppConfig.HOST_API + "/goods/getBrandList.sc?structName=%s&catId=%s";
    //一键选酒
    public static String FIRSTCLASS = AppConfig.HOST_API + "/goods/getTags.sc";
    //商品列表
    public static String PRODUCTLIST = AppConfig.HOST_API +
            "/goods/categorySearch.sc?param2=%s&param3=%s&param4=%s&param6=%s&param8=%s&param9=%s&param12=%s&param13=%s&param14=%s&param15=%s&param16=%s&param17=%s";

    //搜索商品列表
    public static String SEARCHPRODUCTLIST = AppConfig.HOST_API +
            "/goods/categorySearch.sc";
   //秒杀列表
    public static String GETCESSORLIST= AppConfig.HOST_API +
           "/goods/cessors.sc";
    //秒杀海报
    public static String GETCESSORPOSTER= AppConfig.HOST_API +
            "/store/getTags.sc";

    /**
     * 购物车接口
     **/
    //微信支付
    public static String WXPAY = AppConfig.HOST_API + "/payment/wxOrder.sc";
    //支付宝支付
    public static String ALIPAY = AppConfig.HOST_API + "/payment/alipayOrder.sc";


    /**
     * 我的接口
     **/

    // 登录
    public static String LOGIN = AppConfig.HOST_API + "/user/login.sc";
    // 登录获取验证码
    public static String SENDCODE = AppConfig.HOST_API + "/user/sendCode.sc";
    // 支付密码获取验证码
    public static String SENDVALIDATECODE = AppConfig.HOST_API + "/user/sendValidateCode.sc";
    //该手机是否注册过
    public static String CHECKPHONE = AppConfig.HOST_API + "/user/validatePhone.sc?phone=%s";

    //第一次设置支付密码
    public static String SAVEPASSWROD = AppConfig.HOST_API + "/user/savePWD.sc";
    //修改支付密码
    public static String UPDATEPASSWORD = AppConfig.HOST_API + "/user/updatePWD.sc";
    //重置支付密码
    public static String RESETPASSWORD = AppConfig.HOST_API + "/user/resetPWD.sc";
    //获取收货地址列表
    public static String GETADDRESSLIST = AppConfig.HOST_API + "/user/address/list.sc";
    //删除收货地址
    public static String DELETEADDRESS = AppConfig.HOST_API + "/user/address/del.sc";
    //设置默认地址
    public static String SETDEFAULTADDRESS = AppConfig.HOST_API + "/user/address/setDefault.sc";
    //新增地址
    public static String AddAddress = AppConfig.HOST_API + "/user/address/add.sc";
    //更新地址
    public static String UPDATEADDRESS = AppConfig.HOST_API + "/user/address/update.sc";

    //获取交易流水
    public static String GETBALANCEHISTORY = AppConfig.HOST_API + "/member/vbank/getVbanktransation.sc";

    //查询用户信息
    public static String QUERYUSERINFO = AppConfig.HOST_API + "/user/queryUserInfo.sc";
    //修改用户信息
    public static String MODIFYUSERINFO = AppConfig.HOST_API + "/user/updateBasicInfo.sc";
    //更换手机号
    public static String CHANGEPHONENUMBER = AppConfig.HOST_API + "/user/changePhone.sc";
    //更新头像
    public static String MODIFYHEADIMAGE=AppConfig.HOST_API + "/user/uploadImage.sc";

    /**
     * 储值卡充值相关
     */
    //获取充值方案
    public static String GETRECHARGELIST = AppConfig.HOST_API + "/member/vbank/getRechargeList.sc";

    //充值生成支付信息
    public static String GETRECHARGEPAYINFO = AppConfig.HOST_API + "/member/vbank/toRecharge.sc";

    /**
     * 商品详情接口
     **/
    public static String GETGOODDETAIL = AppConfig.HOST_API + "/commodity/goods.sc";

    /**
     * 下订单相关接口
     **/
    //提交订单前的检查
    public static String ORDERCHECK = AppConfig.HOST_API + "/order/orderCheck.sc";
    //提交订单
    public static String CREATEORDER = AppConfig.HOST_API + "/order/createOrder.sc";
    //生成支付信息
    public static String CREATEPAYINFO = AppConfig.HOST_API + "/order/prePay.sc";
    //商品信息更新接口
    public static String UPDATEPRODUCTINFO = AppConfig.HOST_API + "/order/getProductInfo.sc";

    //查询优惠券
    public static String QUERYCOUPONS = AppConfig.HOST_API + "/coupon/getUserCoupons.sc";

    //获取可配送城市编码文件
    public static String GETCITYLIST = AppConfig.HOST_URL + "/upload/json/city.json";

    //获取运营点id
    public static String GETSUBCOMPANYID=AppConfig.HOST_API + "/store/confirmCompanyAndDelivery.sc";


    /**
     * 支付相关接口
     **/
    //获取虚拟账户信息
    public static String QUERY_VBANK = AppConfig.HOST_API + "/member/vbank/getVbank.sc";

    //账户余额支付
    public static String VBANK_PAY = AppConfig.HOST_API + "/order/pay/vbankPay.sc";

    //校验支付密码
    public static String CHECK_PW = AppConfig.HOST_API + "/user/checkPWD.sc";

    //验证手机号
    public static String VERY_PHONE_NUMBER = AppConfig.HOST_API + "/payment/checkPayCode.sc";

    //注册推送
    public static String PUSH_REGISTER=AppConfig.HOST_API + "/push/register.sc";
    //注册订阅
    public static String PUSH_SUBSCRIBER=AppConfig.HOST_API + "/push/subscribe.sc";

}
