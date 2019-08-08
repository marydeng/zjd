package poll.com.zjd.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.activity.GoodsDetailActivity;
import poll.com.zjd.activity.KillActivity;
import poll.com.zjd.activity.MainActivity;
import poll.com.zjd.activity.SearchActivity;
import poll.com.zjd.activity.SelectorLocationActivity;
import poll.com.zjd.activity.WebViewActivity;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.manager.UrlSchemeManager;
import poll.com.zjd.model.HomeBean;
import poll.com.zjd.model.HomeClassBean;
import poll.com.zjd.model.HomeHeadAdBean;
import poll.com.zjd.model.HomeSign;
import poll.com.zjd.model.HomeTips;
import poll.com.zjd.model.KillHeadPicBean;
import poll.com.zjd.model.Product;
import poll.com.zjd.model.SignBean;
import poll.com.zjd.model.SignCouponBean;
import poll.com.zjd.utils.BeanUtils;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.AutoViewPager;
import poll.com.zjd.view.GridViewNoScroll;
import poll.com.zjd.view.LoadMoreListView;
import poll.com.zjd.wxapi.WXshareUtils;


/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:58
 * 包名:     poll.com.zjd.fragment
 * 项目名:   zjd
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public static final int SELECTADDRESS = 1;  //选择地址

    @FmyViewView(R.id.fra_one_refresh)
    private SwipeRefreshLayout fraOneRefresh;
    @FmyViewView(R.id.fra_one_list)
    private LoadMoreListView fraOneList;
    @FmyViewView(R.id.hear_search)
    private RelativeLayout mSearch;
    @FmyViewView(R.id.home_share_r)
    private RelativeLayout mShareR;
    @FmyViewView(R.id.home_share_b)
    private Button mShareB;
    @FmyViewView(R.id.home_location)
    private LinearLayout mLocation;
    @FmyViewView(R.id.home_location_text)
    private TextView mLocationText;
    @FmyViewView(R.id.express_mode_image)
    private ImageView expressModeImage;
    @FmyViewView(R.id.home_sign_pic)
    private ImageView mHomeSignPic;
    @FmyViewView(R.id.home_sign_text)
    private TextView mSignText;
    @FmyViewView(R.id.home_sign_coupon)
    private RelativeLayout mSignCoupon;
    @FmyViewView(R.id.home_coupon_yuan)
    private TextView mYuan;
    @FmyViewView(R.id.home_coupon_up)
    private TextView mUp;
    @FmyViewView(R.id.home_coupon_min)
    private TextView mMin;
    @FmyViewView(R.id.home_coupon_time)
    private TextView mTime;

    private SuperAdapter superAdapter;
    private AutoViewPager auto;  //轮播viewPager
    private HttpRequestDao httpRequestDao = new HttpRequestDao();  //网络请求工具类
    private List<HomeHeadAdBean> headAdList = new ArrayList<>();            //首页轮播数据
    private List<HomeTips> TipsList = new ArrayList<>();                    //首页Tips数据
    private List<HomeClassBean> homeClassList = new ArrayList<>();          //首页分类数据
    private List<HomeClassBean> homeClassList1 = new ArrayList<>();         //首页分类数据
    private HomeSign homeSign;                                              //签到图片
    private TextView mTipsText;                                             //TipsTextView
    private boolean isFisrtStartThisView = true;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_one);

        initDate();
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            reloading();
        }catch (Exception e){
            LogUtils.e("首页onStart加载异常");
        }
    }

    //刷新数据
    private synchronized void reloading() {
        fraOneRefresh.setRefreshing(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("subcompanyId", MyLocationManagerUtil.getInstance().getSubCompanyId());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        httpRequestDao.getHomeAllDate(gContext, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                HomeBean homeBean = new Gson().fromJson(result, HomeBean.class);
                superAdapter.getData().clear();
                headAdList.clear();
                homeClassList.clear();
                homeClassList1.clear();
                fraOneList.reSetText();
                TipsList.clear();
                makeDate(homeBean);  //整理数据
                LogUtils.e("首页解析成功" + headAdList.size());
                if (mTipsText != null) {
                    if(TipsList.size()>3){
                        List<HomeTips> list = new ArrayList<>();
                        for (int i=0;i<3;i++) {
                            list.add(TipsList.get(i));
                        }
                        TipsList.clear();
                        TipsList.addAll(list);
                    }
                    String tips = "";
                    for (HomeTips tp : TipsList) {
                        tips = tips + tp.getContent() + "   ";
                    }
                    mTipsText.setText(tips);
                }
                if (headAdList.size() > 0 && auto!=null) {
                    LogUtils.e("set----");
                    auto.setImageDates(headAdList, R.drawable.roud_nor, R.drawable.roud_no, AutoViewPager.BOTTOM_RIGHT);
                }
//                if (!ObjectUtils.isEmpty(homeClassList)&&isFisrtStartThisView) {
//                    for (int i = 0; i < 2; i++) {
//                        homeClassList1.add(homeClassList.get(i));
//                    }
//                    superAdapter.setData(homeClassList1);
//                    isFisrtStartThisView = false;
//                }

                superAdapter.notifyDataSetHasChanged();

                fraOneRefresh.setRefreshing(false);

            }

            @Override
            public void onError(Response<String> response) {

            }
        });
    }

    private void makeDate(HomeBean homeBean) {
        //头部的轮播图片
        if (homeBean.row11_site1 != null) {
            for (HomeBean.Row11Site1Bean site2Bean : homeBean.row11_site1) {
                HomeHeadAdBean h = BeanUtils.copyObject(site2Bean, HomeHeadAdBean.class);
                headAdList.add(h);
            }
        }
        //获取签到图片
        if (homeBean.row15_site4 != null && homeBean.row15_site4.size() > 0) {
            homeSign = BeanUtils.copyObject(homeBean.row15_site4.get(0), HomeSign.class);
        }

        //Tips数据List
        if (homeBean.row15_site1 != null && homeBean.row15_site1.size() > 0) {
            for (HomeBean.Row15Site1Bean bean : homeBean.row15_site1) {
                HomeTips homeTips = BeanUtils.copyObject(bean, HomeTips.class);
                TipsList.add(homeTips);
            }
        }
        //设置分类数据
        makeDataInClass(homeBean);

    }

    private void initDate() {

    }

    private void initView() {

        fraOneRefresh.setColorSchemeResources(R.color.red_e51728, R.color.colorPrimaryDark, R.color.orange);
        fraOneRefresh.setRefreshing(true);

        fraOneRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloading();
            }
        });

        //分类
        superAdapter = new SuperAdapter<HomeClassBean>(gContext, homeClassList, R.layout.fragment_one_layout) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final HomeClassBean item) {
                holder.setText(R.id.home_list_text_title, item.getTitle());
                ImageView iv = holder.findViewById(R.id.home_list_title_pic);
                GlideManager.showImageDefault(gContext, item.getAdUrl(),iv , R.drawable.default_img_a, R.drawable.default_img_a);
                GridViewNoScroll grid = holder.findViewById(R.id.home_list_grid);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = item.getClickUrl();
                        if (StringUtils.isNotEmpty(url)){
                            if (url.contains(UrlSchemeManager.URL_HEADER)){
                                UrlSchemeManager.urlAnalysisAction(UrlSchemeManager.FRAGMENT,url);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putString(WebViewActivity.LOADURL, AppConfig.getAppRequestUrl(url));
                                appContext.startActivityForResult(mActivity, WebViewActivity.class, MainActivity.GOWHERE, bundle);
                            }
                        }
                    }
                });
                //点击分类
                holder.findViewById(R.id.home_class).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if (mainActivityCallBack != null) {
                        LogUtils.e("分类"+item.getTitle());
                        ClassFragment.isHomeAction=item.getTitle();
                        mainActivityCallBack.switchPage(1);
                    }
                    }
                });
                grid.setAdapter(new SuperAdapter<Product>(gContext, item.getProductList(), R.layout.fragment_one_layout_grid_item) {
                    @Override
                    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final Product item) {
                        LogUtils.i(new Gson().toJson(item));
                        GlideManager.showImageDefault(gContext, item.getCommodityPicVo().getPicBig(), (ImageView) holder.findViewById(R.id.home_grid_img), 0, 0);
                        holder.setText(R.id.home_grid_name, item.getCommodityName());
                        holder.setText(R.id.home_grid_price, "￥ " + item.getSalePrice());
                        holder.setOnClickListener(R.id.home_product_item, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString(GoodsDetailActivity.GOODS_ID_EXTRA, item.getCommodityId());
                                appContext.startActivity(gContext, GoodsDetailActivity.class, bundle);
                            }
                        });
                    }
                });
            }
        };
        //头部
        final View v = View.inflate(gContext, R.layout.fragment_one_header, null);

        auto = (AutoViewPager) v.findViewById(R.id.home_viewPager);

