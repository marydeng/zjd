package poll.com.zjd.utils;

import android.app.Activity;
import android.content.Context;

import poll.com.zjd.R;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  10:42
 * 包名:     poll.com.zjd.utils
 * 项目名:   zjd
 */

public class ActivityEffectUtils {
    public static final int ENTERRIGHTTOLEFT = 0;
    public static final int OUTLEFTTORIGHT = 1;
    public static final int ENTERBOTTOMTOTOP = 2;
    public static final int OUTTOPTOBOTTOM = 3;
    public static final int FADE_IN_FADE_OUT = 4;

    /**
     * 设置activity效果
     *
     * @param context，如为null，则默认appmanager.currentActivity();
     * @param effectId
     */
    public static void setActivityEffect(Context context, int effectId) {
        if (context == null) {
//            context = AppManager.getAppManager().currentActivity();
//            if (context == null) {
//                return;
//            }
            return;
        }
        switch (effectId) {
            case ActivityEffectUtils.ENTERRIGHTTOLEFT:
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case ActivityEffectUtils.OUTLEFTTORIGHT:
                ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case ActivityEffectUtils.ENTERBOTTOMTOTOP:
                ((Activity) context).overridePendingTransition(R.anim.push_up_in, 0);
                break;
            case ActivityEffectUtils.OUTTOPTOBOTTOM:
                ((Activity) context).overridePendingTransition(0, R.anim.push_down_out);
                break;
            case ActivityEffectUtils.FADE_IN_FADE_OUT:
                ((Activity) context).overridePendingTransition(R.anim.activity_fade_in_anim, R.anim.activity_fade_out_anim);
                break;
        }

    }

}
