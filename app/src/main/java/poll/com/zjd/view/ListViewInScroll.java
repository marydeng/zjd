package poll.com.zjd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/17  22:36
 * 包名:     poll.com.zjd.view
 * 项目名:   zjd
 * listView嵌套在Scrollview里面导致冲突的处理方法
 */

public class ListViewInScroll extends ListView {
    public ListViewInScroll(Context context) {
        super(context);
    }

    public ListViewInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewInScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //通过复写其onMeasure方法、达到对ScrollView适配的效果

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
