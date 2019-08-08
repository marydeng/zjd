package poll.com.zjd.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.text.DecimalFormat;

import poll.com.zjd.utils.LogUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/28
 * 文件描述：应用缓存管理类
 */
public class MyCacheManager {
    private static final String TAG = MyCacheManager.class.getSimpleName();

    private Handler handler;

    private Context context;

    /**
     * 是否正在清理缓存
     */
    private boolean isClearingCache = false;

    public interface MyMessageType {
        int CLEAR_CACHE_SUCCESS = 1;
        int CLEAR_CACHE_FAIL = 2;
    }

    public MyCacheManager(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param versionCode
     * @return
     */
    public static boolean isMethodsCompat(int versionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= versionCode;
    }


    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir(); // e.g. "<sdcard>/Android/data/<package_name>/cache/"
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deleteFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deleteFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deleteFiles++;
                        }
                    }
                }

            } catch (Exception e) {
//                LogUtils.e(com.umeng.analytics.pro.bk.e.toString());
            }
        }
        return deleteFiles;
    }


    //    在项目中经常会使用到WebView 控件,当加载html 页面时,会在/data/data/package_name目录下生成database与cache 两个文件夹。请求的url 记录是保存在WebViewCache.db,而url 的内容是保存在WebViewCache 文件夹下  

    /**
     * 清除app缓存 
     */
    private void clearAppCache() {
        //Todo 15以上的不能使用CacheManager获取web缓存目录了，后面看看该目录是否需要清除
        //清除webview缓存
//        @SuppressWarnings("deprecation")
//        File file=CacheManager.getCacheFileBaseDir();
//
//        //先删除WebViewCache目录下的文件 
//        if(file !=null && file.exists() && file.isDirectory()){
//            for (File item:file.listFiles()){
//                item.delete();
//            }
//            file.delete();
//        }

        //        deleteDatabase("webview.db");    
//        deleteDatabase("webview.db-shm");    
//        deleteDatabase("webview.db-wal");    
//        deleteDatabase("webviewCache.db");    
//        deleteDatabase("webviewCache.db-shm");    
//        deleteDatabase("webviewCache.db-wal");  


        //清除数据缓存
        clearCacheFolder(context.getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());

        //2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(getExternalCacheDir(context), System.currentTimeMillis());
        }
    }

    /**
     * 清除app 缓存
     */
    public void startClearAppCache() {
        if (!isClearingCache) {
            isClearingCache = true;
            new Thread() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    try {
                        clearAppCache();
                        msg.what = MyMessageType.CLEAR_CACHE_SUCCESS;
                        msg.obj = calculateCacheSize();// 清除的缓存大小
                    } catch (Exception e) {
                        LogUtils.e(e.toString());
                        msg.what = MyMessageType.CLEAR_CACHE_FAIL;
                    }
                    handler.sendMessage(msg);
                    isClearingCache = false;
                }
            }.start();
            LogUtils.d("startClearCache");
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件大小  单位/B
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 获取目录文件大小
     *
     * @param dir 目录文件
     * @return 目录文件大小
     */
    public static long getDirSize(File dir) {
        if (null == dir) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    private String calculateCacheSize() {
        String cacheSize = "0KB";
        long fileSize = 0;
        File fileDir = context.getFilesDir();// /data/data/package_name/files 
        File cacheDir = context.getCacheDir(); //  /data/data/package_name/cache 

        fileSize += getDirSize(fileDir);
        fileSize += getDirSize(cacheDir);

        // 2.2版本才有将应用缓存转移到sd卡的功能  
        if (isMethodsCompat(Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = getExternalCacheDir(context);//"<sdcard>/Android/data/<package_name>/cache/"  
            fileSize += getDirSize(externalCacheDir);
        }

        if (fileSize > 0) {
            cacheSize = formatFileSize(fileSize);
        }

        return cacheSize;
    }

}
