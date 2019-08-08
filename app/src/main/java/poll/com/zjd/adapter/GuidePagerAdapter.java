package poll.com.zjd.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import poll.com.zjd.utils.ObjectUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/15
 * 文件描述：引导页滑动图片的adapter
 */
public class GuidePagerAdapter extends PagerAdapter {

    private List<ImageView> imageViewList;

    public GuidePagerAdapter(List<ImageView> imageViewList) {
        this.imageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return ObjectUtils.isEmpty(imageViewList) ? 0 : imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position), 0);
        return imageViewList.get(position);
    }
}
