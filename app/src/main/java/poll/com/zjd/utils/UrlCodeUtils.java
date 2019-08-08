package poll.com.zjd.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Tommy on 2017/5/16.
 */

public class UrlCodeUtils {
    /**
     * 编码
     * @param paramString
     * @return
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            LogUtils.e(paramString);
            return "";
        }

        try{
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        }
        catch (Exception localException){
            LogUtils.e(localException);
        }

        return "";
    }

    /**
     * 解码
     * @param paramString
     * @return
     */
    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            LogUtils.e(paramString);
            return "";
        }

        try{
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        }
        catch (Exception localException){
            LogUtils.e(localException);
        }

        return "";
    }
}
