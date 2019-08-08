package poll.com.zjd.utils;


import poll.com.zjd.app.AppContext;

public class UiUtil {
    /**
     * @param runnable
     * @description 把Runnable 方法提交到主线程运行
     */
//    public static void runOnUiThread(Runnable runnable) {
//        // 在主线程运行
//        if (Looper.getMainLooper().isCurrentThread()) {
//            runnable.run();
//        } else {
//            //获取handler
//            AppContext.getInstance().getMainHandler().post(runnable);
//        }
//    }

    public static void runOnUiThreadDelayed(Runnable runnable, long delayTimeMillion) {
        AppContext.getMainHandler().postDelayed(runnable, delayTimeMillion);
    }
}
