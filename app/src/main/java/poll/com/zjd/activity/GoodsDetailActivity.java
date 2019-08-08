package poll.com.zjd.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.sina.weibo.sdk.utils.LogUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

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
import poll.com.zjd.api.Urls;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.mini.GoodsBeanMini;
import poll.com.zjd.model.CartAddBean;
import poll.com.zjd.model.ExtendPropList;
import poll.com.zjd.model.GoodsBean;
import poll.com.zjd.model.ProductInfoBean;
import poll.com.zjd.model.UpdaeProductInfoRequest;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.ShareUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.LinearLayoutForListView;
import poll.com.zjd.view.ListViewInScroll;


/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/22
 * 文件描述：商品详情页面
 */

@FmyContentView(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = GoodsDetailActivity.class.getSimpleName();
    public static final String GOODS_ID_EXTRA = "detail.good.id.extra";
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.head_icon_right)
    private ImageView rightHeadIcon;
    @FmyViewView(R.id.description_goods_detail)
    private TextView descriptionTitleTextView;
    @FmyViewView(R.id.specifications_goods_detail)
    private TextView specificationsTitleTextView;
    @FmyViewView(R.id.specifications_recycler_view)
    private ListViewInScroll specificationListViewInScroll;
    @FmyViewView(R.id.ad_txt_detail)
    private TextView adTextView;
    @FmyViewView(R.id.name_txt_detail)
    private TextView prodNameTextView;
    @FmyViewView(R.id.origin_price_txt_detail)
    private TextView originPriceTextView;
    @FmyViewView(R.id.current_price_txt_detail)
    private TextView currentPriceTextView;
    @FmyViewView(R.id.seconds_kill_icon_detail)
    private ImageView secondsKillIcon;
    //    @FmyViewView(R.id.detail_auto_viewPager)
