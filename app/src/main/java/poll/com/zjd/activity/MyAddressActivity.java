package poll.com.zjd.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.ArrayList;
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
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.view.LoadMoreListView;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/18
 * 文件描述：my-我的地址列表页面
 */
@FmyContentView(R.layout.activity_my_address)
public class MyAddressActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MyAddressActivity.class.getSimpleName();
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
    /**
     * 选择为默认地址的位置
     */
    private AddressBean defaultAddressBean;

    private HttpRequestDao httpRequestDao;
    // 当前查询到的页数
    private int currentPageIndex = 1;
    //是下拉刷新还是上拉加载
    private boolean isRefreshData;

    public interface ResultCode {
        int add_address_success = 1;
        int update_address_success = 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式

        titleTextView.setText(R.string.my_address);
        newAddressTextView.setText(R.string.add_address);

        httpRequestDao = new HttpRequestDao();

        List<AddressBean> addressBeanList = new ArrayList<>();
        superAdapter = new SuperAdapter<AddressBean>(MyAddressActivity.this, addressBeanList, R.layout.item_my_address) {
            @Override
            public void onBind(final SuperViewHolder holder, int viewType, final int layoutPosition, final AddressBean item) {
                holder.setText(R.id.text_view_recipient, item.getReceivingName());
                holder.setText(R.id.address_phone, item.getReceivingMobilePhone());
                String addressStr = item.getProvince() + item.getCity() +item.getDistrict()+ item.getReceivingAddress() + item.getSpecificAddress();
                String indentation = getString(R.string.indentation_address);
                SpannableString spannableString = new SpannableString(indentation + addressStr);
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.transparent)), 0, indentation.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setText(R.id.address_detail, spannableString);
                if (AddressBean.IsDefaultAddress.defaultAddress.equals(item.getIsDefaultAddress())) {
                    holder.getView(R.id.check_default_icon).setSelected(true);
                    defaultAddressBean = item;
                } else {
                    holder.getView(R.id.check_default_icon).setSelected(false);
                }
                holder.setOnClickListener(R.id.set_default_address_rlt, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i(TAG + ",layoutPosition=" + layoutPosition + ",view.getId=" + view.toString());
                        if (!holder.getView(R.id.check_default_icon).isSelected()) {
                            DialogUtil.showProgressDialog(MyAddressActivity.this, null, null);
                            HashMap<String, String> requestParam = new HashMap<String, String>();
                            requestParam.put("id", superAdapter.getItem(layoutPosition).getId());
                            JSONObject jsonObject = JsonUtils.convertJSONObject(requestParam);
                            httpRequestDao.setDefaultAddress(MyAddressActivity.this, jsonObject, new OkGoStringCallback() {
                                @Override
                                public void onSuccessEvent(String result) {
                                    DialogUtil.hideProgressDialog();
                                    item.setIsDefaultAddress(AddressBean.IsDefaultAddress.defaultAddress);
                                    if (null != defaultAddressBean) {
                                        defaultAddressBean.setIsDefaultAddress(AddressBean.IsDefaultAddress.notDefaultAddress);
                                    }
                                    defaultAddressBean = item;
                                    superAdapter.notifyDataSetHasChanged();
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
                holder.setText(R.id.text_view_address_type, AddressUtil.convertAddressTypeName(item.getConcatType()));

                View.OnClickListener editClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i(TAG + ",layoutPosition=" + layoutPosition + ",view.getId=" + view.toString());
                        Bundle bundle = new Bundle();
                        bundle.putString(MyAddressEditActivity.EXTRA_ADDRESS_DATA, JSON.toJSONString(item));
                        appContext.startActivityForResult(MyAddressActivity.this, MyAddressEditActivity.class, 0, bundle);
                    }
                };
                holder.setOnClickListener(R.id.base_info_relative_layout, editClickListener);
                holder.setOnClickListener(R.id.address_edit, editClickListener);
                holder.setOnClickListener(R.id.address_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i(TAG + ",layoutPosition=" + layoutPosition + ",view.getId=" + view.toString());
                        showDeleteConfirmDialog(layoutPosition);
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
        queryAddressList(1);
    }

    private void showDeleteConfirmDialog(final int layoutPosition) {
        final Dialog dialog = DialogUtil.showTextDialogCenter(MyAddressActivity.this, getResources().getString(R.string.confirm_delete_address), getResources().getString(R.string.cancel), getResources().getString(R.string.delete), new DialogUtil.CommonDialogListener() {
            @Override
            public void onClickFirst() {
                LogUtils.i(TAG + ",donothing");
            }

            @Override
            public void onClickSecond() {
                DialogUtil.showProgressDialog(MyAddressActivity.this, null, getResources().getString(R.string.delete_ongoing));
                HashMap<String, String> requestParam = new HashMap<String, String>();
                requestParam.put("id", superAdapter.getItem(layoutPosition).getId());
                JSONObject jsonObject = JsonUtils.convertJSONObject(requestParam);
                httpRequestDao.deleteAddress(MyAddressActivity.this, jsonObject, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        DialogUtil.hideProgressDialog();
                        superAdapter.remove(layoutPosition);
                        superAdapter.notifyDataSetHasChanged();
                        checkNoData();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        DialogUtil.hideProgressDialog();
                    }
                });
            }

            @Override
            public void onClickThird() {

            }
        });
        dialog.show();
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

    @FmyClickView({R.id.head_back, R.id.head_text_right})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.head_text_right:
                appContext.startActivityForResult(this, MyAddressEditActivity.class, 0, null);
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ResultCode.add_address_success || resultCode == ResultCode.update_address_success) {
            //请求数据
            swipeRefreshLayout.setRefreshing(true);
            isRefreshData = true;
            queryAddressList(0);
        }
    }
}
