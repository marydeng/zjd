package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.OnItemClickListener;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.fragment.ShoppingCartFragment;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.model.AddressBean;
import poll.com.zjd.model.AddressListBean;
import poll.com.zjd.model.CartAddBean;
import poll.com.zjd.model.CouponsBean;
import poll.com.zjd.model.CreateOrderResponse;
import poll.com.zjd.model.CreatePayInfoResponse;
import poll.com.zjd.model.GetSubcompanyIdResponse;
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
import poll.com.zjd.view.ClearEditText;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/24
 * 文件描述：提交订单页面
 */
@FmyContentView(R.layout.activity_commit_order)
public class CommitOrderActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = CommitOrderActivity.class.getSimpleName();
    public static final String CHANGE_ADDRESS_EXTRA = "change.address";
    public static final String EXTRA_SET_COUPONS = "coupons.set";
    public static final String EXTAR_CHANGE_COUPONS = "coupons.change";
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.express_tips)
    private TextView expressTipsTxt;
    @FmyViewView(R.id.choose_address)
    private LinearLayout chooseAddressLint;
    @FmyViewView(R.id.relative_layout_address)
    private RelativeLayout defaultAddressRlt;
    @FmyViewView(R.id.consignee_text_view)
    private TextView consigneeTextView;
    @FmyViewView(R.id.address_text_view)
    private TextView addressTextView;
    @FmyViewView(R.id.phone_text_view)
    private TextView phoneTextView;
    @FmyViewView(R.id.goods_list_recycler_view)
    private RecyclerView recyclerView;
    @FmyViewView(R.id.goods_count_text_view)
    private TextView goodsCountTextView;
    @FmyViewView(R.id.relative_layout_address)
    private RelativeLayout goodsListRelativeLayout;
    @FmyViewView(R.id.delivery_mode)
    private TextView deliveryModeText;
    @FmyViewView(R.id.order_note_edit_txt)
    private ClearEditText orderNoteEditTxt;
    @FmyViewView(R.id.second_kill_price)
    private TextView secondKillPriceTxt;
    @FmyViewView(R.id.member_price)
    private TextView memberPriceTxt;
    @FmyViewView(R.id.coupons_name)
    private TextView couponsNameTxt;
    @FmyViewView(R.id.coupons_value)
    private TextView couponsValueTxt;
    @FmyViewView(R.id.commit_discount_amount)
    private TextView discountAmountTextView;
    @FmyViewView(R.id.price_real_pay)
    private TextView realPayPriceTxt;
    @FmyViewView(R.id.transportation_expense)
    private TextView transportationExpenseTxt;
    @FmyViewView(R.id.sum_price)
    private TextView sumPriceTxt;
    @FmyViewView(R.id.sum_price_bottom)
    private TextView sumPriceBottomTxt;
    @FmyViewView(R.id.go_to_pay_txt)
    private TextView goToPayTxt;
    @FmyViewView(R.id.commit_coupon)
    private LinearLayout mCommitCoupon;

    private SuperAdapter<ShoppiingCartBean> superAdapter;
    private List<ShoppiingCartBean> shoppiingCartBeanList;
    private HttpRequestDao httpRequestDao;
    private AddressBean defaultAddressBean;
    private boolean hasDefaultAddress;
    /**
     * 邮费
     */
    private double transportationExpense = 0.0;
    private QuotaActive quotaActive;
    /**
     * 实付金额
     */
    private double allSumMonkey = 0.0;

    public interface ResultCode {
        int changeAddress = 1;
        int changeCoupons = 3;
    }

    private CouponsBean couponsBean; //用户选择的优惠券

    private String subCompanyId;//运营点id

    private DecimalFormat decimalFormat = new DecimalFormat("0.0#");

    // 两小时达配送开始时间
    private String deliverStartTime;
    // 两小时达配送结束时间
    private String deliverEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }


    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式
        titleTextView.setText(R.string.commit_order);

        //取得优惠券
        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                String jsonStr = bundle.getString(EXTRA_SET_COUPONS);
                if (null != jsonStr) {
                    couponsBean = JSON.parseObject(jsonStr, CouponsBean.class);
                }
            }
        }

        shoppiingCartBeanList = ShoppingCartUtil.getInstance().getChooseBuyList();
        superAdapter = new SuperAdapter<ShoppiingCartBean>(this, shoppiingCartBeanList, R.layout.item_goods_order) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, ShoppiingCartBean item) {
                String startUrl = item.getCartAddBean().getPicSmall();
                if (!StringUtils.isBlank(startUrl)) {
                    GlideManager.showImage(mContext, startUrl, (ImageView) holder.findViewById(R.id.good_poster), 0, R.drawable.splash);
                }

                //是否售光
                holder.setVisibility(R.id.sold_out_text, (item.isSoldOut() || item.isOffShelves()) ? View.VISIBLE : View.GONE);
            }
        };
        superAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                appContext.startActivity(CommitOrderActivity.this, OrderGoodsListActivity.class, null);
            }
        });
        recyclerView.setAdapter(superAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        goodsCountTextView.setText(getString(R.string.goods_num, String.valueOf(ShoppingCartUtil.getInstance().getGoodsNum(superAdapter.getData()))));

        httpRequestDao = new HttpRequestDao();
        getDefaultAddress();
    }



    private void getDefaultAddress() {
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("page", String.valueOf(1));
        requestParams.put("pageSize", "100");
        JSONObject jsonObject = JsonUtils.convertJSONObject(requestParams);
        DialogUtil.showProgressDialog(CommitOrderActivity.this, null, null);
        httpRequestDao.getAddressList(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                AddressListBean addressListBean = gson.fromJson(result, AddressListBean.class);
                List<AddressBean> addressBeanList = addressListBean.getData();

                for (AddressBean addressBean : addressBeanList) {
                    if (AddressBean.IsDefaultAddress.defaultAddress.equals(addressBean.getIsDefaultAddress())) {
                        defaultAddressBean = addressBean;
                        break;
                    }
                }
                if (null != defaultAddressBean) {
                    hasDefaultAddress = true;
                    chooseAddressLint.setVisibility(View.GONE);
                    defaultAddressRlt.setVisibility(View.VISIBLE);
                    consigneeTextView.setText(defaultAddressBean.getReceivingName());
                    String addressStr = defaultAddressBean.getProvince() + defaultAddressBean.getCity() +defaultAddressBean.getDistrict()+ defaultAddressBean.getReceivingAddress() + defaultAddressBean.getSpecificAddress();
                    addressTextView.setText(addressStr);
                    phoneTextView.setText(defaultAddressBean.getReceivingMobilePhone());
                    getSubCompanyId();
                } else {
                    hasDefaultAddress = false;
                    chooseAddressLint.setVisibility(View.VISIBLE);
                    defaultAddressRlt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
                initViewValue();
            }
        });
    }


    private void getSubCompanyId() {
        HashMap<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(defaultAddressBean.getReceivingProvince())) {
            map.put("province", defaultAddressBean.getReceivingProvince());
            if (!StringUtils.isBlank(defaultAddressBean.getReceivingCity())) {
                map.put("city", defaultAddressBean.getReceivingCity());
                if (!StringUtils.isBlank(defaultAddressBean.getReceivingDistrict())) {
                    map.put("area", defaultAddressBean.getReceivingDistrict());
                    if (!StringUtils.isBlank(defaultAddressBean.getReceivingStreet())) {
                        map.put("town", defaultAddressBean.getReceivingStreet());
                    }
                }
            }
        }
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        httpRequestDao.getSubcompayId(AppContext.getInstance().getApplicationContext(), jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                GetSubcompanyIdResponse getSubcompanyIdResponse = gson.fromJson(result, GetSubcompanyIdResponse.class);
                if (null != getSubcompanyIdResponse) {
                    subCompanyId = getSubcompanyIdResponse.getSubcompanyId();
                    deliverStartTime=getSubcompanyIdResponse.getStartTime();
                    deliverEndTime=getSubcompanyIdResponse.getEndTime();
                    getProductInfo();
                }
            }
        });
    }


    private void getProductInfo() {
        UpdaeProductInfoRequest updaeProductInfoRequest = new UpdaeProductInfoRequest();
        updaeProductInfoRequest.setSubcompanyId(subCompanyId);
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
        DialogUtil.showProgressDialog(this, null, null);
        httpRequestDao.updateProdcutInfo(this, jsonObject, new OkGoStringCallback() {
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
//                couponsBean = calculateBestCoupons();
                showCouponsValue(couponsBean);
                orderCheck();
                superAdapter.notifyDataSetHasChanged();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });
    }

    private List<String> getProductNoList() {
        List<String> productList = new ArrayList<>();
        for (int i = 0; i < shoppiingCartBeanList.size(); i++) {
            ShoppiingCartBean cartBean = shoppiingCartBeanList.get(i);
            if (cartBean.isChosen()) {
                productList.add(cartBean.getCartAddBean().getProductNo());
            }
        }
        return productList;
    }

    private void orderCheck() {
        HashMap<String, String> map = new HashMap<>();
        map.put("productNos", getProductNos());
        map.put("userName", defaultAddressBean.getReceivingName());
        if (!StringUtils.isBlank(defaultAddressBean.getReceivingProvince())) {
            map.put("province", defaultAddressBean.getReceivingProvince());
            if (!StringUtils.isBlank(defaultAddressBean.getReceivingCity())) {
                map.put("city", defaultAddressBean.getReceivingCity());
                if (!StringUtils.isBlank(defaultAddressBean.getReceivingDistrict())) {
                    map.put("area", defaultAddressBean.getReceivingDistrict());
                    if (!StringUtils.isBlank(defaultAddressBean.getReceivingStreet())) {
                        map.put("town", defaultAddressBean.getReceivingStreet());
                    }
                }
            }
        }
        map.put("consigneeAddress",defaultAddressBean.getReceivingAddress()+ defaultAddressBean.getSpecificAddress());
        if (couponsBean != null) {
            map.put("couponNumber", couponsBean.getPromotionNo());
        }


        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(CommitOrderActivity.this, null, null);
        httpRequestDao.orderCheck(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                OrderCheckResponse orderCheckResponse = gson.fromJson(result, OrderCheckResponse.class);
                quotaActive = orderCheckResponse.getQuotaActive();
                //邮费
                transportationExpense = orderCheckResponse.getFreightAmount();

                //快递方式
                if (orderCheckResponse.isTwoHoursDelivery()) {
                    expressTipsTxt.setVisibility(View.VISIBLE);
                    String curTimeFormat = DateTimeUtils.getCurDateTime("HH:mm");
                    if (deliverEndTime.compareTo(curTimeFormat) > 0) {
                        expressTipsTxt.setText(getString(R.string.two_hours_arrive,deliverStartTime,deliverEndTime));
                        deliveryModeText.setText(R.string.deliver_mode_two_hour_arrive);
                    } else {
                        expressTipsTxt.setText(getString(R.string.two_hours_arrive_tomorrow,deliverEndTime,deliverStartTime));
                        deliveryModeText.setText(R.string.deliver_mode_two_hour_arrive_tomorrow);
                    }
                } else {
                    expressTipsTxt.setVisibility(View.GONE);
                    deliveryModeText.setText(R.string.deliver_mode_express);
                }
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

    private void createOrder() {
        if (null != defaultAddressBean) {
            HashMap<String, String> map = new HashMap<>();
            map.put("productNos", getProductNos());
            map.put("userName", defaultAddressBean.getReceivingName());
            if (!StringUtils.isBlank(defaultAddressBean.getReceivingProvince())) {
                map.put("province", defaultAddressBean.getReceivingProvince());
                if (!StringUtils.isBlank(defaultAddressBean.getReceivingCity())) {
                    map.put("city", defaultAddressBean.getReceivingCity());
                    if (!StringUtils.isBlank(defaultAddressBean.getReceivingDistrict())) {
                        map.put("area", defaultAddressBean.getReceivingDistrict());
                        if (!StringUtils.isBlank(defaultAddressBean.getReceivingStreet())) {
                            map.put("town", defaultAddressBean.getReceivingStreet());
                        }
                    }
                }
            }
            map.put("consigneeAddress", defaultAddressBean.getReceivingAddress()+ defaultAddressBean.getSpecificAddress());
            if (couponsBean != null) {
                map.put("couponNumber", couponsBean.getPromotionNo());
            }
            if (!StringUtils.isBlank(orderNoteEditTxt.getText().toString().trim())) {
                map.put("message", orderNoteEditTxt.getText().toString().trim());
            }


            JSONObject jsonObject = JsonUtils.convertJSONObject(map);
            DialogUtil.showProgressDialog(CommitOrderActivity.this, null, null);
            httpRequestDao.createOrder(this, jsonObject, new OkGoStringCallback() {
                @Override
                public void onSuccessEvent(String result) {
                    DialogUtil.hideProgressDialog();
                    Gson gson = new Gson();
                    CreateOrderResponse orderCheckResponse = gson.fromJson(result, CreateOrderResponse.class);
                    createPayInfo(orderCheckResponse.getOrderMainNo());
                }

                @Override
                public void onError(Response<String> response) {
                    super.onError(response);
                    DialogUtil.hideProgressDialog();
                    onCommitOrderFail();
                }
            });
        }
    }

    private void onCommitOrderFail() {
        if (null != defaultAddressBean) {
            List<String> cityNoList = new ArrayList<>();
            if (!StringUtils.isBlank(defaultAddressBean.getReceivingProvince())) {
                cityNoList.add(defaultAddressBean.getReceivingProvince());
                if (!StringUtils.isBlank(defaultAddressBean.getReceivingCity())) {
                    cityNoList.add(defaultAddressBean.getReceivingCity());
                    if (!StringUtils.isBlank(defaultAddressBean.getReceivingDistrict())) {
                        cityNoList.add(defaultAddressBean.getReceivingDistrict());
                        if (!StringUtils.isBlank(defaultAddressBean.getReceivingStreet())) {
                            cityNoList.add(defaultAddressBean.getReceivingStreet());
                        }
                    }
                }
            }
            MyLocationManagerUtil.getInstance().update(defaultAddressBean.getReceivingAddress(), cityNoList);
        }
        setResult(ShoppingCartFragment.ResultCode.commitOrderFail);
        finish();
    }

    private void createPayInfo(String orderMainNo) {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderMainNo", orderMainNo);

        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(CommitOrderActivity.this, null, null);
        httpRequestDao.createPayInfo(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                CreatePayInfoResponse createPayInfoResponse = gson.fromJson(result, CreatePayInfoResponse.class);
                Bundle bundle = new Bundle();
                bundle.putString(OrderChoosePayWayActivity.EXTRA_ORDER_ID, createPayInfoResponse.getId());
                bundle.putDouble(OrderChoosePayWayActivity.EXTRA_ORDER_MONEY, createPayInfoResponse.getOrderPayTotalAmont());
                appContext.startActivity(CommitOrderActivity.this, OrderChoosePayWayActivity.class, bundle);

                ShoppingCartUtil.getInstance().removeChooseGoods();
                setResult(ShoppingCartFragment.ResultCode.commitOrderSuccess);
                CommitOrderActivity.this.finish();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
                onCommitOrderFail();
            }
        });
    }

    @FmyClickView({R.id.head_back, R.id.relative_layout_address, R.id.relative_layout_goods_list, R.id.linear_layout_coupons, R.id.go_to_pay_txt, R.id.choose_address, R.id.commit_coupon})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.relative_layout_address:
            case R.id.choose_address:
                appContext.startActivityForResult(this, OrderChooseAddressActivity.class, 0, null);
                break;
            case R.id.relative_layout_goods_list:
                appContext.startActivity(this, OrderGoodsListActivity.class, null);
                break;
            case R.id.linear_layout_coupons:
                if (!ObjectUtils.isEmpty(ShoppingCartUtil.getInstance().couponsBeanList)) {
                    Bundle bundle = new Bundle();
                    if (null != couponsBean) {
                        bundle.putString(ChooseCouponsActivity.EXTRA_COUPONS_ID, couponsBean.getPromotionNo());
                    }
                    bundle.putDouble(ChooseCouponsActivity.EXTRA_MEMEBER_AMOUNT, getMemeberAmount());
                    appContext.startActivityForResult(this, ChooseCouponsActivity.class, 0, bundle);
                }
                break;
            case R.id.go_to_pay_txt:
                createOrder();
                break;
            case R.id.commit_coupon:
                //优惠券说明
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.LOADURL, Urls.COUPONSINSTRUCTION);
                appContext.startActivityForResult(CommitOrderActivity.this, WebViewActivity.class, MainActivity.GOWHERE, bundle);
                break;
            default:
                break;
        }
    }

    private void calculatePrice(OrderCheckResponse orderCheckResponse) {
        double secondKillPriceSum = orderCheckResponse.getSnappingUpAmount();
        double memberPriceSum = orderCheckResponse.getMemberAmount();
        double goodsRealPayPrice = orderCheckResponse.getBuyAmount();
        double allSumMonkey = 0.0;

        if (null != quotaActive && quotaActive.isJoin() && goodsRealPayPrice > quotaActive.getFreeFullAmount()) {
            allSumMonkey = goodsRealPayPrice;
            transportationExpenseTxt.setText(getString(R.string.free_post));
        } else {
            allSumMonkey = DoubleCalculateUtil.add(goodsRealPayPrice, transportationExpense);
            transportationExpenseTxt.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(transportationExpense)));
        }
        secondKillPriceTxt.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(secondKillPriceSum)));
        memberPriceTxt.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(memberPriceSum)));
        realPayPriceTxt.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(goodsRealPayPrice)));
        String discountStr="-" +getString(R.string.monkey_currency_symbol, decimalFormat.format(orderCheckResponse.getPreferentialAmount()));
        discountAmountTextView.setText(discountStr);
        sumPriceBottomTxt.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(allSumMonkey)));
        sumPriceTxt.setText(getString(R.string.monkey_currency_symbol, decimalFormat.format(allSumMonkey)));
    }

    private void initViewValue() {
        secondKillPriceTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        memberPriceTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        realPayPriceTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        String discountStr="-" +getString(R.string.monkey_currency_symbol, String.valueOf(0.0));
        discountAmountTextView.setText(discountStr);
        sumPriceBottomTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        sumPriceTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        transportationExpenseTxt.setText(getString(R.string.monkey_currency_symbol, String.valueOf(0.0)));
        couponsNameTxt.setText(R.string.no_coupons_can_use);
        couponsValueTxt.setText("");
    }




    private CouponsBean calculateBestCoupons() {
        double normanGoodsSum = getMemeberAmount();
        CouponsBean bestCoupons = null;
        // 计算最优优惠劵
        if (!ObjectUtils.isEmpty(ShoppingCartUtil.getInstance().couponsBeanList)) {
            for (CouponsBean cb : ShoppingCartUtil.getInstance().couponsBeanList) {
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
                        if (Double.parseDouble(cb.getParValue())>Double.parseDouble(bestCoupons.getParValue())) {
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
            couponsNameTxt.setText(R.string.coupons_name_title);
            String p = "-" + getString(R.string.monkey_currency_symbol, couponsBean.getParValue());
            couponsValueTxt.setText(p);
        } else {
            couponsNameTxt.setText(R.string.coupons_name_title);
            couponsValueTxt.setText("");
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
            couponsNameTxt.setText(getString(R.string.coupons_can_use_type, bestCouponsBean.getParValue()));
            String p = "-" + getString(R.string.monkey_currency_symbol, bestCouponsBean.getParValue());
            couponsValueTxt.setText(p);
        } else {
            couponsNameTxt.setText(R.string.no_coupons_can_use);
            couponsValueTxt.setText("");
        }
    }

    private void setDefaultAddress(AddressBean addressBean) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", addressBean.getId());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        httpRequestDao.setDefaultAddress(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                hasDefaultAddress = true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ResultCode.changeAddress) {
            if (data != null && null != data.getStringExtra(CHANGE_ADDRESS_EXTRA)) {
                String jsonStr = data.getStringExtra(CHANGE_ADDRESS_EXTRA);
                defaultAddressBean = JSON.parseObject(jsonStr, AddressBean.class);
                if (null != defaultAddressBean) {
                    if (!hasDefaultAddress) {
                        setDefaultAddress(defaultAddressBean);
                    }
                    chooseAddressLint.setVisibility(View.GONE);
                    defaultAddressRlt.setVisibility(View.VISIBLE);
                    consigneeTextView.setText(defaultAddressBean.getReceivingName());
                    String addressStr = defaultAddressBean.getProvince() + defaultAddressBean.getCity()+defaultAddressBean.getDistrict() + defaultAddressBean.getReceivingAddress() + defaultAddressBean.getSpecificAddress();
                    addressTextView.setText(addressStr);
                    phoneTextView.setText(defaultAddressBean.getReceivingMobilePhone());
                    getSubCompanyId();
                }
            }
        } else if (resultCode == ResultCode.changeCoupons) {
            if (data != null) {
                String couponsId = data.getStringExtra(EXTAR_CHANGE_COUPONS);
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
