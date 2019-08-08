package poll.com.zjd.app;

import poll.com.zjd.utils.SPUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:24
 * 包名:     poll.com.zjd.app
 * 项目名:   zjd
 */

public class AppStates {
    public String previousPageName;//被关闭掉的上一个ActivitySimpleName
    public String Authorization;   //带touken的认证
    public String accessToken;
    public String userPhone;
    public String locationCity;//定位的城市
    public String locationProvince;//获取省份

    public String userAgentInfo;//设备信息
    public String driverIMEI;//手机的IMEI

    public double longitude;//经度
    public double latitude;//纬度

    //积分

    public boolean isFirstUsedAndToLogin = false;//第一次安装APP并且使用了欢迎页的登录注册

    public boolean isForcedUpdate = false; //App 是否 需要强制更新
    public SPUtils spUtils;//有关AccessToken的SP

    public boolean appointmentStatusChanged = false; // 预约回执状态改变



    private static AppStates instance;

    private AppStates() {

    }

    public static AppStates getInstance() {
        if (instance == null) {
            instance = new AppStates();
        }
        return instance;
    }
}
