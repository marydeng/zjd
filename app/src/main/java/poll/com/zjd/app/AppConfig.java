package poll.com.zjd.app;

import poll.com.zjd.utils.StringUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  13:56
 * 包名:     poll.com.zjd.app
 * 项目名:   zjd
 */

public class AppConfig {

    public static final String APP_PACKAGE_NAME = "poll.com.zjd";

//    public static String HOST_URL = "http://120.24.157.236:8080";   //域名
//    public static String HOST_URL = "http://192.168.43.19:8080";   //域名
//    public static String HOST_URL = "http://www.vanhmall.com:8090";   //域名
    public static String HOST_URL = "http://47.104.26.248:80";   //域名
    public static String HOST_API = HOST_URL + "/api";
    public static float DIALOG_DIM = 0.7f;



    /**
     * 获取完整的带域名的Url
     */
    public static String getAppRequestUrl(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) return "";
        return imageUrl.contains("http") ? imageUrl : HOST_URL + imageUrl;
    }

    public interface AccessTokenSPKey{
        String AccessToken="poll.com.zjd.app.name";
        String PhoneNumber="poll.com.zjd.app.phone";
        String LoginId="poll.com.zjd.app.login.id";
    }

    public interface ResultCode{
        //请求成功
        int success=0;
        //请求失败
        int fail=1;
        //未登录或者token失效
        int unAuthorize=2;
    }

}
