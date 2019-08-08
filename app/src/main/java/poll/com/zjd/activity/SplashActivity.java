package poll.com.zjd.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.GuidePagerAdapter;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.manager.TitleBarManager;
import poll.com.zjd.model.HomeBean;
import poll.com.zjd.utils.CityVoUtil;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.SPUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/6  22:56
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */

public class SplashActivity extends Activity implements ViewPager.OnPageChangeListener ,View.OnClickListener{
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static final String Key_Is_Not_First_Start_Up = "key.is.not.first.start.up";
    // unit:ms
    private static final int COUNTDONWTIME = 4000;
    private CountDownTimer timer;
    private TextView countDownTextView;
    private HttpRequestDao httpRequestDao = new HttpRequestDao();
    private Context mContext;
    private SPUtils spUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        TitleBarManager titleBarManager = new TitleBarManager(this);
        titleBarManager.setStatusBarView();  //设置沉浸式

        CityVoUtil.saveCityData(this);


        spUtils = new SPUtils(this, SPUtils.HOME_START_ADS_TIME_LONG);
        boolean isNotFirstStartUp = spUtils.getBoolean(this, Key_Is_Not_First_Start_Up);
        if (!isNotFirstStartUp) {
            //显示引导页
            int[] imgIdArray = new int[]{R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three, R.drawable.guide_four};
            List<ImageView> mImageViews = new ArrayList<>();
            for (int drawableId : imgIdArray) {
                ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(drawableId);
                mImageViews.add(imageView);
            }

            mImageViews.get(imgIdArray.length - 1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtils.i("view.getId=" + view.toString());
                    spUtils.put(SplashActivity.this,Key_Is_Not_First_Start_Up,true);
                    startActivity(MainActivity.createIntent(SplashActivity.this));
                    finish();
                }
            });

            ViewPager viewPager = (ViewPager) findViewById(R.id.splash_view_pager);
            GuidePagerAdapter guidePagerAdapter = new GuidePagerAdapter(mImageViews);
            viewPager.setAdapter(guidePagerAdapter);


        } else {
            // 显示配置广告
            show();
            countDownTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    httpRequestDao.getStartAd(SplashActivity.this, new OkGoStringCallback() {
                        @Override
                        public void onSuccessEvent(String result) {
                            try {
                                HomeBean homeBean = new Gson().fromJson(result, HomeBean.class);
                                String startUrl = homeBean.row13_site1.get(0).imgUrl;
                                GlideManager.showImageDefaultSplash(mContext, startUrl, (ImageView) findViewById(R.id.splash_Rv), R.drawable.splash, R.drawable.splash);
                            } catch (Exception e) {
                                LogUtils.e(e.toString());
                            }

                            countDownTextView.setVisibility(View.VISIBLE);
                            timer.start();

                        }

                        @Override
                        public void onError(Response<String> response) {
                            LogUtils.e("失败");
                            countDownTextView.setVisibility(View.VISIBLE);
                            timer.start();
                            Glide.with(mContext).load(R.drawable.splash).centerCrop().into((ImageView) findViewById(R.id.splash_Rv));
                        }
                    });
                }
            },1500);

        }

    }

    //显示倒计时
    private void show() {
        countDownTextView = (TextView) findViewById(R.id.timer_text);
        countDownTextView.setOnClickListener(this);

        final String txt = getResources().getString(R.string.guide_count_down);
        String firstTxt = String.format(txt, COUNTDONWTIME / 1000);
        countDownTextView.setText(firstTxt);
        timer = new CountDownTimer(COUNTDONWTIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtils.i("millisUntilFinished=" + millisUntilFinished);
                String s = String.format(txt, millisUntilFinished / 1000);
                countDownTextView.setText(s);
            }

            @Override
            public void onFinish() {
                String s = String.format(txt, 0);
                countDownTextView.setText(s);
                startActivity(MainActivity.createIntent(SplashActivity.this));
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.fade, R.anim.hold);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        LogUtils.i("v.getId="+v.toString());
        switch (v.getId()){
            case R.id.timer_text:
                startActivity(MainActivity.createIntent(SplashActivity.this));
                finish();
                break;
            default:
                break;
        }
    }
}
