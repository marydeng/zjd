package poll.com.zjd.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
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
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.manager.UrlSchemeManager;
import poll.com.zjd.mini.CessorCommodityBeanMini;
import poll.com.zjd.mini.GoodsBeanMini;
import poll.com.zjd.model.CartAddBean;
import poll.com.zjd.model.CessorCommodityBean;
import poll.com.zjd.model.CessorPosterResponse;
import poll.com.zjd.model.KillHeadPicBean;
import poll.com.zjd.model.ProductBean;
import poll.com.zjd.model.ShoppiingCartBean;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.LoadMoreListView;

import static net.lucode.hackware.magicindicator.buildins.UIUtil.getScreenWidth;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/21  23:05
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */
@FmyContentView(R.layout.activity_kill)
public class KillActivity extends BaseActivity implements View.OnClickListener {
    @FmyViewView(R.id.kill_refresh)
    private SwipeRefreshLayout mRefresh;
    @FmyViewView(R.id.kill_list)
    private LoadMoreListView mListView;
    @FmyViewView(R.id.head_back)
    private ImageView mbk;
    @FmyViewView(R.id.head_text)
    private TextView mTitle;
    @FmyViewView(R.id.view_no_data)
    private LinearLayout viewNoDataLinearLayout;
    @FmyViewView(R.id.kill_commodity_content)
    private FrameLayout commodityContentFrameLayout;
    @FmyViewView(R.id.no_data_text)
    private TextView noDataTxt;
    @FmyViewView(R.id.kill_cart)
    private ImageView mCart;
    @FmyViewView(R.id.kill_count)
    private TextView mCount;
    private FrameLayout headPosterView;

    private HttpRequestDao httpRequestDao;
    private List<CessorCommodityBean> cessorCommodityBeanList;
    private SuperAdapter<CessorCommodityBean> superAdapter;

    private KillHeadPicBean killHeadPicBean;
    private PathMeasure mPathMeasure;
    private int cartCount = 0;
    private float[] mCurrentPosition = new float[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBarManager.setStatusBarView();  //设置沉浸式
        httpRequestDao = new HttpRequestDao();
        noDataTxt.setText(R.string.cessor_list_no_data);
        initView();
        getCessorList();
        getCessorPoster();
    }

    int count;

