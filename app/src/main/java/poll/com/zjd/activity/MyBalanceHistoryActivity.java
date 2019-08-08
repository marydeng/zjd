package poll.com.zjd.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.BalanceBean;
import poll.com.zjd.model.BalanceListBean;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.view.LoadMoreListView;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：账单记录明细
 */
@FmyContentView(R.layout.activity_my_balance_history)
public class MyBalanceHistoryActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MyBalanceHistoryActivity.class.getSimpleName();
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.balance_history_list_view)
    private LoadMoreListView loadMoreListView;

    @FmyViewView(R.id.balance_swipeRefreshLayout)
    private SwipeRefreshLayout swipeRefreshLayout;
    @FmyViewView(R.id.view_no_data)
    private LinearLayout noDataView;
    @FmyViewView(R.id.no_data_text)
    private TextView noDataTxt;

    private SuperAdapter<BalanceBean> listAdapter;

    private HttpRequestDao httpRequestDao;
    //是下拉刷新还是上拉加载
    private boolean isRefreshData;
    // 当前查询到的页数
    private int currentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBarManager.setStatusBarView();  //设置沉浸式
        initData();
    }

    private void initData() {
        titleTextView.setText(R.string.balance_detail);
        noDataTxt.setText(R.string.balance_no_history);

        httpRequestDao = new HttpRequestDao();

        listAdapter = new SuperAdapter<BalanceBean>(this, BalanceBean.createBalanceBeanList(), R.layout.item_balance) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, BalanceBean item) {

                holder.setText(R.id.balance_type, convertTansTypeName(item.getTransType()));
                holder.setText(R.id.balance_date, item.getTransDate());
                holder.setText(R.id.balance_monkey, showMonkey(item.getTransAmt(), item.getTransType()));
                holder.setText(R.id.balance_serial_number, item.getNumberId());
            }
        };

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.orange);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshData = true;
                queryBalanceHistoryList(1);
            }
        });

        loadMoreListView.setAdapter(listAdapter);
        loadMoreListView.setOnRefreshInterface(new LoadMoreListView.OnRefreshInterface() {
            @Override
            public void onLoad() {
                isRefreshData = false;
                queryBalanceHistoryList(currentPageIndex + 1);
            }
        });

        //请求数据
        isRefreshData = true;
        swipeRefreshLayout.setRefreshing(true);
        queryBalanceHistoryList(1);
    }

    private void queryBalanceHistoryList(int page) {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("page", String.valueOf(page));
        JSONObject jsonObject = JsonUtils.convertJSONObject(requestParams);
        httpRequestDao.getBalanceHistory(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                Gson gson = new Gson();
                BalanceListBean balanceListBean = gson.fromJson(result, BalanceListBean.class);
                currentPageIndex = balanceListBean.getPageNo();
                List<BalanceBean> balanceBeanList = balanceListBean.getData();

                if (isRefreshData) {
                    swipeRefreshLayout.setRefreshing(false);
                    listAdapter.clear();
                } else {
                    loadMoreListView.refreshComplete();
                }
                if (!ObjectUtils.isEmpty(balanceBeanList)) {
                    listAdapter.addAll(balanceBeanList);
                }
                listAdapter.notifyDataSetHasChanged();
                loadMoreListView.setEnableLoad(balanceListBean.isHasNext());
                checkNoData();

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (isRefreshData) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    loadMoreListView.refreshComplete();
                }
                checkNoData();
            }
        });
    }

    private void checkNoData() {
        if (ObjectUtils.isEmpty(listAdapter.getData())) {
            noDataView.setVisibility(View.VISIBLE);
            loadMoreListView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.GONE);
            loadMoreListView.setVisibility(View.VISIBLE);
        }
    }

    @FmyClickView({R.id.head_back})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private String convertTansTypeName(int tansType) {
        String tansTypeName;
        if (BalanceBean.TansType.consume == tansType) {
            tansTypeName = getString(R.string.balance_consume);
        } else if (BalanceBean.TansType.recharge == tansType) {
            tansTypeName = getString(R.string.balance_recharge);
        } else if (BalanceBean.TansType.refund == tansType) {
            tansTypeName = getString(R.string.balance_refund);
        }
        else {
            tansTypeName = getString(R.string.balance_recharge_get);
        }
        return tansTypeName;
    }

    private String showMonkey(float tansAmt, int tansType) {
        String monkeyStr;
        if (BalanceBean.TansType.consume == tansType) {
            monkeyStr = "- ¥ " + tansAmt;
        } else {
            monkeyStr = "+ ¥ " + tansAmt;
        }
        return monkeyStr;
    }
}
