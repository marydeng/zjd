package poll.com.zjd.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.activity.ChooseCouponsActivity;
import poll.com.zjd.activity.CommitOrderActivity;
import poll.com.zjd.activity.SelectorLocationActivity;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.model.CartAddBean;
import poll.com.zjd.model.CouponsBean;
import poll.com.zjd.model.CouponsListBean;
import poll.com.zjd.model.OrderCheckResponse;
import poll.com.zjd.model.ProductInfoBean;
import poll.com.zjd.model.QuotaActive;
import poll.com.zjd.model.ShoppiingCartBean;
import poll.com.zjd.model.UpdaeProductInfoRequest;
import poll.com.zjd.utils.DateTimeUtils;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.DoubleCalculateUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:58
 * 包名:     poll.com.zjd.fragment
 * 项目名:   zjd
 */

public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener {

    @FmyViewView(R.id.cart_list)
    private ListView mListView;
    @FmyViewView(R.id.coupons_notice_relative_layout)
    private RelativeLayout couponsNoticeRelativeLayout;
    @FmyViewView(R.id.coupons_notice_txt)
    private TextView couponsNoticeTextView;
    @FmyViewView(R.id.location_txt)
    private TextView locationTextView;
    @FmyViewView(R.id.second_kill_notice)
    private RelativeLayout secondKillNotice;
    @FmyViewView(R.id.second_kill_price)
    private TextView secondKillPriceTextView;
    @FmyViewView(R.id.member_price)
    private TextView memberPriceTextView;
    @FmyViewView(R.id.coupons_used)
    private TextView couponsUsedTextView;
    @FmyViewView(R.id.coupons_price)
    private TextView couponsPriceTextView;
    @FmyViewView(R.id.discount_amount)
    private TextView discountAmountTextView;
    @FmyViewView(R.id.real_pay_price)
    private TextView realPayPriceTextView;
    @FmyViewView(R.id.transportation_expense)
    private TextView transportationExpenseTextView;
    @FmyViewView(R.id.total_price)
    private TextView totalPriceTextView;
    @FmyViewView(R.id.gather_goods_relative)
    private RelativeLayout gatherGoodsRelative;
    @FmyViewView(R.id.gather_goods_txt)
    private TextView gatherGoodsTextView;
    @FmyViewView(R.id.select_all_icon)
    private ImageView selectAllIcon;
    @FmyViewView(R.id.view_no_data)
    private LinearLayout noDataView;
    @FmyViewView(R.id.content_view)
    private RelativeLayout contentView;
    @FmyViewView(R.id.no_data_text)
    private TextView noDataText;

    private List<CouponsBean> couponsBeanList;//用户拥有的优惠券
    private List<ShoppiingCartBean> shoppiingCartBeanList;
    private SuperAdapter superAdapter;

    private HttpRequestDao httpRequestDao;

    private QuotaActive quotaActive;
    private double transportationExpense;//邮费
    private CouponsBean couponsBean; //用户选择的优惠券

    public interface ResultCode {
        int commitOrderSuccess = 1;
        int commitOrderFail = 2;
    }

    private DecimalFormat decimalFormat = new DecimalFormat("0.0#");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_shoppingcart);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initView();
