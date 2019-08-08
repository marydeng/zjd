package poll.com.zjd.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import poll.com.zjd.adapter.superadapter.SuperAdapter;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/11/18
 * 文件描述：可以使用BaseAdapter的Listview
 */
public class LinearLayoutForListView extends LinearLayout {

    private SuperAdapter adapter;
    private OnClickListener onClickListener = null;

    public LinearLayoutForListView(Context context) {
        super(context);
    }

    public LinearLayoutForListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutForListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        int count = adapter.getCount();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            if (null != this.onClickListener)
                v.setOnClickListener(this.onClickListener);
            addView(v, i);
        }

    }

    public void setAdapter(SuperAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