    private void initView() {

        cessorCommodityBeanList = new ArrayList<>();
        superAdapter = new SuperAdapter<CessorCommodityBean>(mContext, cessorCommodityBeanList, R.layout.adapter_cessor_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final CessorCommodityBean item) {
                GlideManager.showImageSqu(mContext, AppConfig.getAppRequestUrl(item.getCommodityDetailVo().getCommodityPicVo().getPicDesc()), (ImageView) holder.findViewById(R.id.product_img), 0, 0);

                LinearLayout productItem = holder.findViewById(R.id.product_item);
                holder.setText(R.id.product_name, item.getCommodityName());
                holder.setText(R.id.product_description, item.getCommodityDetailVo().getAlias());

                TextView tv = holder.findViewById(R.id.product_original_price);
                tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                tv.setText(getString(R.string.origin_price_txt, String.valueOf(item.getCommodityDetailVo().getPublicPrice())));
                holder.setText(R.id.product_real_price, getString(R.string.second_kill_price_txt, String.valueOf(item.getSnappingUpPrice())));


                final ImageView del = holder.findViewById(R.id.product_del);          //减
                final ImageView add = holder.findViewById(R.id.product_add);          //加
                final TextView countText = holder.findViewById(R.id.product_count);   //中间的数字

                count = item.getCount();
                if (count == 0) {
                    del.setVisibility(View.GONE);
                    countText.setVisibility(View.GONE);
                    add.setImageResource(R.drawable.product_add_red);
                } else {
                    countText.setText(item.getCount() + "");
                    del.setImageResource(R.drawable.product_del_gray);
                    add.setImageResource(R.drawable.product_add_gray);
                    countText.setVisibility(View.VISIBLE);
                }

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int alreadyAddCount = ShoppingCartUtil.getInstance().getCardAddBeanCount(item.getCommodityId());
                        if (alreadyAddCount > 0) {
                            ToastUtils.showToast(R.string.second_kill_goods_only_buy_one);
                        } else {
                            del.setImageResource(R.drawable.product_del_gray);
                            add.setImageResource(R.drawable.product_add_gray);
                            addCart((ImageView) v);
                            count = item.getCount() + 1;
                            if (count > 0) {
                                del.setVisibility(View.VISIBLE);
                                countText.setVisibility(View.VISIBLE);
                            }
                            item.setCount(count);
                            countText.setText("" + count);
                            superAdapter.notifyDataSetChanged();
                            CartAddBean cartAddBean = new CartAddBean();
                            cartAddBean.setProductId(CessorCommodityBeanMini.getProductId(item));
                            cartAddBean.setProductNo(CessorCommodityBeanMini.getProductNo(item));
                            cartAddBean.setPicDesc(item.getCommodityDetailVo().getCommodityPicVo().getPicDesc());
                            cartAddBean.setPicSmall(item.getCommodityDetailVo().getCommodityPicVo().getPicSmall());
                            cartAddBean.setPublicPrice(item.getSnappingUpPrice());

                            cartAddBean.setProductName(item.getCommodityName());
                            cartAddBean.setCessor(true);//是否秒杀商品
                            cartAddBean.setPublicPrice(item.getCommodityDetailVo().getPublicPrice());
                            cartAddBean.setCessorPrice(item.getSnappingUpPrice());

//                cartAddBean.setStock(GoodsBeanMini.get);//Todo 库存待加
                            //Todo 是否下架待加
                            ShoppingCartUtil.getInstance().addGood(1, cartAddBean);
                        }
                    }
                });

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        count = item.getCount() - 1;
                        add.setImageResource(R.drawable.product_add_red);
                        item.setCount(count);
                        countText.setText("" + count);
                        cartCount--;
                        mCount.setText(String.valueOf(cartCount));
                        if (count == 0) {
                            del.setVisibility(View.GONE);
                            countText.setVisibility(View.GONE);
                        }
                        superAdapter.notifyDataSetChanged();
                        CartAddBean cartAddBean = new CartAddBean();
                        cartAddBean.setProductId(CessorCommodityBeanMini.getProductId(item));
                        cartAddBean.setProductNo(CessorCommodityBeanMini.getProductNo(item));
                        cartAddBean.setPicDesc(item.getCommodityDetailVo().getCommodityPicVo().getPicDesc());
                        cartAddBean.setPicSmall(item.getCommodityDetailVo().getCommodityPicVo().getPicSmall());
                        cartAddBean.setPublicPrice(item.getCommodityDetailVo().getPublicPrice());
                        cartAddBean.setPublicPrice(item.getSnappingUpPrice());
                        cartAddBean.setProductName(item.getCommodityName());
                        cartAddBean.setCessor(true);  //是否秒杀商品
                        cartAddBean.setCessorPrice(item.getSnappingUpPrice());