//    private ImageAutoViewPager autoViewPager;
    @FmyViewView(R.id.sold_num_detail)
    private TextView soldNumTextView;
    @FmyViewView(R.id.wine_characteristics_one)
    private TextView wineCharacOneTxt;
    @FmyViewView(R.id.wine_characteristics_two)
    private TextView wineCharacTwoTxt;
    @FmyViewView(R.id.wine_characteristics_three)
    private TextView wineCharacThreeTxt;
    @FmyViewView(R.id.description_recycler_view)
    private LinearLayoutForListView descriptionListViewInScroll;
    @FmyViewView(R.id.cart_num_txt)
    private TextView cartNumTxt;
    @FmyViewView(R.id.goods_detail_root)
    private RelativeLayout goodsDetailRoot;
    @FmyViewView(R.id.add_cart_button)
    private TextView addCartTxt;
    @FmyViewView(R.id.cart_icon)
    private ImageView cartIcon;
    @FmyViewView(R.id.good_banner)
    private Banner mBanner;
    @FmyViewView(R.id.scroll_view)
    private ScrollView scrollView;

    private GoodsBean goodsBean;
    private HttpRequestDao httpRequestDao;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private int stockCount = 0;//库存数量

    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }


    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式
        titleTextView.setText(R.string.goods_detail_title);
        rightHeadIcon.setImageResource(R.drawable.share_icon);
        changeTab(R.id.description_goods_detail);
        originPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        cartNumTxt.setText(String.valueOf(ShoppingCartUtil.getInstance().getGoodsNum()));

        screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

        httpRequestDao = new HttpRequestDao();

        String goodId = null;
        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                goodId = bundle.getString(GOODS_ID_EXTRA);
            }
        }

        if (StringUtils.isBlank(goodId)) {
            LogUtils.w("good id empty");
            this.finish();
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("commodityId", goodId);
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);

        DialogUtil.showProgressDialog(this, null, null);

        httpRequestDao.getGoodDetail(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                try {
                    goodsBean = new Gson().fromJson(result, GoodsBean.class);
                    if (null != goodsBean) {
                        initDataFormGoods();
                    }
                } catch (Exception e) {
                    ToastUtils.showToast(mContext, "解析错误", 1);
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                DialogUtil.hideProgressDialog();
            }
        });

    }

    private void initDataFormGoods() {
        // 设置商品数据
        String adStr = "\"" + GoodsBeanMini.getAlias(goodsBean) + "\"";
        adTextView.setText(adStr);
        prodNameTextView.setText(GoodsBeanMini.getProdName(goodsBean));
        originPriceTextView.setText(getString(R.string.origin_price_txt, String.valueOf(GoodsBeanMini.getPublicPrice(goodsBean))));

        if (!GoodsBeanMini.isSecondKillGoods(goodsBean)) {
            currentPriceTextView.setText(getString(R.string.member_price_txt, String.valueOf(GoodsBeanMini.getSalePrice(goodsBean))));
            secondsKillIcon.setVisibility(View.GONE);
        } else {
            currentPriceTextView.setText(getString(R.string.second_kill_price_txt, String.valueOf(GoodsBeanMini.getKillPrice(goodsBean))));
            secondsKillIcon.setVisibility(View.VISIBLE);
        }
        soldNumTextView.setText(getString(R.string.sold_num, GoodsBeanMini.getSalesCount(goodsBean)));
        wineCharacOneTxt.setText(GoodsBeanMini.getWineCharacteristicOne(goodsBean));
        wineCharacTwoTxt.setText(GoodsBeanMini.getWineCharacteristicTwo(goodsBean));
        wineCharacThreeTxt.setText(GoodsBeanMini.getWineCharacteristicThree(goodsBean));

//        autoViewPager.setImageDates(GoodsBeanMini.getImageUrlList(goodsBean), R.drawable.roud_nor, R.drawable.roud_no, AutoViewPager.BOTTOM_RIGHT);

        showKillHeadPic(mBanner, GoodsBeanMini.getImageUrlList(goodsBean));  //轮播图

        specificationListViewInScroll.setAdapter(new SuperAdapter<ExtendPropList>(this, GoodsBeanMini.getProductSpecifications(goodsBean), R.layout.item_specification) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, ExtendPropList item) {
                holder.setText(R.id.specification_item_txt, item.getPropItemName() + "：" + item.getPropItemValue());
            }
        });
        String url = goodsBean.getGoodsCommodityDetailVo().getProdPropDesc();
        List<String> descriptionUrl = new ArrayList<>();
        if (!StringUtils.isBlank(url)) {
            if (url.endsWith(",")) {
                url = url.substring(0, url.length() - 1);
            }
            String[] arrs = url.split(",");
            if (!ObjectUtils.isEmpty(arrs)) {
                for (String s : arrs) {
                    descriptionUrl.add(s);
                }
            }
        }
        descriptionListViewInScroll.setVisibility(View.GONE);
        descriptionListViewInScroll.setAdapter(new SuperAdapter<String>(this, descriptionUrl, R.layout.item_image_description) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, String item) {
                ImageView posterImage = holder.findViewById(R.id.description_image);
                ViewGroup.LayoutParams lp = posterImage.getLayoutParams();
                lp.width = screenWidth;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                posterImage.setLayoutParams(lp);
                posterImage.setMaxWidth(screenWidth);
                posterImage.setMaxHeight(screenWidth * 5);
                GlideManager.showImageDefaultB(mContext, item, posterImage, 0, R.drawable.default_poster_detail);
            }
        });

        descriptionListViewInScroll.bindLinearLayout();
        descriptionListViewInScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                descriptionListViewInScroll.setVisibility(View.VISIBLE);
            }
        }, 100);

        if (!appContext.isGuest) {
            getProductInfo(GoodsBeanMini.getGoodNo(goodsBean));
        }


    }

    //轮播初始化
    private void showKillHeadPic(Banner banner, List<String> images) {
//        //设置图片加载器
//        banner.setImageLoader(new GlideImageLoader());
//        //设置图片集合
//        banner.setImages(images);
//        //动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
//        //设置自动轮播，默认为true
//        banner.isAutoPlay(true);
//        //设置轮播时间
//        banner.setDelayTime(3000);
//        //设置指示器圆形
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        //banner设置方法全部调用完毕时最后调用
//        banner.start();
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器圆形
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            GlideManager.showImageDefaultA(context, AppConfig.getAppRequestUrl((String) path), imageView, R.drawable.default_img_a, R.drawable.default_img_a);
        }
    }

    private void getProductInfo(String productNO) {
        UpdaeProductInfoRequest updaeProductInfoRequest = new UpdaeProductInfoRequest();
        updaeProductInfoRequest.setSubcompanyId(MyLocationManagerUtil.getInstance().getSubCompanyId());
        List<String> productNoList = new ArrayList<>();
        productNoList.add(productNO);
        updaeProductInfoRequest.setProductNos(productNoList);
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
                if (!ObjectUtils.isEmpty(productInfoBeanList)) {
                    stockCount = productInfoBeanList.get(0).getStock();
                    if(stockCount==-1){
                        addCartTxt.setText(R.string.stop_sale);
                        addCartTxt.setEnabled(false);
                    }else{
                    if (GoodsBeanMini.isSecondKillGoods(goodsBean)) {
                        if (stockCount == 0) {
                            addCartTxt.setText(R.string.stop_sale);
                            addCartTxt.setEnabled(false);
                        } else {
                            int alreadyAddCount = ShoppingCartUtil.getInstance().getCardAddBeanCount(GoodsBeanMini.getGoodId(goodsBean));
                            if (alreadyAddCount > 0) {
                                addCartTxt.setText(R.string.already_add_to_cart);
                                addCartTxt.setEnabled(false);
                            } else {
                                addCartTxt.setText(R.string.add_to_cart);
                                addCartTxt.setEnabled(true);
                            }
                        }
                    } else {
                        if (stockCount == 0) {
                            addCartTxt.setText(R.string.arrival_remind);
                            addCartTxt.setEnabled(true);
                        } else {
                            addCartTxt.setText(R.string.add_to_cart);
                            addCartTxt.setEnabled(true);
                        }
                    }
                }}
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtils.dismissShareDialog();
    }

    @FmyClickView({R.id.head_back, R.id.head_icon_right, R.id.specifications_goods_detail, R.id.description_goods_detail, R.id.add_cart_button, R.id.detail_cart_frame})
    @Override
    public void onClick(View view) {
        LogUtils.i(TAG + ",view.getId=" + view.getId());
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.head_icon_right:

                ShareUtils.shareDialog(GoodsDetailActivity.this, res, GoodsBeanMini.getProdName(goodsBean), GoodsBeanMini.getAlias(goodsBean), String.format(Urls.SHAREPRODUCT, goodsBean.getGoodsCommodityDetailVo().getProdId()), AppConfig.getAppRequestUrl(GoodsBeanMini.getProductDetailUrl(goodsBean)));

                break;
            case R.id.description_goods_detail:
            case R.id.specifications_goods_detail:
                changeTab(view.getId());
                break;
            case R.id.add_cart_button:
                if (appContext.isGuest) {
                    LogUtil.i(TAG, "current is guest,can not add cart");
                    return;
                }
                if (stockCount > 0) {
                    int alreadyAddCount = ShoppingCartUtil.getInstance().getCardAddBeanCount(GoodsBeanMini.getGoodId(goodsBean));
                    if (GoodsBeanMini.isSecondKillGoods(goodsBean) && alreadyAddCount > 0) {
                        ToastUtils.showToast(R.string.second_kill_goods_only_buy_one);
                    } else if (alreadyAddCount >= stockCount) {
                        ToastUtils.showToast(R.string.stock_insufficient);
                    } else {
                        CartAddBean cartAddBean = new CartAddBean();
                        cartAddBean.setProductId(GoodsBeanMini.getGoodId(goodsBean));
                        cartAddBean.setProductNo(GoodsBeanMini.getGoodNo(goodsBean));
                        cartAddBean.setPicDesc(GoodsBeanMini.getProductDetailUrl(goodsBean));
                        cartAddBean.setPicSmall(GoodsBeanMini.getProductSmallPosterUrl(goodsBean));
                        cartAddBean.setPublicPrice(GoodsBeanMini.getPublicPrice(goodsBean));
                        cartAddBean.setProductName(GoodsBeanMini.getProdName(goodsBean));
                        if (GoodsBeanMini.isSecondKillGoods(goodsBean)) {
                            cartAddBean.setCessor(true);
                            cartAddBean.setCessorPrice(GoodsBeanMini.getKillPrice(goodsBean));
                        } else {
                            cartAddBean.setCessor(false);
                        }
                        cartAddBean.setSalePrice(GoodsBeanMini.getSalePrice(goodsBean));
                        cartAddBean.setStock(stockCount);
                        //Todo 是否下架待加
                        ShoppingCartUtil.getInstance().addGood(1, cartAddBean);
                        addCart(cartIcon);
                        if (GoodsBeanMini.isSecondKillGoods(goodsBean)) {
                            addCartTxt.setText(R.string.already_add_to_cart);
                            addCartTxt.setEnabled(false);
                        }
                    }
                } else {
                    View contentView = LayoutInflater.from(GoodsDetailActivity.this).inflate(R.layout.dialog_remind_arrive, null);
                    TextView okTextView = (TextView) contentView.findViewById(R.id.ok);

                    final Dialog dialog = DialogUtil.showDialog(GoodsDetailActivity.this, contentView, getResources().getDimensionPixelSize(R.dimen.x460), getResources().getDimensionPixelSize(R.dimen.x290));
                    okTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LogUtils.i("view.getId=" + view.getId());
                            dialog.dismiss();
                            subscriberPush();
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.detail_cart_frame:
                appContext.toMainActivity(this, 3);
                break;
            default:
                break;
        }

    }

    private void subscriberPush() {
        HashMap<String, String> map = new HashMap<>();
        map.put("subcompany", MyLocationManagerUtil.getInstance().getSubCompanyId());
        map.put("productNo", GoodsBeanMini.getGoodNo(goodsBean));
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
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(GoodsDetailActivity.this, null, null);
        httpRequestDao.subscriberPush(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
            }
        });
    }

    private void changeTab(int tabId) {
        if (tabId == R.id.description_goods_detail) {
            descriptionTitleTextView.setSelected(true);
            specificationsTitleTextView.setSelected(false);
            descriptionListViewInScroll.setVisibility(View.VISIBLE);
            specificationListViewInScroll.setVisibility(View.GONE);
        } else {
            descriptionTitleTextView.setSelected(false);
            specificationsTitleTextView.setSelected(true);
            descriptionListViewInScroll.setVisibility(View.GONE);
            specificationListViewInScroll.setVisibility(View.VISIBLE);
        }
    }

    private void addCart(ImageView iv) {
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(mContext);
//        goods.setImageDrawable(productPoster.getDrawable());
        goods.setImageResource(R.drawable.logo);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
        goodsDetailRoot.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        goodsDetailRoot.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        addCartTxt.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        cartNumTxt.getLocationInWindow(endLoc);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + addCartTxt.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + addCartTxt.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + cartNumTxt.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        float controlY = startLoc[1] - parentLocation[1] - addCartTxt.getHeight() * 2;

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, controlY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
                cartNumTxt.setText(String.valueOf(ShoppingCartUtil.getInstance().getGoodsNum()));
                // 把移动的图片imageview从父布局里移除
                goodsDetailRoot.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
