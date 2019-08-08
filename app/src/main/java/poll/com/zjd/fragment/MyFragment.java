package poll.com.zjd.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jhworks.library.ImageSelector;
import com.lzy.okgo.model.Response;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.activity.LoginActivity;
import poll.com.zjd.activity.MainActivity;
import poll.com.zjd.activity.MyAddressActivity;
import poll.com.zjd.activity.MyBalanceActivity;
import poll.com.zjd.activity.MyRechargeActivity;
import poll.com.zjd.activity.MySettingActivity;
import poll.com.zjd.activity.WebViewActivity;
import poll.com.zjd.adapter.CardAdapter;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.model.CardBean;
import poll.com.zjd.model.UserInfoBean;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.FileUploadUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.SPUtils;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.CircleImageView;
import poll.com.zjd.wxapi.WXshareUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:58
 * 包名:     poll.com.zjd.fragment
 * 项目名:   zjd
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyFragment.class.getSimpleName();
    private static final int REQUEST_IMAGE = 2;
    public static final String SP_USER_HEAD_ICON = "user_head_icon";
    public static final String SP_USER_HEAD_NAME = "user_head_name";
    public static final String SP_USER_ID = "user_id";

    @FmyViewView(R.id.my_balance_relative_layout)
    private LinearLayout myBalanceLinearLayout;
    @FmyViewView((R.id.setting_relative_layout))
    private RelativeLayout settingRelativeLayout;
    @FmyViewView(R.id.immediate_charge_button)
    private Button immediateChargeButton;
    @FmyViewView(R.id.my_address_relative_layout)
    private RelativeLayout myAddressRelativeLayout;
    @FmyViewView(R.id.my_home_card_recycler_view)
    private RecyclerView recyclerView;
    @FmyViewView(R.id.my_head_login)
    private TextView loginTextView;
    @FmyViewView(R.id.my_head_user_relative_layout)
    private RelativeLayout headUserRelativeLayout;
    @FmyViewView(R.id.my_head_userName)
    private TextView headUserNameTextView;
    @FmyViewView(R.id.my_user_head_icon)
    private CircleImageView userHeadImageView;
    @FmyViewView(R.id.progress_grow_up)
    private ImageView progressGrowUpIcon;
    @FmyViewView(R.id.current_level_txt)
    private TextView currentLevelTxt;
    @FmyViewView(R.id.next_level_txt)
    private TextView nextLevelTxt;
    @FmyViewView(R.id.grow_up_value_txt)
    private TextView growUpValueTxt;
    @FmyViewView(R.id.fra_my_balance)
    private TextView myBalanceTextView;
    @FmyViewView(R.id.fra_my_coupon)
    private TextView myCouponsTextView;
    @FmyViewView(R.id.fra_my_integral)
    private TextView myIntegralTextView;
    @FmyViewView(R.id.my_order)
    private RelativeLayout mMyOrders;
    @FmyViewView(R.id.my_waitingpay)
    private RelativeLayout mWaitingpay;
    @FmyViewView(R.id.my_wsend)
    private RelativeLayout mWsend;
    @FmyViewView(R.id.my_sending)
    private RelativeLayout mSending;
    @FmyViewView(R.id.my_received)
    private RelativeLayout mReceived;
    @FmyViewView(R.id.my_reimbursing)
    private RelativeLayout mReimbursing;
    @FmyViewView(R.id.my_coupons)
    private RelativeLayout mCoupons;
    @FmyViewView(R.id.git_most_value)
    private TextView gitMostValueTxt;
    @FmyViewView(R.id.my_invite)
    private RelativeLayout mInvite;
    @FmyViewView(R.id.get_coupons_value_txt)
    private TextView getCouponsValueTxt;
    @FmyViewView(R.id.my_help)
    private RelativeLayout mHelp;
    @FmyViewView(R.id.my_invoice)
    private RelativeLayout minvoice;

    private CardAdapter cardAdapter;

    private ArrayList<String> mSelectPath;

    private SPUtils spUtils;

    private HttpRequestDao httpRequestDao;

    private List<CardBean> cardBeanList;

    private boolean isFirst = true;

    public interface ResultCode {
        int backFromCouponsList = 1;
        int backFromRecharge = 2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_my);
        initData();
        return view;
    }

    private void initData() {
        httpRequestDao = new HttpRequestDao();
        spUtils = new SPUtils(gContext, SPUtils.CACHE_USER_INFO);
        refreshHead();

        cardBeanList = new ArrayList<>();
        cardAdapter = new CardAdapter(gContext, cardBeanList, R.layout.item_value_card_my_home);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(gContext, LinearLayoutManager.HORIZONTAL, false));
