package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import poll.com.zjd.model.AddressBean;
import poll.com.zjd.model.AddressListBean;
import poll.com.zjd.utils.AddressUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.view.LoadMoreListView;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/23
 * 文件描述：购物车-提交订单-选择收货地址页面
 */
@FmyContentView(R.layout.activity_my_address)
public class OrderChooseAddressActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OrderChooseAddressActivity.class.getSimpleName();
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.head_text_right)
    private TextView newAddressTextView;
    @FmyViewView(R.id.address_list_load_more)
    private LoadMoreListView loadMoreListView;
    @FmyViewView(R.id.address_swipeRefreshLayout)
    private SwipeRefreshLayout swipeRefreshLayout;
    @FmyViewView(R.id.view_no_data)
    private LinearLayout noDataView;

    private SuperAdapter<AddressBean> superAdapter;

    private HttpRequestDao httpRequestDao;
    // 当前查询到的页数
    private int currentPageIndex = 1;
    //是下拉刷新还是上拉加载
    private boolean isRefreshData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式

        titleTextView.setText(R.string.choose_address);
        newAddressTextView.setText(R.string.management);
        httpRequestDao = new HttpRequestDao();


        superAdapter = new SuperAdapter<AddressBean>(this, AddressBean.createAddressList(), R.layout.item_order_choose_address) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final AddressBean item) {
                holder.setText(R.id.text_view_recipient, item.getReceivingName());
                holder.setText(R.id.address_phone, item.getReceivingMobilePhone());
                String indentation = getString(R.string.indentation_address);
                SpannableString spannableString;
                if (AddressBean.IsDefaultAddress.defaultAddress.equals(item.getIsDefaultAddress())) {
                    String address = indentation + item.getProvince() + item.getCity() +item.getDistrict()+ item.getReceivingAddress() + item.getSpecificAddress();
                    String totalStr = address + "  " + getString(R.string.default_address);
                    spannableString = new SpannableString(totalStr);
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.transparent)), 0, indentation.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_e51728)), address.length(), totalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.x22)), address.length(), totalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else {
                    spannableString = new SpannableString(indentation + item.getProvince() + item.getCity() +item.getDistrict()+ item.getReceivingAddress() + item.getSpecificAddress());
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.transparent)), 0, indentation.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.setText(R.id.address_detail, spannableString);
                holder.setText(R.id.text_view_address_type, AddressUtil.convertAddressTypeName(item.getConcatType()));
                holder.setOnClickListener(R.id.order_choose_address_item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i(TAG + "view.getid=" + view.toString());
                        Intent data = new Intent();
                        data.putExtra(CommitOrderActivity.CHANGE_ADDRESS_EXTRA, JSON.toJSONString(item));
                        setResult(CommitOrderActivity.ResultCode.changeAddress, data);
                        finish();
                    }
                });
            }
        };
        loadMoreListView.setAdapter(superAdapter);
        loadMoreListView.setOnRefreshInterface(new LoadMoreListView.OnRefreshInterface() {
            @Override
            public void onLoad() {
                isRefreshData = false;
                queryAddressList(currentPageIndex + 1);
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.orange);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshData = true;
                queryAddressList(0);
            }
        });


        //请求数据
        swipeRefreshLayout.setRefreshing(true);
        isRefreshData = true;
        queryAddressList(0);


    }

    @FmyClickView({R.id.head_back, R.id.head_text_right})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.head_text_right:
                appContext.startActivityForResult(this, MyAddressActivity.class, 0, null);
            default:
                break;
        }
    }

    private void queryAddressList(int page) {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("page", String.valueOf(page));
        JSONObject jsonObject = JsonUtils.convertJSONObject(requestParams);
        httpRequestDao.getAddressList(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                Gson gson = new Gson();
                AddressListBean addressListBean = gson.fromJson(result, AddressListBean.class);
                currentPageIndex = addressListBean.getPageNo();
                List<AddressBean> addressBeanList = addressListBean.getData();

                if (isRefreshData) {
                    swipeRefreshLayout.setRefreshing(false);
                    superAdapter.clear();
                } else {
                    loadMoreListView.refreshComplete();
                }
                if (!ObjectUtils.isEmpty(addressBeanList)) {
                    superAdapter.addAll(addressBeanList);
                }
                superAdapter.notifyDataSetHasChanged();
                loadMoreListView.setEnableLoad(addressListBean.isHasNext());
                if (addressListBean.isHasNext()) {
                    loadMoreListView.reSetText();
                } else {
                    loadMoreListView.refreshNoMore();
                }
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
        if (ObjectUtils.isEmpty(superAdapter.getData())) {
            noDataView.setVisibility(View.VISIBLE);
            loadMoreListView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.GONE);
            loadMoreListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //请求数据
        swipeRefreshLayout.setRefreshing(true);
        isRefreshData = true;
        queryAddressList(0);
    }
}
