package poll.com.zjd.base;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  11:24
 * 包名:     poll.com.zjd.base
 * 项目名:   zjd
 * 各种正则表达式
 */

public class RegexModel {
    public static String REG_TELE_PHONE               = "1[34578]\\d{9}";
    public static String REG_USER_NAME                = "^([\\u4E00-\\u9FA5].*|\\w){2,15}$";
    public static String REG_USER_ALIAS               = ".{2,20}";
    public static String REG_PASSWORD                 = "\\w{6,20}";
    public static String REG_PASSWORD_LENGTH          = ".{6,20}";
    public static String REG_VERIFY                   = "\\d{4}";
    public static String REG_EMAIL                    = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    //public static String USERID = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
    public static String REG_IDCARD                   = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[0-9X]{1}$";
    public static String SQUARE_ACTIVITY_THEME        = ".{4,16}";//活动广场-发起活动主题
    //后台返回的messagede 验证
    public static String HTTP_ERROR_MESSAGE_TOAST     = "[\u4E00-\u9FA5]+[\\w\\p{Punct}]*";
    public static String REG_DONATION_SERIAL          = ".{6,20}";//献血序列号
    //匹配前后引号
    public static String REG_START_END_WITH_QUOTATION = "\".*\"";
    //匹配链接
//    public static String REG_BLOOD_LINK                     = "(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*";
//    public static String REG_BLOOD_LINK                     = "(((http|https|ftp|svn)://)|(www\\.))+(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    public static String REG_BLOOD_LINK               = "(((http|https)://)|(www\\\\.))+17donor.com(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    //    public static String REG_BLOOD_LINK               = "(((http|https)://)|(www\\\\.))+bs.sharing8.cn(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    //匹配话题
    public static String REG_TOPIC                    = "#.+#";

    // 前端校验输入框
    public static boolean checkEditText(EditText editText, String regex) {
        String text = editText.getText().toString().trim();
        return RegexModel.checkText(text, regex);
    }

    // 前端校验输入框
    public static boolean checkText(String value, String regex) {
        if (!StringUtils.isEmpty(value) && value.trim().matches(regex))
            return true;
        return false;
    }

    // 前端校验输入框
    public static boolean checkText(Context mContext, String value, String regex, String emptyTips, String errorTips) {
        if (!StringUtils.isEmpty(value)) {
            if (regex != null || errorTips != null) {
                if (value.trim().matches(regex)) {
                    return true;
                } else {
                    ToastUtils.showToast(mContext, errorTips, Toast.LENGTH_SHORT);
                }
            } else {
                return true;
            }
        } else {
            ToastUtils.showToast(mContext, emptyTips, Toast.LENGTH_SHORT);
        }
        return false;
    }

    // 前端校验输入框
    public static boolean checkNumber(Context mContext, Integer value, String emptyTips) {
        if (value != null && value.intValue() != 0) {
            return true;
        }
        ToastUtils.showToast(mContext, emptyTips, Toast.LENGTH_SHORT);
        return false;
    }

    // 前端校验输入框
    public static boolean checkList(Context mContext, List list, String emptyTips) {
        if (list != null && list.size() > 0) {
            return true;
        }
        ToastUtils.showToast(mContext, emptyTips, Toast.LENGTH_SHORT);
        return false;
    }


    /**
     * 校验是否为空
     *
     * @param mContext
     * @param value
     * @param emptyTips
     * @return
     */
    public static boolean checkText(Context mContext, String value, String emptyTips) {
        return !StringUtils.isEmpty(value);
    }

    /**
     * 校验对象是否为空
     *
     * @param mContext
     * @param value
     * @param emptyTips
     * @return
     */
    public static boolean checkObject(Context mContext, Object value, String emptyTips) {
        return value != null;
    }
}
