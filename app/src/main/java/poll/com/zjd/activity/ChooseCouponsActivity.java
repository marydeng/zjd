package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.model.AddressBean;
import poll.com.zjd.model.CouponsBean;
import poll.com.zjd.utils.DateTimeUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/23
 * 文件描述：选择优惠券页面
 */
@FmyContentView(R.layout.activity_choose_coupons)
public class ChooseCouponsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ChooseCouponsActivity.class.getSimpleName();
    public static final String EXTRA_COUPONS_ID = "extra_coupons_id";
    public static final String EXTRA_MEMEBER_AMOUNT="poll.com.zjd.activity.member.amount";
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.coupons_recycler_view)
    private RecyclerView recyclerView;
    @FmyViewView(R.id.no_coupons_check)
    private ImageView noCouponsCheckImageView;

    private SuperAdapter<CouponsBean> superAdapter;
    /**
     * 选择为默认优惠券选择的位置
     */
    private int checkedPosition = 0;

    private String couponsId;

    private double currentPayAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式
        titleTextView.setText(R.string.coupons_title);

        //取得优惠券
        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                couponsId = bundle.getString(EXTRA_COUPONS_ID);
                currentPayAmount=bundle.getDouble(EXTRA_MEMEBER_AMOUNT);
            }
        }
        if (StringUtils.isBlank(couponsId) || ObjectUtils.isEmpty(ShoppingCartUtil.getInstance().couponsBeanList)) {
            checkedPosition = -1;
        } else {
            for (int i = 0; i < ShoppingCartUtil.getInstance().couponsBeanList.size(); i++) {
                if (couponsId.equals(ShoppingCartUtil.getInstance().couponsBeanList.get(i).getPromotionNo())) {
                    checkedPosition = i;
                    break;
                }
            }
        }

        superAdapter = new SuperAdapter<CouponsBean>(this, ShoppingCartUtil.getInstance().couponsBeanList, R.layout.item_coupons) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final CouponsBean item) {
                String monkeySymbol = getString(R.string.money_symbol);
                final String value = monkeySymbol + item.getParValue();
                SpannableString spannableString = new SpannableString(value);
                spannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.x32)), 0, monkeySymbol.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setText(R.id.coupons_money, spannableString);
                holder.setText(R.id.coupons_name, getString(R.string.coupons_name, item.getParValue()));
                if(Double.parseDouble(item.getLowestPay())>0){
                    holder.setText(R.id.coupons_scope, getString(R.string.coupons_low_limit,item.getLowestPay()));
                }else{
                    holder.setText(R.id.coupons_scope, getString(R.string.coupons_no_limit));
                }

                holder.setText(R.id.coupons_validity, item.getUseEndTime());
                ImageView checkedImageView = holder.getView(R.id.check_image_view);
                checkedImageView.setSelected(layoutPosition == checkedPosition);
                long endTime = DateTimeUtils.getCurDateInt();
                try {
                    endTime = DateTimeUtils.parse(item.getUseEndTime(), "yyyy-MM-dd HH:mm").getTime();
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                if (endTime - DateTimeUtils.getCurDateInt() < 24 * 3600 * 1000L) {
                    holder.setVisibility(R.id.overdue_notice_txt, View.VISIBLE);
                } else {
                    holder.setVisibility(R.id.overdue_notice_txt, View.GONE);
                }
                holder.setOnClickListener(R.id.coupons_relative_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i(TAG + ",view.getId=" + view.toString());
                        if (!view.isSelected()) {
                            if (Double.parseDouble(item.getLowestPay()) > currentPayAmount) {
                                ToastUtils.showToast(getString(R.string.no_reach_lowest_pay_account,item.getLowestPay()));
                            } else {
                                checkedPosition = layoutPosition;
                                superAdapter.notifyDataSetHasChanged();
                                noCouponsCheckImageView.setSelected(false);
                                couponsId = item.getPromotionNo();
                                setDataBack();
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(superAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @FmyClickView({R.id.head_back, R.id.no_coupons_linear_layout})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.no_coupons_linear_layout:
                if (!noCouponsCheckImageView.isSelected()) {
                    noCouponsCheckImageView.setSelected(true);
                    checkedPosition = -1;
                    superAdapter.notifyDataSetHasChanged();
                    couponsId = null;
                    setDataBack();
                }
                break;
            default:
                break;
        }
    }

    private void setDataBack() {
        Intent intent = new Intent();
        intent.putExtra(CommitOrderActivity.EXTAR_CHANGE_COUPONS, couponsId);
        setResult(CommitOrderActivity.ResultCode.changeCoupons, intent);
        finish();
    }

    @Override
    public synchronized void onBackPressed() {
        super.onBackPressed();
    }

}