//                cartAddBean.setStock(GoodsBeanMini.get);//Todo 库存待加
                        //Todo 是否下架待加
                        ShoppingCartUtil.getInstance().subtractGood(1, cartAddBean);

                    }
                });


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
                }

                productItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(GoodsDetailActivity.GOODS_ID_EXTRA, item.getCommodityId());
                        appContext.startActivity(mContext, GoodsDetailActivity.class, bundle);
                    }
                });

            }
        };

        //广告头部
        final View v = View.inflate(mContext, R.layout.header_kill, null);
        Banner banner = (Banner) v.findViewById(R.id.banner);
        showKillHeadPic(banner);  //设置轮播
        mListView.addHeaderView(v);
        mListView.setAdapter(superAdapter);

        mTitle.setText("秒杀专区");
        mbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.orange);

        mRefresh.setEnabled(false);
        mListView.setEnableLoad(false);
    }

    private void getCessorList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("subcompanyId", MyLocationManagerUtil.getInstance().getSubCompanyId());
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
        DialogUtil.showProgressDialog(this, null, null);
        httpRequestDao.getCessorList(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                List<CessorCommodityBean> dataList = JSON.parseArray(result, CessorCommodityBean.class);
                if (!ObjectUtils.isEmpty(dataList)) {
                    cessorCommodityBeanList.addAll(dataList);
                    for (CessorCommodityBean cessorCommodityBean : cessorCommodityBeanList) {
                        cessorCommodityBean.setCount(ShoppingCartUtil.getInstance().getGoodsCount(CessorCommodityBeanMini.getProductId(cessorCommodityBean)));
                    }
                    superAdapter.notifyDataSetHasChanged();
                }

                checkNoData();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                checkNoData();
            }
        });
        cartCount = ShoppingCartUtil.getInstance().getGoodsNum();
        mCount.setText(String.valueOf(cartCount));
    }

    private void getCessorPoster() {
        HashMap<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(MyLocationManagerUtil.getInstance().getProvinceNO())) {
            map.put("struc", MyLocationManagerUtil.getInstance().getProvinceNO());
        }
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        httpRequestDao.getCessorPoster(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                Gson gson = new Gson();
                CessorPosterResponse cessorPosterResponse = gson.fromJson(result, CessorPosterResponse.class);
                if (null != cessorPosterResponse && !ObjectUtils.isEmpty(cessorPosterResponse.getRow15_site6()) && !StringUtils.isBlank(cessorPosterResponse.getRow15_site6().get(0).getImgUrl())) {
                    GlideManager.showImage(mContext, cessorPosterResponse.getRow15_site6().get(0).getImgUrl(), headPosterView, 0, 0);
                }
            }
        });
    }

    private void checkNoData() {
        if (ObjectUtils.isEmpty(cessorCommodityBeanList)) {
            viewNoDataLinearLayout.setVisibility(View.VISIBLE);
            commodityContentFrameLayout.setVisibility(View.GONE);
        } else {
            viewNoDataLinearLayout.setVisibility(View.GONE);
            commodityContentFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showKillHeadPic(final Banner banner) {

        httpRequestDao.getKillHeaderPic(mContext, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                KillHeadPicBean killHeadPicBean = new Gson().fromJson(result, KillHeadPicBean.class);
                List<KillHeadPicBean.Row15Site6Bean> row15Site6Been = killHeadPicBean.getRow15_site6();
                if (row15Site6Been.size() > 0) {
                    //设置图片加载器
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合
                    banner.setImages(row15Site6Been);
                    //设置自动轮播，默认为true
                    banner.isAutoPlay(true);
                    //设置轮播时间
                    banner.setDelayTime(3000);
                    //设置指示器圆形
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                }
            }
        });
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            final KillHeadPicBean.Row15Site6Bean bean = (KillHeadPicBean.Row15Site6Bean) path;

            if (!KillActivity.this.isDestroyed()){
                GlideManager.showImageDefault(context, AppConfig.getAppRequestUrl(bean.getImgUrl()), imageView, R.drawable.default_img_a, R.drawable.default_img_a);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = bean.getClickUrl();
                        if (StringUtils.isNotEmpty(url)) {
                            if (url.contains(UrlSchemeManager.URL_HEADER)) {
                                UrlSchemeManager.urlAnalysisAction(UrlSchemeManager.FRAGMENT, url);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString(WebViewActivity.LOADURL, AppConfig.getAppRequestUrl(url));
                                appContext.startActivityForResult(KillActivity.this, WebViewActivity.class, MainActivity.GOWHERE, bundle);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * @param iv
     */
    private void addCart(ImageView iv) {
        int height = getResources().getDimensionPixelSize(R.dimen.x28);
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(mContext);
        goods.setImageDrawable(iv.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height, height);
        commodityContentFrameLayout.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        commodityContentFrameLayout.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        mCart.getLocationInWindow(endLoc);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + iv.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + mCart.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
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
                cartCount++;
                mCount.setText(String.valueOf(cartCount));
                // 把移动的图片imageview从父布局里移除
                commodityContentFrameLayout.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @FmyClickView({R.id.kill_cart_rlt})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kill_cart_rlt:
                appContext.toMainActivity(this, 3);
                break;
            default:
                break;
        }
    }
}
