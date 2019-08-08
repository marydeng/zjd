package poll.com.zjd.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import poll.com.zjd.R;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.manager.MyCacheManager;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.SPUtils;
import poll.com.zjd.utils.ToastUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/16
 * 文件描述：我的设置主页
 */
@FmyContentView(R.layout.activity_setting)
public class MySettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MySettingActivity.class.getSimpleName();
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.version_num)
    private TextView versionNumTextView;
    @FmyViewView(R.id.check_update)
    private RelativeLayout checkUpdateRelativeLayout;
    @FmyViewView(R.id.clear_cache)
    private RelativeLayout clearCacheRelatvieLayout;
    @FmyViewView(R.id.account_safe)
    private RelativeLayout accountSafeRelativeLayout;
    @FmyViewView(R.id.exit)
    private TextView exitTextView;

    private MyCacheManager myCacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式

        titleTextView.setText(R.string.setting);
        versionNumTextView.setText(getString(R.string.version_num, AppContext.getVersionName()));

        myCacheManager = new MyCacheManager(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                LogUtils.i("msg type=" + msg.what);
                switch (msg.what) {
                    case MyCacheManager.MyMessageType.CLEAR_CACHE_SUCCESS:
                        String clearSize = (String) (msg.obj);
                        ToastUtils.showToast(MySettingActivity.this, getString(R.string.clear_how_much_cache, clearSize));
                        break;
                    case MyCacheManager.MyMessageType.CLEAR_CACHE_FAIL:
                        ToastUtils.showToast(MySettingActivity.this, R.string.clear_cache_fail);
                        break;
                    default:
                        break;
                }
            }
        }, this);
    }

    @FmyClickView({R.id.head_back, R.id.check_update, R.id.clear_cache, R.id.account_safe, R.id.exit})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.check_update:
                doCheckUpdate();
                break;
            case R.id.clear_cache:
                doClearCache();
                break;
            case R.id.account_safe:
                doAccountSafe();
                break;
            case R.id.exit:
               Dialog dialog= DialogUtil.showTextDialogCenter(this, getString(R.string.confirm_quit), getResources().getString(R.string.cancel), getResources().getString(R.string.ok), new DialogUtil.CommonDialogListener() {
                    @Override
                    public void onClickFirst() {
                        LogUtils.i("do nothing");
                    }

                    @Override
                    public void onClickSecond() {
                        LogUtils.i("quit account");
                        SPUtils spUtils = new SPUtils(MySettingActivity.this, SPUtils.CACHE_ACCESSTOKEN_INFO);
                        spUtils.remove(MySettingActivity.this, AppConfig.AccessTokenSPKey.AccessToken);
                        SPUtils tokenSpUtils = new SPUtils(AppContext.getInstance().getApplicationContext(), SPUtils.CACHE_ACCESSTOKEN_INFO);
                        tokenSpUtils.remove(AppContext.getInstance().getApplicationContext(), AppConfig.AccessTokenSPKey.LoginId);
                        appContext.isGuest = true;
                        appContext.startActivity(MySettingActivity.this,MainActivity.class,null);
                        onBackPressed();
                    }

                    @Override
                    public void onClickThird() {

                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void doCheckUpdate() {
        Beta.checkUpgrade();
    }

    private void doClearCache() {
        myCacheManager.startClearAppCache();
    }

    private void doAccountSafe() {
        appContext.startActivity(this, AccountSafeActivity.class, null);
    }
}
