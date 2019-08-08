package poll.com.zjd.viewmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;

import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.os.Build;



/**
 * 业务视图控制和数据
 */
public abstract class BaseViewModel extends BaseObservable {

    public String currentActivityName;//当前界面上展示的Activity的simpleName

    protected Context mContext;
//    protected AppContext appContext;
    protected Resources res;
    protected IViewModelCallback viewModelCallback;

    public BaseViewModel(Context _context) {
        mContext = _context;
//        appContext = AppContext.getInstance();
        res = mContext.getResources();
    }

    /**
     * ViewModel数据值的初始化，方便在一个模块一个ViewModel中在进到具体界面的数据恢复初值
     */
    protected abstract void initField();

    /**
     * 设置action listener
     */
    public void setViewModelListener(IViewModelCallback _viewModelCallback) {
        viewModelCallback = _viewModelCallback;
    }

    /**
     * 通过资源id获取字符串
     */
    public String getString(int resouceId) {
        return res.getString(resouceId);
    }

    /**
     * 通过资源id获取drawable
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Drawable getDrawble(int resourceId) {
        return res.getDrawable(resourceId, null);
    }

}
