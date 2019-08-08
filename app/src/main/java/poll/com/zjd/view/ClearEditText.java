package poll.com.zjd.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import poll.com.zjd.R;


/**
 * 带清除键的edit text
 * Created by marydeng on 2017/7/8.
 */

public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    private static final String TAG = ClearEditText.class.getSimpleName();
    private Drawable clearDrawable;
    private boolean hasFocus;

    public ClearEditText(Context context) {
        super(context);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        clearDrawable = getCompoundDrawables()[2];
        if (null == clearDrawable) {
            clearDrawable = getResources().getDrawable(R.drawable.clearicon);
        }
        int size=getResources().getDimensionPixelSize(R.dimen.x18);
        clearDrawable.setBounds(0, 0, size, size);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            if (getCompoundDrawables()[2] != null) {
                if (event.getX() > (getWidth() - getTotalPaddingRight()) && event.getX() < (getWidth() - getPaddingRight())) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    //当输入框里面内容发生变化的时候回调的方法
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int end, int after) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        }
    }

    //当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏 
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    private void setClearIconVisible(boolean clearIconVisible) {
        Drawable right = clearIconVisible ? clearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
}
