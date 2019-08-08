package poll.com.zjd.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import poll.com.zjd.R;
import poll.com.zjd.activity.LoginActivity;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.utils.LogUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/26
 * 文件描述：首页-未登录页面
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_login);
        initData();
        return view;
    }

    private void initData() {
    }

    @FmyClickView({R.id.login_button})
    @Override
    public void onClick(View view) {
        LogUtils.i("view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.login_button:
                appContext.startActivity(gContext, LoginActivity.class, null);
                break;
            default:
                break;
        }
    }
}
