package poll.com.zjd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import poll.com.zjd.app.AppContext;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:03
 * 包名:     poll.com.zjd.utils
 * 项目名:   zjd
 */

public class SPUtils {
    //存放用户信息的缓存文件
    public static String CACHE_USER_INFO = "zjz-user";
    //存放搜索历史
    public static String CACHE_SEARCH_TEXT = "zjz-history_text";

    public static String WEIXIN_LOGIN_MODEL = "weixin_login_model";//微信登录后的信息Model


    //存放AccessToken的缓存文件
    public static String CACHE_ACCESSTOKEN_INFO = "zjz-accesstoken";
    //SP中CACHE_ACCESSTOKEN_INFO缓存中存放的key
    public final static String IS_FIRST_USE = "isFirstUse";
    public static final String ACCESS_TOKEN_V2 = "access_token_v2";
    public static final String USER_PHONE = "user_phone";
    public static final String ACCESS_TOKEN_MODEL = "accesstokenModel";
    public static final String OAUTH2_TOKEN_MODEL = "oauth2TokenModel";
    public static final String BAIDU_LOCATION__MODEL = "baidu_location__model";//百度定位返回的Model缓存(LocationModel)
    public static final String BAIDU_LOCATION__MODEL_TIME = "baidu_location__model_time";//百度定位返回的Model最后一次定位时间(long)
    public static final String LOCATION = "location";//定位的城市
    public static final String SELECTED_CITY = "selected_city"; // 用户选择的城市
    public static final String USER_AGENT_INFO = "user_agent_info";
    public static final String BANNER_MODEL = "banner_model";//行动界面Banner的model缓存
    public static final String APP_UPDATE_STATE = "app_update_state";//APP当前更新得状态
    public static final String APP_ISFORCEDUPDATE = "app_isforcedupdate";//APP当前更新得状态 - 是否需要强制更新
    public static final String SHOW_FOOTMARK_COVER = "show_footmark_cover"; // 是否该显示足迹图引导页
    //输错密码
    public static final String PWD_ERROR_TIMES = "pwd_error_times"; // 密码输入错误次数
    public static final String PWD_ERROR_TIMESTAMP = "pwd_error_timestamp"; // 上次密码输入错误时间
    //启动广告缓存
    public static final String HOME_START_ADS_TIME_LONG = "home_start_ads_time_long";
    //oauth过期时间
    public static final String OAUTH_LASTSAVETIME = "oauth_lastsavetime";


    //存放购物车商品信息
    public static final String SHOPP_CART_GOODS_DATA="shopp_cart_goods_data";

    public final String FILE_NAME = "share_data";   //保存在手机里面的文件名
    public final String SHAREDDIR = "shared_prefs";   //SharedPreferences 文件夹默认名称
    public SharedPreferences sp = null;
    public String newfilename = null;

    public SPUtils() {
        /* cannot be instantiated */
        //throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 设置非默认xml文件名
     *
     * @param context
     * @param filename
     */
    public SPUtils(Context context, String filename) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        newfilename = filename;
    }

    public void setSharedFileName(Context context, String filename) {
        sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        newfilename = filename;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public void put(Context context, String key, Object object) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(Context context, String key, Object defaultObject) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return defaultObject;
    }

    /**
     * 获取key的Value,默认为""
     */
    public String getString(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, "");
    }

    public long getLong(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getLong(key, 0);
    }

    public int getInt(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, 0);
    }

    public boolean getBoolean(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, false);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public void clear(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public boolean contains(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return sp.getAll();
    }

    /**
     * 获取SharedPreferences大小
     */
    public long getSharedSize(Context context) {
        File file = getSharedFile(context);
        try {
            long filesize;
            if (file == null) {
                filesize = 0L;
            }
            filesize = file.length();
            return filesize;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取sharedpreference文件
     */
    public File getSharedFile(Context context) {
        File file = context.getCacheDir();
        String filename = FILE_NAME;
        if (newfilename != null) {
            filename = newfilename;
        }
        String path = file.getParent() + File.separator + file.getName().replace("cache", this.SHAREDDIR) + File.separator + filename + ".xml";
        file = new File(path);
        return file;
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {

        private static final Method spEditorApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (spEditorApplyMethod == null) {
                    editor.commit();
                } else {
                    spEditorApplyMethod.invoke(editor);
                }
            } catch (Exception e) {
                editor.commit();
            }
        }
    }
}
