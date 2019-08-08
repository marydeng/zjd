package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.CardAdapter;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.fragment.MyFragment;
import poll.com.zjd.model.CardBean;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/15
 * 文件描述：我的主页-余额
 */
@FmyContentView(R.layout.activity_my_balance)
public class MyBalanceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MyBalanceActivity.class.getSimpleName();
    public static final String EXTRA_MY_BALANCE_VALUE = "extra.my.balance.value";
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.head_text_right)
    private TextView detailTextView;
    @FmyViewView((R.id.my_balance))
    private TextView myBalanceTextView;
    @FmyViewView(R.id.recycler_view)
    private RecyclerView recyclerView;
    @FmyViewView(R.id.balance_charge)
    private TextView balanceChargeTextView;
    @FmyViewView(R.id.top_linear_layout)
    private LinearLayout topLinearLayout;
    @FmyViewView(R.id.balance_highest_giving)
    private TextView highestGivingTextView;

    private CardAdapter superAdapter;

    private HttpRequestDao httpRequestDao;

    private List<CardBean> cardBeanList;
    private String myBalanceValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleTextView.setText(getResources().getString(R.string.my_balance));
        detailTextView.setText(getResources().getString(R.string.my_balance_detail));


        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                myBalanceValue = bundle.getString(EXTRA_MY_BALANCE_VALUE);
            }
        }

        myBalanceTextView.setText(getResources().getString(R.string.monkey_currency_symbol, myBalanceValue));
        titleBarManager.setTransparentStatusBar(topLinearLayout);


        httpRequestDao = new HttpRequestDao();
        cardBeanList = new ArrayList<>();

        superAdapter = new CardAdapter(this, cardBeanList, R.layout.item_value_card);
        recyclerView.setAdapter(superAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        httpRequestDao.getRechargeList(this, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                cardBeanList.addAll(JSON.parseArray(result, CardBean.class));
                superAdapter.notifyDataSetHasChanged();

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
                    DecimalFormat decimalFormat=new DecimalFormat("#.#");
                    highestGivingTextView.setText(getString(R.string.highest_giving_money, decimalFormat.format(gitMostCardBean.getGiftAmount())));
                }

            }
        });
    }



    @FmyClickView({R.id.head_back, R.id.head_text_right, R.id.balance_charge})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getid=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.head_text_right:
                appContext.startActivity(this, MyBalanceHistoryActivity.class, null);
                break;
            case R.id.balance_charge:
                doCharge();
                break;
            default:
                break;
        }

    }

    private void doCharge() {
        Bundle bundle = new Bundle();
        bundle.putString(MyRechargeActivity.EXTRA_CARD_BEAN, JSON.toJSONString(superAdapter.getItem(superAdapter.getSelectedPosition())));
        appContext.startActivityForResult(this, MyRechargeActivity.class,0, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == MyFragment.ResultCode.backFromRecharge){
            setResult(MyFragment.ResultCode.backFromRecharge);
            this.finish();
        }
    }
}
