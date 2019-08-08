package poll.com.zjd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

import poll.com.zjd.model.GoodsClass;

/**
 * Created by Tommy on 2017/7/4.
 */

public class SelectAdapter extends CommonNavigatorAdapter {

    private List<GoodsClass> mDates;
    private ViewPager mViewPager;

    public SelectAdapter(List<GoodsClass> mDates,ViewPager viewPager){
        this.mDates = mDates;
        this.mViewPager = viewPager;
    }

    @Override
    public int getCount() {
        return mDates == null ? 0 : mDates.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#333333"));
        colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#e51728"));

        colorTransitionPagerTitleView.setText(mDates.get(index).getCatName());

        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(index);
            }
        });
        return colorTransitionPagerTitleView;

    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setColors(Color.parseColor("#e51728"));
        indicator.setMode(1);  //2固定短 //1根据标题长度
        return indicator;
    }
}
