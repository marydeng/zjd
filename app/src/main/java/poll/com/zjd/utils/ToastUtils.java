package poll.com.zjd.utils;

import android.content.Context;
import android.widget.Toast;

import poll.com.zjd.app.AppContext;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  11:26
 * 包名:     poll.com.zjd.utils
 * 项目名:   zjd
 */

public class ToastUtils {
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s, int time) {
        if (toast == null) {
            toast = Toast.makeText(context, s, time);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > time) {
                    toast.setDuration(time);
                    toast.show();
                }
            } else {
                toast.setDuration(time);
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
    public static void showToast(Context context,String s){
        showToast(context,s,Toast.LENGTH_LONG);
    }

    public static void showToast(Context context,int resId){
        showToast(context,resId,Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, int resId, int time) {
        showToast(context, context.getString(resId), time);
    }

    public static void showToast(int resId) {
        showToast(AppContext.getInstance().getApplicationContext(), AppContext.getInstance().getApplicationContext().getString(resId), Toast.LENGTH_LONG);
    }

    public static void showToast(String msg) {
        showToast(AppContext.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG);
    }
}
