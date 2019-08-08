package poll.com.zjd.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.annotation.FmyViewInject;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.callback.MainActivityCallBack;
import poll.com.zjd.manager.ActivityManager;
import poll.com.zjd.utils.LogUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  10:17
 * 包名:     poll.com.zjd.fragment
 * 项目名:   zjd
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG_FRAGMENT = "BaseFragment";//当前Fragment的simpleName
    protected AppContext appContext;
    protected Resources res;
    protected Context gContext;
    protected Activity mActivity;
    protected View view = null;
    protected ActivityManager activityManager;
    /**
     * 布局解释器
     * @warn 不能在子类中创建
     */
    protected LayoutInflater inflater = null;
    /**
     * 添加这个Fragment视图的布局
     * @warn 不能在子类中创建
     */
    @Nullable
    protected ViewGroup container = null;

    private boolean isAlive = false;
    private boolean isRunning = false;

    protected MainActivityCallBack mainActivityCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG_FRAGMENT = getClass().getName();
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onAttach");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = AppContext.getInstance();
        gContext = getActivity();
        mActivity = getActivity();
        res = getResources();
        activityManager = ActivityManager.getAppManager();
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onCreate");
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        isAlive = true;
        this.inflater = inflater;
        this.container = container;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onActivityCreated");

    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG_FRAGMENT); // 统计页面LogUtils.generateTag(LogUtils.getCallerStackTraceElement())
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onResume");

    }


    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG_FRAGMENT);//MainScreen
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onStop");

    }

    @Override
    public void onDestroyView() {
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.d(TAG_FRAGMENT + "Fragment Life cycle =====> onDestroy");
        super.onDetach();
    }

    /**
     * startActivityForResult 跳转
     */
    public void startActivityForResult(Class<? extends BaseActivity> activity, int requestCode, Bundle bundle) {

        String activityName = appContext.loginPermissionFilter(getActivity(), activity.getName());

        Intent intent = new Intent();
        intent.setClassName(getActivity(), activityName);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivityForResult(intent, requestCode);

        appContext.onActivityAnim(getActivity(), activityName, true);
    }


    /**
     * Activity回调Fragment的数据方法，统一处理
     *
     * @param code
     * @param data
     */
    public void onActivityForResult(String code, String data) {

    }

    public void setContentView(View v, ViewGroup.LayoutParams params) {
        view = v;
        FmyViewInject.inject(this);
    }
    public void setContentView(View view) {
        setContentView(view, null);
    }
    public void setContentView(int layoutResID) {
        setContentView(inflater.inflate(layoutResID, container, false));
    }
    public <V extends View> V findViewById(int id) {
        return (V) view.findViewById(id);
    }

    public Intent getIntent() {
        return mActivity.getIntent();
    }

    //刷新页面状态，如已登录，未登录状态
    public void refreshData(){

    }

    // 主要用于主页
    public void onChangeToFront(){

    }

    public void onLocationChanged(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setMainActivityCallBack(MainActivityCallBack mainActivityCallBack) {
        this.mainActivityCallBack = mainActivityCallBack;
    }
}