//        String headIconUrl = (String) (spUtils.get(gContext, SP_USER_HEAD_ICON, ""));
//        if (!StringUtils.isBlank(headIconUrl)) {
//            Glide.with(gContext).load(headIconUrl).into(userHeadImageView);
//        }

        httpRequestDao.getRechargeList(gContext, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                cardBeanList.addAll(JSON.parseArray(result, CardBean.class));
                cardAdapter.notifyDataSetHasChanged();

                //计算最高赠送额度
                if (!ObjectUtils.isEmpty(cardBeanList)) {
                    CardBean gitMostCardBean = null;
                    for (CardBean cardBean : cardBeanList) {
                        if (null == gitMostCardBean) {
                            gitMostCardBean = cardBean;
                        } else if (gitMostCardBean.getGiftAmount() < cardBean.getGiftAmount()) {
                            gitMostCardBean = cardBean;
                        }
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    gitMostValueTxt.setText(getString(R.string.highest_giving_money, decimalFormat.format(gitMostCardBean.getGiftAmount())));
                }

            }
        });
    }

    private void queryUserInfo() {
        if (appContext.isGuest) {
            return;
        }
        httpRequestDao.queryUserInfo(gContext, null, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                Gson gson = new Gson();
                UserInfoBean userInfoBean = gson.fromJson(result, UserInfoBean.class);
                headUserNameTextView.setText(userInfoBean.getLogName());
                currentLevelTxt.setText(userInfoBean.getCurrentLevel());
                nextLevelTxt.setText(userInfoBean.getNextLevel());
                int nextLevelValue = Integer.parseInt(userInfoBean.getNextLevelExperience());
                int currentValue = nextLevelValue - Integer.parseInt(userInfoBean.getNextLevelAmplitude());
                String str = currentValue + "/" + userInfoBean.getNextLevelExperience();
                growUpValueTxt.setText(str);
                ViewGroup.LayoutParams layoutParams = progressGrowUpIcon.getLayoutParams();
                layoutParams.width = getResources().getDimensionPixelSize(R.dimen.x44) + currentValue * getResources().getDimensionPixelSize(R.dimen.x250) / nextLevelValue;
                appContext.bindPhoneNumber = userInfoBean.getPhone();
                appContext.bindWxName = userInfoBean.getWxUserName();
                if (userInfoBean.getBalance() > 0) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    myBalanceTextView.setText(decimalFormat.format(userInfoBean.getBalance()));
                } else {
                    myBalanceTextView.setText("0");
                }

                myCouponsTextView.setText(userInfoBean.getCouponNum());
                myIntegralTextView.setText(userInfoBean.getIntegrals());
                if (!StringUtils.isBlank(userInfoBean.getHeadImageUrl())) {
                    GlideManager.showImageDefault(gContext, userInfoBean.getHeadImageUrl(), userHeadImageView, R.drawable.user_head_default, R.drawable.user_head_default);
                }
                appContext.hasPayPassword = UserInfoBean.IsSavePWD.hasPWD == userInfoBean.getIsSavePWD();

                 str = getString(R.string.invite_get_coupons_value, String.valueOf(userInfoBean.getCouponAmount()));
                int start = str.indexOf(String.valueOf(userInfoBean.getCouponAmount()));
                int end = start + String.valueOf(userInfoBean.getCouponAmount()).length();
                SpannableString spannableString = new SpannableString(str);
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_e51728)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                getCouponsValueTxt.setText(spannableString);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    private void refreshHead() {
        if (AppContext.getInstance().isGuest) {
            loginTextView.setVisibility(View.VISIBLE);
            headUserRelativeLayout.setVisibility(View.GONE);
        } else {
            queryUserInfo();
            loginTextView.setVisibility(View.GONE);
            headUserRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void goWeb(String url, String json) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.LOADURL, url);
        if (StringUtils.isNotEmpty(json)) {
            bundle.putString(WebViewActivity.DATA, json);
        }
        appContext.startActivityForResult(mActivity, WebViewActivity.class, MainActivity.GOWHERE, bundle);
    }

    @FmyClickView({R.id.my_invoice,R.id.my_help,R.id.my_invite, R.id.my_coupons, R.id.my_order, R.id.my_waitingpay, R.id.my_wsend, R.id.my_sending, R.id.my_received, R.id.my_reimbursing, R.id.my_head_login, R.id.my_balance_relative_layout, R.id.setting_relative_layout, R.id.immediate_charge_button, R.id.my_address_relative_layout, R.id.my_user_head_icon, R.id.my_head_userName, R.id.my_integral})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.my_invoice:
                goWeb(Urls.INVOICE, null);          //发票申请
                break;
            case R.id.my_help:
                goWeb(Urls.HELP, null);          //客服与帮助
                break;
            case R.id.my_invite:
                goWeb(Urls.INVITE, null);        //邀请有礼
                break;
            case R.id.my_coupons:
                goWeb(Urls.COUPONS, null);       //优惠券
                break;
            case R.id.my_order:
                goWeb(Urls.ORDERS, null);        //我的订单
                break;
            case R.id.my_waitingpay:
                goWeb(Urls.WAITINGPAY, null);    //代付款
                break;
            case R.id.my_wsend:
                goWeb(Urls.WSEND, null);         //代发货
                break;
            case R.id.my_sending:
                goWeb(Urls.SENDING, null);       //配送中
                break;
            case R.id.my_received:
                goWeb(Urls.RECEIVED, null);      //已签收
                break;
            case R.id.my_reimbursing:
                goWeb(Urls.REFUNDED, null);      //退款/售后
                break;
            case R.id.my_head_login:
                appContext.startActivity(gContext, LoginActivity.class, null);
                break;
            case R.id.my_balance_relative_layout://我的余额
                bundle = new Bundle();
                bundle.putString(MyBalanceActivity.EXTRA_MY_BALANCE_VALUE, myBalanceTextView.getText().toString().trim());
                appContext.startActivityForResult(getActivity(), MyBalanceActivity.class,0, bundle);
                break;
            case R.id.my_integral://积分
                if (mainActivityCallBack != null) {
                    mainActivityCallBack.switchPage(2);
                }
                break;
            case R.id.setting_relative_layout:
                appContext.startActivity(gContext, MySettingActivity.class, null);
                break;
            case R.id.immediate_charge_button://充值
                bundle = new Bundle();
                bundle.putString(MyRechargeActivity.EXTRA_CARD_BEAN, JSON.toJSONString(cardAdapter.getItem(cardAdapter.getSelectedPosition())));
                appContext.startActivityForResult(getActivity(), MyRechargeActivity.class,0, bundle);
                break;
            case R.id.my_address_relative_layout:
                appContext.startActivity(gContext, MyAddressActivity.class, null);
                break;
            case R.id.my_head_userName:
                modifyUserName();
                break;
            case R.id.my_user_head_icon:
                // 进入相册选择图标
                pickImage();
                break;
            default:
                break;
        }
    }

    private void modifyUserName() {
        View contentView = LayoutInflater.from(gContext).inflate(R.layout.dialog_edit_name, null);
        final Dialog dialog = DialogUtil.showDialog(gContext, contentView, getResources().getDimensionPixelSize(R.dimen.x540), WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText nameEditText = (EditText) contentView.findViewById(R.id.user_name_edit_text);
        nameEditText.setText(headUserNameTextView.getText().toString().toString());
        nameEditText.setSelection(headUserNameTextView.getText().toString().toString().length());
        TextView saveButton = (TextView) contentView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG + ",view.getId=" + view.toString());
                final String name = nameEditText.getText().toString().trim();
                if (!StringUtils.isBlank(name)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("loginName", name);
                    JSONObject jsonObject = JsonUtils.convertJSONObject(map);
                    DialogUtil.showProgressDialog(gContext, null, null);
                    httpRequestDao.modifyUserInfo(gContext, jsonObject, new OkGoStringCallback() {
                        @Override
                        public void onSuccessEvent(String result) {
                            DialogUtil.hideProgressDialog();
                            dialog.dismiss();
                            headUserNameTextView.setText(name);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            DialogUtil.hideProgressDialog();
                        }
                    });

                }

            }
        });
    }

    @Override
    public void refreshData() {
        refreshHead();
    }

    @Override
    public void onChangeToFront() {
        if (isFirst) {
            isFirst = false;
            return;
        }
        queryUserInfo();
    }

    private void pickImage() {
        ImageSelector selector = ImageSelector.create();
        selector.single();
        selector.origin(mSelectPath)
                .showCamera(false)
                .openCameraOnly(false)
                .count(1)
                .spanCount(3)
                .start(getActivity(), REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                if (!ObjectUtils.isEmpty(mSelectPath)) {
                    spUtils.put(gContext, SP_USER_HEAD_ICON, mSelectPath.get(0));
                    Glide.with(gContext).load(mSelectPath.get(0)).into(userHeadImageView);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileUploadUtil.uploadFile(new File(mSelectPath.get(0)), Urls.MODIFYHEADIMAGE);
                        }
                    });
                    thread.start();
                }
            }
        } else if (resultCode == ResultCode.backFromCouponsList || resultCode == ResultCode.backFromRecharge) {
            queryUserInfo();
        }
    }

}
