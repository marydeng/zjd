package poll.com.zjd.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import poll.com.zjd.R;
import poll.com.zjd.activity.GoodsDetailActivity;
import poll.com.zjd.activity.MainActivity;
import poll.com.zjd.activity.WebViewActivity;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.ActivityManager;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MacUtil;
import poll.com.zjd.utils.StringUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/9/23  1:24
 * 包名:     poll.com.zjd.broadcast
 * 项目名:   zjd
 */

public class JpushBroadcastReceiver extends BroadcastReceiver {
    private Context gContext;
    public static final String COMMUNICATION_CIRCLE_MESSAGE = "COMMUNICATION_CIRCLE_MESSAGE";//圈子消息通知


    public interface UrlType {
        String goods_detail = "1";
        String order_detail = "15";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.gContext = context;

        Bundle bundle = intent.getExtras();
        LogUtils.e("[极光推送] [MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtils.e("[极光推送] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
            AppContext.JpushId = regId;
            registerPushId();
        }
        //自定义
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("[极光推送] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogUtils.e("消息" + message);
            LogUtils.e("key_value" + extras);

            processCustomMessage(bundle);

        }
        //默认
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("[极光推送] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.e("[极光推送] 接收到推送下来的通知的ID: " + notifactionId);

        }
        //点击通知
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.e("[极光推送] 用户点击打开了通知");

            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogUtils.e("[极光推送] extra extra=" + jsonString);


        }
        //处理JPushInterface.EXTRA_EXTRA
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtils.e("[极光推送] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtils.e("[极光推送]" + intent.getAction() + " connected state change to " + connected);
        }
        //其他
        else {
            LogUtils.e("[极光推送] Unhandled intent - " + intent.getAction());
        }
    }

    public void processCustomMessage(Bundle bundle) {

        int icon = R.mipmap.ic_launcher;
        String tickertext = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
            Gson gson = new Gson();
            map = gson.fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), new TypeToken<Map<String, String>>() {
            }.getType());
        }

        String title = map.get("title");
        String d = map.get("data");
        String appUrl = map.get("appUrl");


        //设置一个唯一的ID，随便设置  
        int notification_id = 111;

        Bundle data = new Bundle();
        Intent intent = null;
        if (UrlType.goods_detail.equals(appUrl)) {
            data.putString(GoodsDetailActivity.GOODS_ID_EXTRA, d);
            intent = new Intent(gContext, GoodsDetailActivity.class);
            intent.putExtras(data);
        } else if (UrlType.order_detail.equals(appUrl)) {
            String url = String.format(Urls.ORDER_DETAIL, d);
            data.putString(WebViewActivity.LOADURL, url);
            intent = new Intent(gContext, WebViewActivity.class);
        intent.putExtras(data);
    }
        PendingIntent pt = PendingIntent.getActivity(gContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //Notification管理器  
        Notification notification = new Notification.Builder(gContext).setContentTitle(title).setContentText(content).setSmallIcon(icon).setContentIntent(pt).setTicker(tickertext).build();

        notification.defaults = Notification.DEFAULT_ALL;
        //这是设置通知是否同时播放声音或振动，声音为Notification.DEFAULT_SOUND  
        //振动为Notification.DEFAULT_VIBRATE;  
        //Light为Notification.DEFAULT_LIGHTS，在我的Milestone上好像没什么反应  
        //全部为Notification.DEFAULT_ALL  
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) AppContext.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notification_id, notification);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.e("[极光推送] This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (Exception e) {
                    LogUtils.e("[极光推送] Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    private void registerPushId() {
        HashMap<String, String> map = new HashMap<>();
        map.put("device", "0");
        map.put("deviceId", MacUtil.getAdresseMAC(AppContext.getInstance().getApplicationContext()));
        map.put("registrationId", AppContext.JpushId);


        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        HttpRequestDao httpRequestDao = new HttpRequestDao();
        httpRequestDao.registerPushId(gContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
            }
        });
    }

    public static final class JpushKey {
        public static final String CUSTOM_CIRCLE_ESSAY_PUSH_MESSAGE = "essayPushMessage";//圈子消息

        public static final String BADGE_PUSH_MESSAGE = "badgePushMessage";//勋章类消息

        public static final String LEVEL_PUSH_MESSAGE = "levelPushMessage";//等级类消息

        public static final int DEFAULT_CIRCLE_ESSAY_PUSH_MESSAGE = 100;
    }
}
