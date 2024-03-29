package poll.com.zjd.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import poll.com.zjd.R;
import poll.com.zjd.app.AppContext;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：支付密码输入框
 */
public class PasswordView extends LinearLayout {

    private ImageView[] mDataTv ;

    private EditText mPasswordEt ;

    private StringBuffer mContent ;

    private callBack listener;
    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context , R.layout.layout_password , this);
        mDataTv = new ImageView[6];
        mDataTv[0] = (ImageView) findViewById(R.id.tv_password_1);
        mDataTv[1] = (ImageView) findViewById(R.id.tv_password_2);
        mDataTv[2] = (ImageView) findViewById(R.id.tv_password_3);
        mDataTv[3] = (ImageView) findViewById(R.id.tv_password_4);
        mDataTv[4] = (ImageView) findViewById(R.id.tv_password_5);
        mDataTv[5] = (ImageView) findViewById(R.id.tv_password_6);
        mPasswordEt = (EditText) findViewById(R.id.et_password);

        mPasswordEt.setCursorVisible(false);

        mContent = new StringBuffer();

        initEvent();
    }

    public void setCallBack(callBack completeListener){
        this.listener = completeListener ;
    }

    private void initEvent(){
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(listener != null){
                    listener.onTextChanged(s,start,before,count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                  if(!"".equals(s.toString())){
                      if(mContent.length() >= mDataTv.length){
                          mPasswordEt.setText("");
                      }else {
                          mContent.append(s.toString());
                          mPasswordEt.setText("");
                          for(int i = 0 ; i < mContent.length() ; i ++){
                              mDataTv[i].setSelected(true);
                          }
                      }
                      if(mContent.length() == mDataTv.length){
                          if(listener != null){
                              listener.complete(getPassword());
                          }
                      }
                  }
            }
        });

        mPasswordEt.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN){
                    delete();
                    return true;
                }
                return false;
            }
        });
    }

    private void delete(){
        if(mContent == null){
            return;
        }
        if(mContent.length() > 0){
            mContent.deleteCharAt(mContent.length() - 1);
            for(int i = 0 ; i < mDataTv.length ; i ++){
                mDataTv[i].setSelected(false);
            }
            for(int i = 0 ; i < mContent.length() ; i ++){
                mDataTv[i].setSelected(true);
        }
        }
    }

    public String getPassword(){
        return  mContent.toString();
    }

    public boolean isComplete(){
        boolean f=false;
        if(!TextUtils.isEmpty(mContent.toString()) && mContent.length()==mDataTv.length){
            f=true;
        }
        return f;
    }

    public void showInputMethod(){
        mPasswordEt.requestFocus();
        InputMethodManager inputMethodManager= (InputMethodManager) AppContext.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mPasswordEt,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setInputType(int inputType){
        mPasswordEt.setInputType(inputType);
    }

    public void clearText(){
        for(int i = 0 ; i < mContent.length() ; i ++){
            mDataTv[i].setSelected(false);
        }
        mContent=new StringBuffer();
    }

    public interface callBack {
        void complete(String password);
        void onTextChanged(CharSequence s, int start, int before, int count);
    }
}
