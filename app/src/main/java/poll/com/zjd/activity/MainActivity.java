package poll.com.zjd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poll.com.zjd.R;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.callback.MainActivityCallBack;
import poll.com.zjd.fragment.BaseFragment;
import poll.com.zjd.fragment.ClassFragment;
import poll.com.zjd.fragment.HomeFragment;
import poll.com.zjd.fragment.LoginFragment;
import poll.com.zjd.fragment.MyFragment;
import poll.com.zjd.fragment.ShoppingCartFragment;
import poll.com.zjd.fragment.ZjzFragment;
import poll.com.zjd.manager.TitleBarManager;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.view.CustomViewPager;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  10:17
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, MainActivityCallBack, MyLocationManagerUtil.MyLocationCallBack {

    public static final int GOWHERE = 100;                 //去哪里
    public static final String GOWHERETYPE = "gowhereType";//到哪里去
    public static final String EXTRA_PAGE_INDEX = "MainActivity.extra.page.index";

//    private ViewPager fragmentPager;
    private CustomViewPager fragmentPager;
    private RelativeLayout fragmentOne, fragmentTwo, fragmentThree, fragmentFor, fragmentFive;
    private Context mContext;
    private LinearLayout mStatusLv;
    private long exitTime = 0;//结束app的时间判断用
    private List<RelativeLayout> selectors = new ArrayList<>();

    private MyFragmentAdapter myFragmentAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private void isShowStatusBar() {
        int height = titleBarManager.getStatusBarHeight();
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mStatusLv.getLayoutParams();
        //臻实惠跟我的不需要头
        int currentPosition = fragmentPager.getCurrentItem();
        if (currentPosition == 4) {
            linearParams.height = 0;          // 设置高度
        } else {
            linearParams.height = height;     // 设置高度
        }
        mStatusLv.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        mStatusLv.setBackgroundColor(TitleBarManager.STATUS_BAR_COLOR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //自动更新
        Bugly.init(getApplicationContext(), AppContext.APP_ID_BUGLY, false);
//        titleBarManager.setStatusBarView();  //设置沉浸式
        //填充状态栏的头
        mStatusLv = (LinearLayout) findViewById(R.id.home_head_ll);
        // 状态栏沉浸，4.4+生效 <<<<<<<<<<<<<<<<<
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mContext = this;
        fragmentPager = (CustomViewPager) findViewById(R.id.fragment_pager);
        fragmentOne = (RelativeLayout) findViewById(R.id.fragment_one);
        fragmentTwo = (RelativeLayout) findViewById(R.id.fragment_two);
        fragmentThree = (RelativeLayout) findViewById(R.id.fragment_three);
        fragmentFor = (RelativeLayout) findViewById(R.id.fragment_for);
        fragmentFive = (RelativeLayout) findViewById(R.id.fragment_five);
        fragmentOne.setSelected(true);
        selectors.add(0, fragmentOne);
        selectors.add(1, fragmentTwo);
        selectors.add(2, fragmentThree);
        selectors.add(3, fragmentFor);
        selectors.add(4, fragmentFive);

        //设置点击监听
        setItemClickListener();

        //设置适配器
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        fragmentPager.setAdapter(myFragmentAdapter);
        //设置滑动监听
        fragmentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                myFragmentAdapter.getFragmentMap().get(String.valueOf(position)).onChangeToFront();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //0是静止，1是正在滑动，2是停止滑动
                if (state == 2) {
                    //设置滑动ViewPager导航同步变化
                    changeSelectorColor(fragmentPager.getCurrentItem());
                    isShowStatusBar();
                    Fragment fragment = myFragmentAdapter.getFragmentMap().get(String.valueOf(fragmentPager.getCurrentItem()));
                    if (null!=fragment){
                        ((BaseFragment)fragment).onChangeToFront();
                    }
//                    myFragmentAdapter.getFragmentMap().get(String.valueOf(fragmentPager.getCurrentItem())).
                }
            }
        });
        fragmentPager.setOffscreenPageLimit(1);
        isShowStatusBar();

        MyLocationManagerUtil.getInstance().setCallBack(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            int position = intent.getIntExtra(MainActivity.EXTRA_PAGE_INDEX, -1);
            if (position != -1) {
                switchPage(position);
            }
        }
    }

    @Override
    public void refreshData() {
        if (null != myFragmentAdapter && !ObjectUtils.isEmpty(myFragmentAdapter.getFragmentMap().values())) {
            for (BaseFragment fragment : myFragmentAdapter.getFragmentMap().values()) {
                if (null != fragment) {
                    fragment.refreshData();
                }
            }
        }
    }

    /**
     * viewPager的adapter，改变当前fragment
     */
    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private Map<String, BaseFragment> fragmentMap;
        private FragmentManager fms;
        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragmentMap = new HashMap<>();
            fms = fm;
        }
        public void setFragment(Fragment fragment) {
            if(fragment != null){
                FragmentTransaction ft = fms.beginTransaction();

                String fragmentTag = fragment.getTag();

                ft.remove(fragment);

                Fragment aa = new ClassFragment();

                ft.add(aa,fragmentTag);

                ft.attach(aa);
                ft.commit();

                ft=null;
                fms.executePendingTransactions();
            }
            LogUtils.e("回-----");
            this.notifyDataSetChanged();
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            //得到缓存的fragment
//            Fragment fragment = (Fragment) super.instantiateItem(container, position)

            //得到tag，这点很重要
//            String fragmentTag = fragment.getTag();
//            if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
//                //如果这个fragment需要更新
//                LogUtils.e("回-----进入");
//                FragmentTransaction ft = fms.beginTransaction();
//                //移除旧的fragment
//                ft.remove(fragment);
//                //换成新的fragment
////                fragment = fragments[position % fragments.length];
//                fragment = fragmentMap.get(position);
//                //添加新fragment时必须用前面获得的tag，这点很重要
//                ft.add(container.getId(), fragment, fragmentTag);
//                ft.attach(fragment);
//                ft.commit();
//
//                //复位更新标志
//                fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
//            }
//            return fragment;
//        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new ClassFragment();
                    break;
                case 2:
                    if (appContext.isGuest) {
                        fragment = new LoginFragment();
                    } else {
                        fragment = new ZjzFragment();
                    }
                    break;
                case 3:
                    if (appContext.isGuest) {
                        fragment = new LoginFragment();
                    } else {
                        fragment = new ShoppingCartFragment();
                    }
                    break;
                case 4:
                    fragment = new MyFragment();
                    break;
            }
            if (fragment != null) {
                fragment.setMainActivityCallBack(MainActivity.this);
            }
            fragmentMap.put(String.valueOf(position), fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            //一共5个页面
            return 5;
        }

        public Map<String, BaseFragment> getFragmentMap() {
            return fragmentMap;
        }
    }


    @Override
    public void onClick(View v) {
        int flag = v.getId();
        switch (flag) {
            case R.id.fragment_one:
                setCurrentStatus(0);
                break;
            case R.id.fragment_two:
                setCurrentStatus(1);
                break;
            case R.id.fragment_three:
                setCurrentStatus(2);
                break;
            case R.id.fragment_for:
                setCurrentStatus(3);
                break;
            case R.id.fragment_five:
                setCurrentStatus(4);
                break;
        }
    }

    private void setCurrentStatus(int position) {
        //如果当前点击的不是当前页面，才去切换页面
        if (fragmentPager.getCurrentItem() != position) {
            changeSelectorColor(position);
            fragmentPager.setCurrentItem(position);
            isShowStatusBar();
        }

    }

    private void setItemClickListener() {
        for (RelativeLayout selector : selectors) {
            selector.setOnClickListener(MainActivity.this);
        }
    }


    private void changeSelectorColor(int position) {
        setAllBlack();
        selectors.get(position).setSelected(true);
    }

    private void setAllBlack() {
        for (int i = 0; i < selectors.size(); i++) {
            selectors.get(i).setSelected(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (null != myFragmentAdapter && !ObjectUtils.isEmpty(myFragmentAdapter.getFragmentMap().values())) {
            for (BaseFragment fragment : myFragmentAdapter.getFragmentMap().values()) {
                if (null != fragment) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

        switch (resultCode) {
            case GOWHERE:
                int type = data.getIntExtra(GOWHERETYPE, 0);
                switchPage(type);
                break;
            default:
                break;
        }
    }

    // fragment 回调回来，切换页面
    @Override
    public void switchPage(int pageId) {
        setCurrentStatus(pageId);
    }

    @Override
    public void onMyLocationChanged() {
        Fragment fragment = myFragmentAdapter.getFragmentMap().get(String.valueOf(fragmentPager.getCurrentItem()));
        if (null!=fragment){
            if (fragment instanceof ClassFragment) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.test_fra_2, new ClassFragment());
//                fragmentTransaction.commitAllowingStateLoss();
                FragmentManager fms;
                fms = getSupportFragmentManager();
                FragmentTransaction ft = fms.beginTransaction();
                for (int i=0;i<myFragmentAdapter.getFragmentMap().size();i++){

                    ft.remove(myFragmentAdapter.getFragmentMap().get(i+""));
                }
                ft.commit();
                ft=null;
                fms.executePendingTransactions();

                myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
//                myFragmentAdapter.setFragment(fragment);
                fragmentPager.setAdapter(myFragmentAdapter);
                fragmentPager.setCurrentItem(1);
            } else {
                ((BaseFragment) fragment).onLocationChanged();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != myFragmentAdapter && !ObjectUtils.isEmpty(myFragmentAdapter.getFragmentMap().values())) {
            for (BaseFragment fragment : myFragmentAdapter.getFragmentMap().values()) {
                if (fragment instanceof ZjzFragment) {
                    if (((ZjzFragment) fragment).onKeyDown(keyCode, event)) return true;
                }else if (fragment instanceof HomeFragment){
                    if (((HomeFragment) fragment).onKeyDown(keyCode, event)) return true;
                }
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次返回键返回桌面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
//                MobclickAgent.onKillProcess(this);
//                activityManager.finishAllActivity();
//                android.os.Process.killProcess(android.os.Process.myPid());//杀掉App中的所有进程
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onKillProcess(this);
    }
}