//        auto.setImageDates(headAdList, R.drawable.roud_nor, R.drawable.roud_no, AutoViewPager.BOTTOM_RIGHT);

        auto.getParent().requestDisallowInterceptTouchEvent(true);
        auto.setOnAutoViePagerClickListener(new AutoViewPager.OnAutoViePagerClickListener() {
            @Override
            public void click(int position) {
                String url = headAdList.get(position).getClickUrl();
                if (StringUtils.isNotEmpty(url)){
                    if (url.contains(UrlSchemeManager.URL_HEADER)){
                        UrlSchemeManager.urlAnalysisAction(UrlSchemeManager.FRAGMENT,url);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebViewActivity.LOADURL, AppConfig.getAppRequestUrl(url));
                        appContext.startActivityForResult(mActivity, WebViewActivity.class, MainActivity.GOWHERE, bundle);
                    }
                }
            }
        });
        //新人专享
        v.findViewById(R.id.head_newer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.LOADURL, Urls.NEWONE);
                appContext.startActivityForResult(mActivity, WebViewActivity.class, MainActivity.GOWHERE, bundle);
            }
        });
        //邀请有礼
        v.findViewById(R.id.head_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.LOADURL, Urls.INVITE);
                appContext.startActivity(gContext, WebViewActivity.class, bundle);
            }
        });
        //每日签到
        v.findViewById(R.id.head_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignCoupon.setVisibility(View.GONE);
                mSignText.setText("");
                mHomeSignPic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                mHomeSignPic.setImageResource(R.drawable.sign_loading);
                httpRequestDao.getSignDetail(mActivity, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {

                        SignBean signBean = new Gson().fromJson(result,SignBean.class);
                        if (signBean.getFlag()==0){
                            mSignText.setText("您今天已经签到");
                            httpRequestDao.getiSSignCoupon(gContext, new OkGoStringCallback() {
                                @Override
                                public void onSuccessEvent(String result) {
                                    SignBean signBean = new Gson().fromJson(result,SignBean.class);
                                    if (signBean.getFlag()==0){
                                        //已经领过券
                                        Glide.with(mActivity).load(R.drawable.sign_done).centerCrop().into(mHomeSignPic);
                                    }else {
                                        if (homeSign != null) {
                                            GlideManager.showImageDefault(gContext, homeSign.getImgUrl(), mHomeSignPic, 0, R.drawable.sign_gift);
                                        }
                                        getCoupon();
                                    }
                                }

                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    mHomeSignPic.setImageResource(R.drawable.sign_error);
                                }
                            });
                        }else {
                            mSignText.setText("签到成功，+5积分");
                            if (homeSign != null) {
                                GlideManager.showImageDefault(gContext, homeSign.getImgUrl(), mHomeSignPic, 0, R.drawable.sign_gift);
                            }
                            getCoupon();
                        }
                        mShareR.setVisibility(View.VISIBLE);  //每次点击都出来
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mHomeSignPic.setImageResource(R.drawable.sign_error);
                    }
                });

            }
        });
        //限时秒杀
        v.findViewById(R.id.head_kill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appContext.startActivity(mActivity, KillActivity.class, null);
            }
        });
        mTipsText = (TextView) v.findViewById(R.id.home_notify_text);
        mTipsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdvertisingDialog();
            }
        });

        fraOneList.addHeaderView(v);
        fraOneList.setAdapter(superAdapter);

        fraOneList.setOnRefreshInterface(new LoadMoreListView.OnRefreshInterface() {
            @Override
            public void onLoad() {
                fraOneList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        List<HomeClassBean> templeList = new ArrayList<>();
//                        if (homeClassList.size() > homeClassList1.size()) {
//                            for (int i = homeClassList1.size(); i < homeClassList.size(); i++) {
//                                templeList.add(homeClassList.get(i));
//                            }
//                        }
//                        if (templeList.size() > 0) {
//                            superAdapter.getData().addAll(templeList);
//                            fraOneList.refreshComplete();
//                        } else {
//                            fraOneList.refreshNoMore();
//                        }
                        fraOneList.refreshNoMore();
                    }
                }, 600);
            }
        });

        mLocationText.setText(MyLocationManagerUtil.getInstance().getAddressName());
    }

    private void getCoupon(){
        //点击领券
        mHomeSignPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpRequestDao.getSignCoupon(mActivity, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        mSignText.setText("您今天已经签到");
                        SignCouponBean signCouponBean = new Gson().fromJson(result,SignCouponBean.class);
                        if (signCouponBean.getFlag()==1){
                            Glide.with(mActivity).load(R.drawable.sign_coupon).centerCrop().into(mHomeSignPic);

                            mYuan.setText("￥"+ signCouponBean.getAmount());
                            mUp.setText(signCouponBean.getAmount() + "元代金券");
                            mMin.setText(signCouponBean.getRule());
                            mTime.setText("有效期至:"+signCouponBean.getEndTime());

                            mSignCoupon.setVisibility(View.VISIBLE);
                            mHomeSignPic.setEnabled(false);
                        }else{
                            mHomeSignPic.setImageResource(R.drawable.sign_error);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mHomeSignPic.setImageResource(R.drawable.sign_error);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (auto != null && auto.getLoopStatus()) {
            auto.stopAutoViewPager();
        }
    }


    @FmyClickView({R.id.home_share_r, R.id.home_share_b, R.id.hear_search, R.id.home_location,R.id.sign_close_icon})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_share_r:
            case R.id.sign_close_icon:
                mShareR.setVisibility(View.GONE);
                break;
            case R.id.home_share_b:
                if(AppContext.getInstance().isLogin()){
                    ShareUtils.shareDialog(mActivity, res, String.format(WXshareUtils.addFriendTitle,AppContext.getUserName(mActivity)),
                            WXshareUtils.addFriendDescription,String.format(WXshareUtils.addFriendUrl,AppContext.getUserId(mActivity)),null);
                }else {
                    ToastUtils.showToast(mActivity,res.getString(R.string.login_notExistError),1);
                }
                break;
            case R.id.hear_search:
                appContext.startActivity(mActivity, SearchActivity.class, null);
                break;
            case R.id.home_location:
                appContext.startActivityForResult(mActivity, SelectorLocationActivity.class, SELECTADDRESS, null);
                break;
            default:
                break;
        }
    }


    private void showAdvertisingDialog() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_advertising, null);
        ImageView closeImageView = (ImageView) contentView.findViewById(R.id.close_advertising);

        final Dialog dialog = DialogUtil.showDialog(mActivity, contentView, getResources().getDimensionPixelSize(R.dimen.x540), WindowManager.LayoutParams.WRAP_CONTENT);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i("view.getId=" + view.getId());
                dialog.dismiss();
            }
        });

        ListView mListView = (ListView) contentView.findViewById(R.id.home_Tips);

        SuperAdapter superAdapter = new SuperAdapter<HomeTips>(gContext, TipsList, R.layout.adapter_location_bd_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, HomeTips item) {

                holder.setText(R.id.location_bd_text, item.getContent());

            }
        };

        mListView.setAdapter(superAdapter);
    }

    @Override
    public void onLocationChanged() {
        if (null != mLocationText)
            mLocationText.setText(MyLocationManagerUtil.getInstance().getAddressName());
        if(null!=expressModeImage){
            expressModeImage.setImageResource(MyLocationManagerUtil.getInstance().isTwoHoursDelivery()?R.drawable.one_two_hours:R.drawable.express_delivery);
        }

        reloading();
    }

    @Override
    public void onChangeToFront() {
        if (null != mLocationText)
            mLocationText.setText(MyLocationManagerUtil.getInstance().getAddressName());
        if(null!=expressModeImage){
            expressModeImage.setImageResource(MyLocationManagerUtil.getInstance().isTwoHoursDelivery()?R.drawable.one_two_hours:R.drawable.express_delivery);
        }
        reloading();
    }

    private void makeDataInClass(HomeBean homeBean){
        //拿分类
        try {
            //拿分类1
            HomeClassBean class1 = new HomeClassBean();
            class1.setTitle(homeBean.row20_site4 != null ? homeBean.row20_site4.get(0).keywordName : "");
            class1.setAdUrl(homeBean.row20_site2 != null ? homeBean.row20_site2.get(0).imgUrl : "");
            class1.setClickUrl(homeBean.row20_site2 != null ? homeBean.row20_site2.get(0).clickUrl : "");
            List<Product> p1 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row20_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p1.add(product);
                }
            }
            class1.setProductList(p1);
            homeClassList.add(class1);
        }catch (Exception e) {

        }

        try {
            //拿分类2
            HomeClassBean class2 = new HomeClassBean();
            class2.setTitle(homeBean.row21_site4 != null ? homeBean.row21_site4.get(0).keywordName : "");
            class2.setAdUrl(homeBean.row21_site2 != null ? homeBean.row21_site2.get(0).imgUrl : "");
            class2.setClickUrl(homeBean.row21_site2 != null ? homeBean.row21_site2.get(0).clickUrl : "");
            List<Product> p2 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row21_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p2.add(product);
                }
            }
            class2.setProductList(p2);
            homeClassList.add(class2);
        }catch (Exception e) {

        }
        try {
            //拿分类3
            HomeClassBean class3 = new HomeClassBean();
            class3.setTitle(homeBean.row22_site4 != null ? homeBean.row22_site4.get(0).keywordName : "");
            class3.setAdUrl(homeBean.row22_site2 != null ? homeBean.row22_site2.get(0).imgUrl : "");
            class3.setClickUrl(homeBean.row22_site2 != null ? homeBean.row22_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row22_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class3.setProductList(p3);
            homeClassList.add(class3);

        }catch (Exception e){

        }

        try {
            //拿分类4
            HomeClassBean class4 = new HomeClassBean();
            class4.setTitle(homeBean.row23_site4 != null ? homeBean.row23_site4.get(0).keywordName : "");
            class4.setAdUrl(homeBean.row23_site2 != null ? homeBean.row23_site2.get(0).imgUrl : "");
            class4.setClickUrl(homeBean.row23_site2 != null ? homeBean.row23_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row23_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class4.setProductList(p3);
            homeClassList.add(class4);

        }catch (Exception e){

        }

        try {
            //拿分类5
            HomeClassBean class5 = new HomeClassBean();
            class5.setTitle(homeBean.row24_site4 != null ? homeBean.row24_site4.get(0).keywordName : "");
            class5.setAdUrl(homeBean.row24_site2 != null ? homeBean.row24_site2.get(0).imgUrl : "");
            class5.setClickUrl(homeBean.row24_site2 != null ? homeBean.row24_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row24_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class5.setProductList(p3);
            homeClassList.add(class5);

        }catch (Exception e){

        }

        try {
            //拿分类6
            HomeClassBean class6 = new HomeClassBean();
            class6.setTitle(homeBean.row25_site4 != null ? homeBean.row25_site4.get(0).keywordName : "");
            class6.setAdUrl(homeBean.row25_site2 != null ? homeBean.row25_site2.get(0).imgUrl : "");
            class6.setClickUrl(homeBean.row25_site2 != null ? homeBean.row25_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row25_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class6.setProductList(p3);
            homeClassList.add(class6);

        }catch (Exception e){

        }

        try {
            //拿分类7
            HomeClassBean class6 = new HomeClassBean();
            class6.setTitle(homeBean.row26_site4 != null ? homeBean.row26_site4.get(0).keywordName : "");
            class6.setAdUrl(homeBean.row26_site2 != null ? homeBean.row26_site2.get(0).imgUrl : "");
            class6.setClickUrl(homeBean.row26_site2 != null ? homeBean.row26_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row26_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class6.setProductList(p3);
            homeClassList.add(class6);

        }catch (Exception e){

        }

        try {
            //拿分类8
            HomeClassBean class6 = new HomeClassBean();
            class6.setTitle(homeBean.row27_site4 != null ? homeBean.row27_site4.get(0).keywordName : "");
            class6.setAdUrl(homeBean.row27_site2 != null ? homeBean.row27_site2.get(0).imgUrl : "");
            class6.setClickUrl(homeBean.row27_site2 != null ? homeBean.row27_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row27_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class6.setProductList(p3);
            homeClassList.add(class6);

        }catch (Exception e){

        }

        try {
            //拿分类9
            HomeClassBean class6 = new HomeClassBean();
            class6.setTitle(homeBean.row28_site4 != null ? homeBean.row28_site4.get(0).keywordName : "");
            class6.setAdUrl(homeBean.row28_site2 != null ? homeBean.row28_site2.get(0).imgUrl : "");
            class6.setClickUrl(homeBean.row28_site2 != null ? homeBean.row28_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row28_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class6.setProductList(p3);
            homeClassList.add(class6);

        }catch (Exception e){

        }

        try {
            //拿分类10
            HomeClassBean class6 = new HomeClassBean();
            class6.setTitle(homeBean.row29_site4 != null ? homeBean.row29_site4.get(0).keywordName : "");
            class6.setAdUrl(homeBean.row29_site2 != null ? homeBean.row29_site2.get(0).imgUrl : "");
            class6.setClickUrl(homeBean.row29_site2 != null ? homeBean.row29_site2.get(0).clickUrl : "");
            List<Product> p3 = new ArrayList<>();
            for (HomeBean.Row20Site3Bean site3Bean : homeBean.row29_site3) {
                Product product = BeanUtils.copyObject(site3Bean, Product.class);
                if (product != null) {
                    product.setCommodityPicVo(BeanUtils.copyObject(site3Bean.commodityPicVo, Product.CommodityPicVoBean.class));
                    p3.add(product);
                }
            }
            class6.setProductList(p3);
            homeClassList.add(class6);

        }catch (Exception e){

        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回值为0，visible；返回值为4，invisible；返回值为8，gone
        if (mShareR!=null){
            int status = mShareR.getVisibility();
            switch (status){
                case View.VISIBLE:
                    mShareR.setVisibility(View.GONE);
                    return true;
                case View.GONE:
                    return false;
                case View.INVISIBLE:
                    return false;
                default:
                    break;
            }
        }
        return false;
    }
}