//        initData();
    }

    private void initData() {
        noDataText.setText(R.string.no_any_goods);
        if (!ObjectUtils.isEmpty(shoppiingCartBeanList)) {
            queryCoupons();
        }
    }


    private void queryCoupons() {
        httpRequestDao = new HttpRequestDao();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "100");
        map.put("couponState", "1");
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(gContext, null, null);
        httpRequestDao.queryCoupons(gContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                CouponsListBean couponsListBean = gson.fromJson(result, CouponsListBean.class);
                couponsBeanList = couponsListBean.getData();
                if (!ObjectUtils.isEmpty(couponsBeanList)) {
                    Iterator<CouponsBean> it = couponsBeanList.iterator();
                    CouponsBean cb = null;
                    while (it.hasNext()) {
                        cb = it.next();
                        long endTime = DateTimeUtils.getCurDateInt();
                        try {
                            endTime = DateTimeUtils.parse(cb.getUseEndTime(), "yyyy-MM-dd HH:mm").getTime();
                        } catch (Exception e) {
                            LogUtils.e(e.toString());
                        }
                        if (endTime - DateTimeUtils.getCurDateInt() < 0) {
                            it.remove();
                        }
                    }
                }
                ShoppingCartUtil.getInstance().couponsBeanList = couponsBeanList;
                getProductInfo();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
                orderCheck();
            }
        });
    }

    private void getProductInfo() {
        UpdaeProductInfoRequest updaeProductInfoRequest = new UpdaeProductInfoRequest();
        updaeProductInfoRequest.setSubcompanyId(MyLocationManagerUtil.getInstance().getSubCompanyId());
        updaeProductInfoRequest.setProductNos(getProductNoList());
        if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getProvinceNO())) {
            updaeProductInfoRequest.setProvince(MyLocationManagerUtil.getInstance().getProvinceNO());
            if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getCityNo())) {
                updaeProductInfoRequest.setCity(MyLocationManagerUtil.getInstance().getCityNo());
                if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getDistrictNo())) {
                    updaeProductInfoRequest.setArea(MyLocationManagerUtil.getInstance().getDistrictNo());
                    if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getStreetNo())) {
                        updaeProductInfoRequest.setTown(MyLocationManagerUtil.getInstance().getStreetNo());
                    }
                }
            }
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON.toJSONString(updaeProductInfoRequest));
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        DialogUtil.showProgressDialog(gContext, null, null);
        httpRequestDao.updateProdcutInfo(gContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                List<ProductInfoBean> productInfoBeanList = JSON.parseArray(result, ProductInfoBean.class);
                for (ProductInfoBean productInfoBean : productInfoBeanList) {
                    for (ShoppiingCartBean shoppingCartBean : shoppiingCartBeanList) {
                        if (productInfoBean.getProductNo().equals(shoppingCartBean.getCartAddBean().getProductNo())) {
                            CartAddBean cartAddBean = shoppingCartBean.getCartAddBean();
                            cartAddBean.setSalePrice(productInfoBean.getSalePrice());
                            if (productInfoBean.isCessor()) {
                                cartAddBean.setCessor(true);
                                cartAddBean.setCessorPrice(productInfoBean.getCessorPrice());
                            } else {
                                cartAddBean.setCessor(false);
                            }
                            cartAddBean.setStock(productInfoBean.getStock());
                            cartAddBean.setOff_shelves(productInfoBean.getProductState() == ProductInfoBean.ProductState.off_shelves);
                            cartAddBean.setProductName(productInfoBean.getProductName());

                            break;
                        }
                    }
                }
                couponsBean = calculateBestCoupons();
                showCouponsValue(couponsBean);
                orderCheck();
                secondKillNotice.setVisibility(hasSecondKillGoods() ? View.VISIBLE : View.GONE);
                updateSelectAllIcon();
                ShoppingCartUtil.getInstance().sortGoodsList(shoppiingCartBeanList);
                superAdapter.notifyDataSetHasChanged();

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });
    }

    private void orderCheck() {
        String productNos = getProductNos();
        if (StringUtils.isBlank(productNos)) {
            initViewValue();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("productNos", productNos);
        map.put("userName", appContext.bindPhoneNumber);
        if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getProvinceNO())) {
            map.put("province", MyLocationManagerUtil.getInstance().getProvinceNO());
            if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getCityNo())) {
                map.put("city", MyLocationManagerUtil.getInstance().getCityNo());
                if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getDistrictNo())) {
                    map.put("area", MyLocationManagerUtil.getInstance().getDistrictNo());
                    if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getStreetNo())) {
                        map.put("town", MyLocationManagerUtil.getInstance().getStreetNo());
                    }
                }
            }
        }
        map.put("consigneeAddress", MyLocationManagerUtil.getInstance().getAddressName());
        if (null != couponsBean) {
            map.put("couponNumber", couponsBean.getPromotionNo());
        }


        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(gContext, null, null);
        httpRequestDao.orderCheck(gContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                OrderCheckResponse orderCheckResponse = gson.fromJson(result, OrderCheckResponse.class);
                quotaActive = orderCheckResponse.getQuotaActive();
                //邮费
                transportationExpense = orderCheckResponse.getFreightAmount();
                calculatePrice(orderCheckResponse);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
                initViewValue();
            }
        });
    }

    private String getProductNos() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < shoppiingCartBeanList.size(); i++) {
            ShoppiingCartBean cartBean = shoppiingCartBeanList.get(i);
            if (cartBean.isChosen()) {
                stringBuilder.append(cartBean.getCartAddBean().getProductNo());
                stringBuilder.append("-");
                stringBuilder.append(cartBean.getCount());
                if (i != shoppiingCartBeanList.size() - 1) {
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    private List<String> getProductNoList() {
        List<String> productList = new ArrayList<>();
        for (int i = 0; i < shoppiingCartBeanList.size(); i++) {
            ShoppiingCartBean cartBean = shoppiingCartBeanList.get(i);
            productList.add(cartBean.getCartAddBean().getProductNo());
        }
        return productList;
    }


    @Override
    public void onChangeToFront() {
        initView();
        initData();
        superAdapter.notifyDataSetHasChanged();
    }

    private void initView() {
        shoppiingCartBeanList = ShoppingCartUtil.getInstance().getShoppingCartBeanList();
        checkNoData();
        superAdapter = new SuperAdapter<ShoppiingCartBean>(gContext, shoppiingCartBeanList, R.layout.adapter_cart_product_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, final ShoppiingCartBean item) {
                GlideManager.showImageSqu(gContext, item.getCartAddBean().getPicDesc(), (ImageView) holder.findViewById(R.id.product_img), 0, 0);
                holder.setText(R.id.product_name, item.getCartAddBean().getProductName());
                TextView tv = holder.findViewById(R.id.product_original_price);
                tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

                ImageView iv_kill = holder.findViewById(R.id.product_kill);
                if (item.getCartAddBean().isCessor()) {//是否秒杀商品
                    String originPrice = getString(R.string.origin_price_txt, decimalFormat.format(item.getCartAddBean().getPublicPrice()));
                    tv.setText(originPrice);
                    holder.setText(R.id.product_real_price, getString(R.string.second_kill_price_txt, decimalFormat.format(item.getCartAddBean().getCessorPrice())));
                    iv_kill.setVisibility(View.VISIBLE);
                    holder.setImageResource(R.id.product_del, R.drawable.product_del_gray);
                    holder.setImageResource(R.id.product_add, R.drawable.product_add_gray);
                    holder.setEnabled(R.id.product_add, false);
                } else {
                    tv.setText("");
                    holder.setText(R.id.product_real_price, getString(R.string.member_price_txt, decimalFormat.format(item.getCartAddBean().getSalePrice())));
                    iv_kill.setVisibility(View.GONE);
                    holder.setImageResource(R.id.product_del, R.drawable.product_del_red);
                    holder.setImageResource(R.id.product_add, R.drawable.product_add_red);
                    holder.setEnabled(R.id.product_add, true);
                }

                final ImageView iv_check = holder.findViewById(R.id.product_select);
                iv_check.setSelected(item.isChosen());

                if (item.isSoldOut() || item.isOffShelves()) {
                    holder.setVisibility(R.id.mask, View.VISIBLE);
                    holder.setVisibility(R.id.goods_operation_rlt, View.GONE);
                    holder.setVisibility(R.id.product_sellOut, View.VISIBLE);
                    holder.setEnabled(R.id.product_select, false);
                    holder.setTextColor(R.id.product_name, getResources().getColor(R.color.gray_999999));
                    holder.setTextColor(R.id.product_real_price, getResources().getColor(R.color.gray_999999));
                    holder.setTextColor(R.id.product_original_price, getResources().getColor(R.color.gray_999999));

                    if (item.isOffShelves()) {
                        holder.setImageResource(R.id.product_sellOut, R.drawable.off_shelves);
                    } else {
                        holder.setImageResource(R.id.product_sellOut, R.drawable.sell_out);
                    }
                } else {
                    holder.setVisibility(R.id.mask, View.GONE);
                    holder.setVisibility(R.id.goods_operation_rlt, View.VISIBLE);
                    holder.setVisibility(R.id.product_sellOut, View.GONE);
                    holder.setEnabled(R.id.product_select, true);
                    holder.setTextColor(R.id.product_name, getResources().getColor(R.color.gray_333333));
                    holder.setTextColor(R.id.product_real_price, getResources().getColor(R.color.red_e51728));
                    holder.setTextColor(R.id.product_original_price, getResources().getColor(R.color.gray_7C7C7C));

                    holder.setOnClickListener(R.id.product_del, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LogUtils.i("view.getId=" + view.toString());
                            if (item.getCount() == 1) {
                                showDeleteDialog(layoutPosition);
                            } else {
                                ShoppingCartUtil.getInstance().subtractGood(1, item.getCartAddBean());
                                couponsBean = calculateBestCoupons();
                                showCouponsValue(couponsBean);
                                orderCheck();
                                superAdapter.notifyDataSetHasChanged();
                            }
                        }
                    });
                    holder.setOnClickListener(R.id.product_add, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LogUtils.i("view.getId=" + view.toString());
                            if (item.getCount() < item.getStockCount()) {
                                ShoppingCartUtil.getInstance().addGood(1, item.getCartAddBean());
                                superAdapter.notifyDataSetHasChanged();

                                couponsBean = calculateBestCoupons();
                                showCouponsValue(couponsBean);
                                orderCheck();
                            } else {
                                ToastUtils.showToast(R.string.stock_insufficient);
                            }
                        }
                    });


                    holder.setOnClickListener(R.id.product_select_rlt, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            item.setChosen(!item.isChosen());
                            iv_check.setSelected(item.isChosen());
                            updateSelectAllIcon();

                            couponsBean = calculateBestCoupons();
                            showCouponsValue(couponsBean);
                            orderCheck();
                        }
                    });
                    holder.setText(R.id.product_count, String.valueOf(item.getCount()));
                }

                if (item.isChosen() && item.getCartAddBean().getStock() < item.getCount()) {
                    holder.setBackgroundResource(R.id.main, R.drawable.shape_red_line);
                    holder.setVisibility(R.id.product_stock, View.VISIBLE);
                    holder.setText(R.id.product_stock, getString(R.string.stock_num, item.getCartAddBean().getStock()));
                } else {
                    holder.setBackgroundColor(R.id.main, getResources().getColor(android.R.color.transparent));
                    holder.setVisibility(R.id.product_stock, View.GONE);
                }
                holder.setOnClickListener(R.id.item_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.i("v.getId=" + v.toString());
                        showDeleteDialog(layoutPosition);
                    }
                });
            }
        };
        mListView.setAdapter(superAdapter);
        locationTextView.setText(MyLocationManagerUtil.getInstance().getAddressName());


    }

    private void checkNoData() {
        if (ObjectUtils.isEmpty(shoppiingCartBeanList)) {
            noDataView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }
    }

    private void showDeleteDialog(final int layoutPosition) {
        final Dialog dialog = DialogUtil.showTextDialogCenter(gContext, getResources().getString(R.string.confirm_delete_goods), getResources().getString(R.string.cancel), getResources().getString(R.string.delete), new DialogUtil.CommonDialogListener() {
            @Override
            public void onClickFirst() {
                LogUtils.i("do nothing");
            }

            @Override
            public void onClickSecond() {
                LogUtils.i("delete goods");
                ShoppingCartUtil.getInstance().removeGood(shoppiingCartBeanList.get(layoutPosition).getCartAddBean().getProductId());
                superAdapter.remove(layoutPosition);
                superAdapter.notifyDataSetHasChanged();

                couponsBean = calculateBestCoupons();
                showCouponsValue(couponsBean);
                orderCheck();
                updateSelectAllIcon();
                checkNoData();
            }

            @Override
            public void onClickThird() {

            }
        });
        dialog.show();
    }

    @FmyClickView({R.id.button_settlement, R.id.location_shopping_cart, R.id.gather_goods_relative, R.id.cart_selectAll, R.id.coupons_relative_layout})
    @Override
    public void onClick(View view) {
        LogUtils.i(",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.button_settlement:
                if (checkSettleOk()) {
                    Bundle bundle = new Bundle();
                    if (null != couponsBean) {
                        bundle.putString(CommitOrderActivity.EXTRA_SET_COUPONS, JSON.toJSONString(couponsBean));
                    }
                    appContext.startActivityForResult(mActivity, CommitOrderActivity.class, 0, bundle);
                }
                break;
            case R.id.location_shopping_cart:
                appContext.startActivityForResult(mActivity, SelectorLocationActivity.class, HomeFragment.SELECTADDRESS, null);
                break;
            case R.id.gather_goods_relative:
                if (mainActivityCallBack != null) {
                    mainActivityCallBack.switchPage(0);
                }
                break;
            case R.id.cart_selectAll:
                selectAllIcon.setSelected(!selectAllIcon.isSelected());
                for (ShoppiingCartBean cartBean : shoppiingCartBeanList) {
                    cartBean.setChosen(selectAllIcon.isSelected());
                }
                superAdapter.notifyDataSetHasChanged();

                couponsBean = calculateBestCoupons();
                showCouponsValue(couponsBean);
                orderCheck();
                ShoppingCartUtil.getInstance().saveDataSP();
                break;
            case R.id.coupons_relative_layout:
                if (!ObjectUtils.isEmpty(ShoppingCartUtil.getInstance().couponsBeanList)) {
                    Bundle bundle = new Bundle();
                    if (null != couponsBean) {
                        bundle.putString(ChooseCouponsActivity.EXTRA_COUPONS_ID, couponsBean.getPromotionNo());
                    }
                    bundle.putDouble(ChooseCouponsActivity.EXTRA_MEMEBER_AMOUNT, getMemeberAmount());
                    appContext.startActivityForResult(getActivity(), ChooseCouponsActivity.class, 0, bundle);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkSettleOk() {
        int chooseCount = 0;
        if (!ObjectUtils.isEmpty(shoppiingCartBeanList)) {
            for (ShoppiingCartBean cartBean : shoppiingCartBeanList) {
                if (cartBean.isChosen()) {
                    chooseCount++;
                    if (cartBean.getCount() > cartBean.getCartAddBean().getStock()) {
                        ToastUtils.showToast(R.string.stock_count_insufficient);
                        return false;
                    }
                }
            }
        }
        if (chooseCount == 0) {
            ToastUtils.showToast(R.string.cart_no_goods_choose);
        }
        return chooseCount != 0;
    }

    private CouponsBean calculateBestCoupons() {
        double normanGoodsSum = getMemeberAmount();
        CouponsBean bestCoupons = null;
        // 计算最优优惠劵
        if (!ObjectUtils.isEmpty(couponsBeanList)) {
            for (CouponsBean cb : couponsBeanList) {
                long endTime = DateTimeUtils.getCurDateInt();
                try {
                    endTime = DateTimeUtils.parse(cb.getUseEndTime(), "yyyy-MM-dd HH:mm").getTime();
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                if (normanGoodsSum > Double.parseDouble(cb.getLowestPay()) && endTime > DateTimeUtils.getCurDateInt()) {
                    if (null == bestCoupons) {
                        bestCoupons = cb;
                    } else {
                        if (Double.parseDouble(cb.getParValue())>Double.parseDouble(bestCoupons.getParValue()) ) {
                            bestCoupons = cb;
                        }
                    }
                }
            }
        }
        return bestCoupons;
    }

    private void showChooseCouponsValue(CouponsBean couponsBean) {
        if (couponsBean != null) {
            couponsNoticeRelativeLayout.setVisibility(View.GONE);
            couponsNoticeTextView.setText("");
            couponsUsedTextView.setText(R.string.coupons_name_title);
            String p = "-" + getString(R.string.monkey_currency_symbol, couponsBean.getParValue());
            couponsPriceTextView.setText(p);
        } else {
            couponsNoticeRelativeLayout.setVisibility(View.GONE);
            couponsNoticeTextView.setText("");
            couponsUsedTextView.setText(R.string.coupons_name_title);
            couponsPriceTextView.setText("");
        }
    }

    private double getMemeberAmount() {
        double normanGoodsSum = 0.0;
        for (ShoppiingCartBean cartBean : shoppiingCartBeanList) {
            if (cartBean.isChosen()) {
                if (cartBean.getCartAddBean() != null) {
                    if (!cartBean.getCartAddBean().isCessor()) {
                        normanGoodsSum = DoubleCalculateUtil.add(normanGoodsSum, DoubleCalculateUtil.multiply(cartBean.getCartAddBean().getSalePrice(), cartBean.getCount()));
                    }
                }
            }
        }
        return normanGoodsSum;
    }

    private void showCouponsValue(CouponsBean bestCouponsBean) {
        if (null != bestCouponsBean) {
            couponsNoticeRelativeLayout.setVisibility(View.VISIBLE);
            couponsNoticeTextView.setText(getString(R.string.best_coupons_choosed, bestCouponsBean.getParValue()));
            couponsUsedTextView.setText(getString(R.string.coupons_can_use_type, bestCouponsBean.getParValue()));
            String p = "-" + getString(R.string.monkey_currency_symbol, bestCouponsBean.getParValue());
            couponsPriceTextView.setText(p);
        } else {
            couponsNoticeRelativeLayout.setVisibility(View.GONE);
            couponsNoticeTextView.setText("");
            couponsUsedTextView.setText(R.string.no_coupons_can_use);
            couponsPriceTextView.setText("");
        }
    }

    // 是否选中了秒杀商品
    private boolean hasSecondKillGoods() {
        boolean hasSecondKill = false;
        for (ShoppiingCartBean shoppiingCartBean : shoppiingCartBeanList) {
            if (shoppiingCartBean.isChosen()) {
                if (shoppiingCartBean.getCartAddBean().isCessor()) {
                    hasSecondKill = true;
                    break;
                }
            }
        }
        return hasSecondKill;
    }


    private void calculatePrice(OrderCheckResponse orderCheckResponse) {
        double secondKillPriceSum = orderCheckResponse.getSnappingUpAmount();
        double memberPriceSum = orderCheckResponse.getMemberAmount();
        double goodsRealPayPrice = orderCheckResponse.getBuyAmount();
        double allSumMonkey = 0.0;

        if (null != quotaActive && quotaActive.isJoin() && goodsRealPayPrice > quotaActive.getFreeFullAmount()) {
            allSumMonkey = goodsRealPayPrice;

            gatherGoodsRelative.setVisibility(View.GONE);

            transportationExpenseTextView.setText(R.string.free_transportation);
        } else {
            allSumMonkey = DoubleCalculateUtil.add(goodsRealPayPrice, transportationExpense);

            if (null != quotaActive && quotaActive.isJoin()) {
                gatherGoodsRelative.setVisibility(View.VISIBLE);
                gatherGoodsTextView.setText(getString(R.string.gather_goods_for_free_express, decimalFormat.format(quotaActive.getFreeFullAmount()), String.valueOf(quotaActive.getFreeFullBalance())));
            }

            transportationExpenseTextView.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(transportationExpense)));
        }

        secondKillPriceTextView.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(secondKillPriceSum)));
        memberPriceTextView.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(memberPriceSum)));
        String discountStr="-" +getString(R.string.monkey_currency_symbol, decimalFormat.format(orderCheckResponse.getPreferentialAmount()));
        discountAmountTextView.setText(discountStr);
        realPayPriceTextView.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(goodsRealPayPrice)));
        totalPriceTextView.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(allSumMonkey)));
    }

    private void updateSelectAllIcon() {
        boolean isAllSelected = true;
        for (ShoppiingCartBean shoppiingCartBean : shoppiingCartBeanList) {
            if (!shoppiingCartBean.isChosen()) {
                isAllSelected = false;
            }
        }
        selectAllIcon.setSelected(isAllSelected);
    }

    private void initViewValue() {
        secondKillPriceTextView.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        memberPriceTextView.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        realPayPriceTextView.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        String discountStr="-" +getString(R.string.monkey_currency_symbol, String.valueOf(0.0));
        discountAmountTextView.setText(discountStr);
        totalPriceTextView.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        transportationExpenseTextView.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        gatherGoodsRelative.setVisibility(View.GONE);
        couponsNoticeRelativeLayout.setVisibility(View.GONE);
        couponsNoticeTextView.setText("");
        couponsUsedTextView.setText(R.string.no_coupons_can_use);
        couponsPriceTextView.setText("");
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void onLocationChanged() {
        locationTextView.setText(MyLocationManagerUtil.getInstance().getAddressName());
        getProductInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCode.commitOrderSuccess || resultCode == ResultCode.commitOrderFail) {
            initView();
            initData();
            superAdapter.notifyDataSetHasChanged();
        } else if (resultCode == CommitOrderActivity.ResultCode.changeCoupons) {
            if (data != null) {
                String couponsId = data.getStringExtra(CommitOrderActivity.EXTAR_CHANGE_COUPONS);
                if (StringUtils.isBlank(couponsId)) {
                    couponsBean = null;
                } else {
                    for (int i = 0; i < ShoppingCartUtil.getInstance().couponsBeanList.size(); i++) {
                        if (couponsId.equals(ShoppingCartUtil.getInstance().couponsBeanList.get(i).getPromotionNo())) {
                            couponsBean = ShoppingCartUtil.getInstance().couponsBeanList.get(i);
                            break;
                        }
                    }
                }
                showChooseCouponsValue(couponsBean);
                orderCheck();
            }
        }
    }
}
