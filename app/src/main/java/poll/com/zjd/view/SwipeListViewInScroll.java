package poll.com.zjd.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/9/10
 * 文件描述：
 */
public class SwipeListViewInScroll extends SwipeListView {
    public SwipeListViewInScroll(Context context) {
        super(context);
    }

    public SwipeListViewInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //通过复写其onMeasure方法、达到对ScrollView适配的效果

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
