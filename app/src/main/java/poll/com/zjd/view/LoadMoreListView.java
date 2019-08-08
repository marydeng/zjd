package poll.com.zjd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import poll.com.zjd.R;



/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  15:04
 * 包名:     poll.com.zjd.view
 * 项目名:   zjd
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private int totalItemCount;// 总数量
    private int lastVisibleItem;// 最后一个可见的item;
    private boolean isLoading = false;// 判断变量
    private RelativeLayout mRv_footer;
    private TextView mTv_footer;
    private ProgressBar mPb_footer;

    public OnRefreshInterface loadListener;// 接口变量

    public boolean enableLoad=true;

    // 加载更多数据的回调接口
    public interface OnRefreshInterface {
        public void onLoad();
    }

    public void setOnRefreshInterface(OnRefreshInterface loadListener) {

        this.loadListener = loadListener;
    }
    // 加载完成通知隐藏
    public void refreshComplete() {
        isLoading = false;
        mRv_footer.setVisibility(View.GONE);
    }
    public void refreshNoMore() {
        isLoading = false;
        mPb_footer.setVisibility(View.GONE);
        mTv_footer.setText("没有更多");
    }
    //重置
    public void reSetText(){
        mPb_footer.setVisibility(View.VISIBLE);
        mTv_footer.setText("加载更多...");
    }

    public LoadMoreListView(Context context) {
        super(context);
        initView(context);
    }

    public void setEnableLoad(boolean enableLoad) {
        this.enableLoad = enableLoad;
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View footer = inflater.inflate(R.layout.list_loadmore_footer, null);
        mRv_footer = (RelativeLayout) footer.findViewById(R.id.list_load_more_footer);
        mTv_footer = (TextView) footer.findViewById(R.id.list_load_textView);
        mPb_footer = (ProgressBar) footer.findViewById(R.id.list_load_progress);
        mRv_footer.setVisibility(View.GONE);

        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE && enableLoad) {
            if (!isLoading && loadListener!=null) {
                isLoading = true;
                mRv_footer.setVisibility(View.VISIBLE);
                loadListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }
}
