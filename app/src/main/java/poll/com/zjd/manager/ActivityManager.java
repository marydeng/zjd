package poll.com.zjd.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  11:19
 * 包名:     poll.com.zjd.manager
 * 项目名:   zjd
 */

public class ActivityManager {
    private static Stack<BaseActivity> activityStack = new Stack<>();
    private static ActivityManager instance = new ActivityManager();

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getAppManager() {
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(BaseActivity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public synchronized BaseActivity currentActivity() {
        if (activityStack.size() > 0) {
            return activityStack.lastElement();
        } else {
            return null;
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public synchronized void finishActivity(Activity activity) {
        if (activity != null) {
            removeActivityOfStack(activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!activity.isDestroyed())
                    activity.finish();
            } else {
                activity.finish();
            }
        }
    }

    /**
     * 结束两个Activity之间的所有Activity,左开右开
     *
     * @param fromActivity
     * @param toActivity
     */
    public synchronized void finishActivityToActivity(Class<? extends Activity> fromActivity, Class<? extends Activity> toActivity) {
        if (activityStack.size() <= 2) {
            return;
        } else {
            int first = ObjectUtils.indexOfFirst(activityStack, fromActivity);
            int last = ObjectUtils.indexOfLast(activityStack, toActivity);
            LogUtils.i("Appmanager_" + first + "---" + last);
            if (last - first <= 1) return;

            List<Activity> removeActivityList = new ArrayList<>();
            for (int i = 0; i < activityStack.size(); i++) {
                if (i > first && i < last) {
                    removeActivityList.add(activityStack.get(i));
                }
            }
            if (ObjectUtils.isEmpty(removeActivityList)) return;
            for (Activity removeActivity : removeActivityList) {
                finishActivity(removeActivity);
            }
        }
    }

    /**
     * 如果栈中有两个当前的Activity,finish掉第一个
     */
    public synchronized void finishOldCurrentActivity() {
        BaseActivity lastActivity = activityStack.lastElement();
        int first = ObjectUtils.indexOfFirst(activityStack, lastActivity.getClass());
        if (first > 0 && first < activityStack.size() - 1) {
            finishActivity(activityStack.get(first));
        }
    }

    public void removeActivityOfStack(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public synchronized void finishActivity(Class<?>... cls) {
        if (activityStack.size() <= 0 || ObjectUtils.isEmpty(cls))
            return;
        List<Activity> removeActivityList = new ArrayList<>();
        for (Class clazz : cls) {
            for (Activity activity : activityStack) {
                Class activityClass = activity.getClass();
                if (activityClass.getSimpleName().equals(clazz.getSimpleName())) {
                    removeActivityList.add(activity);
                }
            }
        }
        if (ObjectUtils.isEmpty(removeActivityList)) return;
        for (Activity removeActivity : removeActivityList) {
            finishActivity(removeActivity);
        }
    }

    /**
     * 结束指定类名(SimpleName)的Activity
     */
    public synchronized void finishActivity(String... activitySimpleName) {
        if (activityStack.size() <= 0 || ObjectUtils.isEmpty(activitySimpleName))
            return;
        List<Activity> activityList = new ArrayList<>();
        for (String name : activitySimpleName) {
            for (Activity activity : activityStack) {
                if (activity.getClass().getSimpleName().equals(name)) {
                    activityList.add(activity);
                }
            }
        }
        if (ObjectUtils.isEmpty(activityList)) return;
        for (Activity activity : activityList) {
            finishActivity(activity);
        }
    }

    /**
     * 清除前面所有的Activity,只保留当前的activity
     */
    public synchronized void finishPreviousActivity(BaseActivity activity) {
        if (activity != null) {
            activityStack.clear();
            activityStack.add(activity);
        }
    }

    /**
     * 清除堆栈中的所有activity
     */
    public void clearStack() {
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public synchronized void finishAllActivity() {
        if (activityStack.size() > 0) {
            while (activityStack.size() > 0) {
                finishActivity(activityStack.remove(0));
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void finishAllActivity(Class<?> exceptActivityClass) {
        if (activityStack.size() <= 0) return;
        Activity findActivity = null;
        List<Activity> finishActivityList = new ArrayList<>();
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (findActivity == null && activity.getClass().equals(exceptActivityClass)) {
                findActivity = activity;
                continue;
            } else {
                finishActivityList.add(activity);
            }
        }
        if (finishActivityList.size() <= 0) return;
        for (int j = 0; j < finishActivityList.size(); j++) {
            finishActivity(finishActivityList.get(j));
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            LogUtils.i("退出异常");
        }
    }

    /**
     * @param activity
     * @return 返回指定activity的索引
     */
    public int getCurrentIndex(Activity activity) {
        return activityStack.search(activity);
    }

    /**
     * @param activity
     * @return 返回指定Activity的最后一个位置
     */
    public int getLastIndex(Activity activity) {
        return activityStack.lastIndexOf(activity);
    }

    /**
     * 移除指定位置的索引
     *
     * @param index
     */
    public void removeActivity(int index) {
        activityStack.get(index).finish();
    }

    /**
     * 移除相同Activity之间的Activity
     *
     * @param activityClass
     */
    public synchronized void finishOldActivity(Class activityClass) {
        int index = 0;
        int activitySum = 0;
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (index != 0) {
                activitySum++;
            }
            if (activity.getClass().equals(activityClass)) {
                if (index == 0) {
                    index = i;
                } else {
                    break;
                }
            }
        }
        if (activitySum > 0) {
            for (int i = index; i <= activitySum; i++) {
                removeActivity(i);
            }
        }
    }

    /**
     * 关闭堆栈中之前的n个Activity
     *
     * @param length
     */
    public void finishActivityForLength(int length) {
        if (length <= activityStack.size()) {
            for (int i = activityStack.size() - 2; i >= activityStack.size() - length - 1; i--) {
                removeActivity(i);
            }
        }
    }

    /**
     * 判断堆栈中是否包含有某个activity
     */
    public synchronized boolean containsActivity(Class activityClass) {
        ListIterator<BaseActivity> activityList = activityStack.listIterator();
        while (activityList.hasNext()) {
            Activity activity = activityList.next();
            if (activity.getClass().getSimpleName().equals(activityClass.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断传入的Activity是否在栈顶
     */
    public synchronized boolean isCurrentOfLaset(Class<?> activityClass) {
        int size = activityStack.size();
        if (size > 0) {
            return activityStack.get(activityStack.size() - 1).getClass().equals(activityClass);
        } else {
            return true;
        }
    }

    /**
     * 获取索引的Activity
     *
     * @param index
     * @return
     */
    public Class getActivity(int index) {
        return activityStack.get(index).getClass();
    }

    /**
     * 从堆栈中获取指定名称的Activity
     *
     * @param activityClass
     * @return
     */
    public synchronized <T> T getActivity(Class<T> activityClass) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(activityClass)) {
                return (T) activityStack.get(i);
            }
        }
        return null;
    }

    /**
     * 获取堆栈的长度
     *
     * @return
     */
    public int getLength() {
        return activityStack.size();
    }

    /**
     * 打印堆栈
     */
    public void printActivityStack() {
        for (Activity activity : activityStack) {
            LogUtils.e("Appmanager_" + activity.getClass().getSimpleName());
        }
    }

    public void refreshData() {
        //刷新页面状态，如已登录，未登录状态
        for (BaseActivity activity : activityStack) {
            activity.refreshData();
        }
    }
}
