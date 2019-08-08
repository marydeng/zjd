package poll.com.zjd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.CheckPhone;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.ClearEditText;

import com.lzy.okgo.model.Response;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：更改手机绑定手机号
 */
@FmyContentView(R.layout.activity_change_phone_number)
public class ChangePhoneNumberActivity extends AccountBindBaseActivity implements View.OnClickListener {
    private static final String TAG = ChangePhoneNumberActivity.class.getSimpleName();
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.login_account)
    private ClearEditText accountEditText;
    @FmyViewView(R.id.login_very_code)
    private ClearEditText codeEditText;
    @FmyViewView(R.id.get_verify_code)
    private TextView getCodeTextView;
    @FmyViewView(R.id.submit_button)
    private TextView submitButton;
    @FmyViewView(R.id.current_phone_number)
    private TextView currentPhoneNumberTxt;

    private HttpRequestDao httpRequestDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.accountEditText = accountEditText;
        viewHolder.backImageView = backImageView;
        viewHolder.codeEditText = codeEditText;
        viewHolder.getCodeTextView = getCodeTextView;
        viewHolder.loginButton = submitButton;
        viewHolder.titleTextView = titleTextView;
        setViewHolder(viewHolder);

        initData();
    }

    private void initData() {
        titleTextView.setText(R.string.change_phone_number);
        titleBarManager.setStatusBarView();  //设置沉浸式

        currentPhoneNumberTxt.setText(appContext.bindPhoneNumber);
        httpRequestDao = new HttpRequestDao();
    }

    @FmyClickView({R.id.head_back, R.id.get_verify_code, R.id.submit_button})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.get_verify_code:
                if (!StringUtils.isBlank(getCodeTextView.getText().toString().trim()) && appContext.bindPhoneNumber.equals(getCodeTextView.getText().toString().trim())) {
                    ToastUtils.showToast(this, R.string.the_same_with_old_number, Toast.LENGTH_LONG);
                    return;
                }
                checkPhone();
                break;
            case R.id.submit_button:
                changePhoneNumber();
                break;
            default:
                break;
        }
    }

    private void checkPhone(){
        httpRequestDao.checkPhone(this, accountEditText.getText().toString().trim(), new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                CheckPhone checkPhone = new Gson().fromJson(result,CheckPhone.class);
                if (checkPhone.getIsBind()==0){
                    //未注册
                    getVeryCode("2");
                }else {
                    //已注册
                    ToastUtils.showToast(ChangePhoneNumberActivity.this,"该手机号已绑定过",1);
                }
            }
        });
    }

    private void changePhoneNumber() {
        HashMap<String, String> map = new HashMap<>();
        map.put("oldPhone", appContext.bindPhoneNumber);
        map.put("newPhone", accountEditText.getText().toString().trim());
        map.put("validateCode", codeEditText.getText().toString().trim());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(this, null, null);
        httpRequestDao.changePhoneNumber(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                appContext.bindPhoneNumber = accountEditText.getText().toString().trim();
                setResult(AccountSafeActivity.RESULT_CODE_CHANGE_PHONE);
                ChangePhoneNumberActivity.this.finish();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });
    }
}
