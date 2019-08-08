package poll.com.zjd.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.manager.GlideManager;


/**
 * Created by mary on 2017/9/8.
 */

public class ImageAutoViewPager extends RelativeLayout {

    public final static int BOTTOM_CENTER = 0;  //底部中间
    public final static int BOTTOM_RIGHT = 1;   //底部右边
    public final static int BOTTOM_LEFT = 2;    //底部左边

    private List<String> imageData;
    private Context mContext;
    private AutoPagerAdapter mAdapter;
    private LinearLayout mLr;
    private ViewPager mViewPager;
    private int currentPosition=0;
    private long clickDownTime,clickUpTime;
    private int height;

    protected int interval = 3000;   //滚动时间
    protected boolean isLoop = false;//滚动状态
    protected int currentId,otherId; //设置切图的资源id

    public OnAutoViePagerClickListener onAutoViePagerClickListener;  //点击事件回调

    public interface OnAutoViePagerClickListener {
        void click(int position);
    }

    public void setOnAutoViePagerClickListener(OnAutoViePagerClickListener onAutoViePagerClickListener) {
        this.onAutoViePagerClickListener = onAutoViePagerClickListener;
    }

    public ImageAutoViewPager(Context context) {
        super(context);
        initView(context);
    }

    public ImageAutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setImageDates(List<String> imageData, int currentResource, int defaultResource, int model) {
        this.imageData = imageData;
        this.currentId = currentResource;
        this.otherId = defaultResource;
        mViewPager.setAdapter(mAdapter);
        initPoint(model);
        startAutoViewPager();
    }
    public boolean getLoopStatus(){
        return isLoop;
    }


    //初始化View
    private void initView(Context context) {

        mContext = context;
        mAdapter = new AutoPagerAdapter();
        mViewPager = new ViewPager(mContext);

        LayoutParams viewPagerParams =
                new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        this.addView(mViewPager,viewPagerParams);

        //状态改变监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (imageData.size()>0 && mLr!=null){
                    for(int i=0;i<mLr.getChildCount();i++){
                        if ((position % imageData.size())==i){
                            ((ImageView)mLr.getChildAt(i)).setImageDrawable(getResources().getDrawable(currentId));
                        }else {
                            ((ImageView)mLr.getChildAt(i)).setImageDrawable(getResources().getDrawable(otherId));
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //触摸监听
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        clickDownTime = System.currentTimeMillis();
                    case MotionEvent.ACTION_MOVE:
                        isLoop = true;
                        stopAutoViewPager();
                        break;
                    case MotionEvent.ACTION_UP:
                        clickUpTime = System.currentTimeMillis();
                        //点击事件的回调
                        if (clickUpTime - clickDownTime < 100){
                            if (onAutoViePagerClickListener!=null){
                                onAutoViePagerClickListener.click(currentPosition % imageData.size());
                            }
                        }
                    case MotionEvent.ACTION_CANCEL:
                        isLoop = false;
                        startAutoViewPager();
                    default:
                        break;
                }
                return false;
            }
        });

    }

    //初始化小圆点
    private void initPoint(final int model){
        this.removeView(mLr);
        mLr = new LinearLayout(mContext);
        int margin = 10;  //距离旁边的距离 单位dp
            LayoutParams linParams =
                    new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            if (model==BOTTOM_CENTER){
            linParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            linParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            linParams.setMargins(0,0,0,dip2px(mContext,margin));
        }else if(model==BOTTOM_LEFT){
            linParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            linParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            linParams.setMargins(dip2px(mContext,margin),0,0,dip2px(mContext,margin));  //转换dp
        }else if(model==BOTTOM_RIGHT){
            linParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            linParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            linParams.setMargins(0,0,dip2px(mContext,margin),dip2px(mContext,margin));  //转换dp
        }

        this.addView(mLr,linParams);

        ViewTreeObserver vto2 = mViewPager.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height= mViewPager.getHeight();
                Log.i("-----","高度--"+height);
                int mDotWidth = (height /20);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotWidth, mDotWidth);
                if (model==BOTTOM_RIGHT){
                    params.rightMargin = (int) (mDotWidth / 1.5);  //小圆点之间的距离
                }else {
                    params.leftMargin = (int) (mDotWidth / 1.5);   //小圆点之间的距离
                }

                for (int i = 0; i< imageData.size(); i++){
                    ImageView iv = new ImageView(mContext);
                    if (i==0){
                        iv.setImageDrawable(getResources().getDrawable(currentId));
                    }else {
                        iv.setImageDrawable(getResources().getDrawable(otherId));
                    }
                    iv.setLayoutParams(params);
                    mLr.addView(iv);
                }
            }
        });

    }

    //轮播的handler
    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager.getChildCount() > 1 && isLoop) {
                mHandler.postDelayed(this, interval);
                currentPosition++;
                mViewPager.setCurrentItem(currentPosition, true);
            }
        }
    };

    //开始轮播
    private void startAutoViewPager() {
        if (imageData.size()==1)return;  //如果他妈只有一条数据,就不滚了
        if (!isLoop && mViewPager != null) {
            mHandler.postDelayed(mRunnable, interval);// 每两秒执行一次runnable.
            isLoop = true;
        }
    }
    //停止轮播
    public void stopAutoViewPager() {
        if (isLoop && mViewPager != null && mHandler!=null) {
            mHandler.removeCallbacks(mRunnable);
            isLoop = false;
        }
    }
    //轮播的适配器
    private class AutoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.item_auto_view,null);

            ImageView iv =(ImageView)view.findViewById(R.id.icon_view);
            if (imageData.size()>0){
                GlideManager.showImageDefault(mContext, imageData.get(position % imageData.size()), iv, R.drawable.default_img_a, R.drawable.default_img_a);
            }

            container.addView(view);

            return view;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
