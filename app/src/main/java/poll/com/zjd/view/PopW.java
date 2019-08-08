package poll.com.zjd.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/30  10:04
 * 包名:     poll.com.zjd.view
 * 项目名:   zjd
 */

public class PopW extends PopupWindow{

    public PopW(Context context, View rootView ,int style){
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(style);
        setContentView(rootView);
    }

    public PopW(Context context, View rootView , int width, int height,int style){

        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(style);
        setContentView(rootView);
    }
}
