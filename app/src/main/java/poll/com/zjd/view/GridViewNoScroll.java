package poll.com.zjd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/9  11:13
 * 包名:     poll.com.zjd.view
 * 项目名:   zjd
 */

public class GridViewNoScroll extends GridView {

    public GridViewNoScroll(Context context) {
        super(context);
    }

    public GridViewNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewNoScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
