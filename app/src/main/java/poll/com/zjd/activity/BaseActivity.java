package poll.com.zjd.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.PopupWindow;

import com.umeng.analytics.MobclickAgent;

import poll.com.zjd.annotation.FmyViewInject;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.app.AppStates;
import poll.com.zjd.callback.MainActivityCallBack;
import poll.com.zjd.manager.ActivityManager;
import poll.com.zjd.manager.TitleBarManager;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ShareUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  10:32
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */

public class BaseActivity extends AppCompatActivity {
    protected String TAG_ACTIVITY = "BaseActivity";//当前activity的simpleName
    public static final String STATE_INTENT_KEY = "STATE_INTENT_KEY";
    public TitleBarManager titleBarManager;  //沉浸式
    public Context mContext;
    public ActivityManager activityManager;
    public AppContext appContext;
    public AppStates appStates;
    protected Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        ActionBar view = getSupportActionBar();
        if (view!=null){
            view.hide();
        }
        mContext = this;
        res = getResources();
        appStates = AppStates.getInstance();
        TAG_ACTIVITY = getClass().getSimpleName();
        activityManager = ActivityManager.getAppManager();
        appContext = AppContext.getInstance();
        activityManager.addActivity(this);
        titleBarManager = new TitleBarManager(this);
        FmyViewInject.inject(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG_ACTIVITY + "Activity Life cycle =====> onRestart");

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG_ACTIVITY + "Activity Life cycle =====> onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        ShareUtils.dismissShareDialog();
        MobclickAgent.onPageStart(TAG_ACTIVITY);
        MobclickAgent.onResume(this); // 统计时长
        LogUtils.d(TAG_ACTIVITY + "Activity Life cycle =====> onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG_ACTIVITY);
        MobclickAgent.onPause(this);
        LogUtils.d(TAG_ACTIVITY + "Activity Life cycle =====> onPause");

    }


    @Override
    protected void onStop() {
        super.onStop();
        //tatService.onStop(this);
        LogUtils.d(TAG_ACTIVITY + "Activity Life cycle =====> onStop");
    }

    @Override
    public synchronized void onBackPressed() {

        appStates.previousPageName = TAG_ACTIVITY;

        if (!activityManager.containsActivity(MainActivity.class)) {
            appContext.startActivity(mContext, MainActivity.class, null);
        } else {
            super.onBackPressed();
        }
        appContext.onActivityAnim(this, getClass().getName(), false);
    }

    // 区分点击back键和主动关闭activity
    public void back(){
        appStates.previousPageName = TAG_ACTIVITY;

        if (!activityManager.containsActivity(MainActivity.class)) {
            appContext.startActivity(mContext, MainActivity.class, null);
        } else {
            finish();
        }
        appContext.onActivityAnim(this, getClass().getName(), false);
    }

    //刷新页面状态，如已登录，未登录状态
    public void refreshData(){

    }

}
