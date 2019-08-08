package poll.com.zjd.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
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
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.mini.GoodsBean1Mini;
import poll.com.zjd.model.CartAddBean;
import poll.com.zjd.model.CessorBean;
import poll.com.zjd.model.CessorCommodityBean;
import poll.com.zjd.model.GoodBean1;

import poll.com.zjd.model.GoodsClass1;
import poll.com.zjd.model.ProductBean;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.ObjectUtils;
import poll.com.zjd.utils.SPUtils;
import poll.com.zjd.utils.ShoppingCartUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.utils.UrlCodeUtils;
import poll.com.zjd.view.LoadMoreListView;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:15
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */
@FmyContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    public static final String SEARCHDATA = "searchData";

    @FmyViewView(R.id.search_back)
    private RelativeLayout searchBack;
    @FmyViewView(R.id.search_edt)
    private EditText searchEdt;
    @FmyViewView(R.id.search_s)
    private RelativeLayout searchS;
    @FmyViewView(R.id.search_del)
    private ImageView searchDel;
    @FmyViewView(R.id.search_history)
    private RelativeLayout searchHistory;
    @FmyViewView(R.id.search_chipView)
    private ChipView mChipView;
    @FmyViewView(R.id.search_list)
    private LoadMoreListView mListView;
    @FmyViewView(R.id.search_history_lay)
    private LinearLayout mHistoryRv;
    @FmyViewView(R.id.search_result)
    private RelativeLayout mProductRv;
    @FmyViewView(R.id.search_car)
    private ImageView mCart;
    @FmyViewView(R.id.search_count)
    private TextView mCount;
    @FmyViewView(R.id.search_rl)
    private RelativeLayout cartIconRlt;
    @FmyViewView(R.id.view_no_data)
    private LinearLayout noDataLinearLayout;
    @FmyViewView(R.id.no_data_text)
    private TextView noDataText;


    private PathMeasure mPathMeasure;
    private int cartCount = 0;
    private float[] mCurrentPosition = new float[2];
    private SPUtils spUtils = new SPUtils();
    private Gson gson = new Gson();

    List<Chip> searchHistoryList = new ArrayList<>();
    private SuperAdapter superAdapter;
    private List<ProductBean> productBeanList;

    private String searchWord;  //传回来要搜索的词

    private HttpRequestDao httpRequestDao;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBarManager.setStatusBarView();  //设置沉浸式
        noDataText.setText(R.string.no_search_result);

        httpRequestDao = new HttpRequestDao();

        cartCount = ShoppingCartUtil.getInstance().getGoodsNum();
        mCount.setText(String.valueOf(cartCount));

        String historyText = spUtils.getString(mContext, SPUtils.CACHE_SEARCH_TEXT);
        if (StringUtils.isNotEmpty(historyText)) {
            List<Tag> tags = gson.fromJson(historyText, new TypeToken<ArrayList<Tag>>() {
            }.getType());
            searchHistoryList.addAll(tags);
        }

        mChipView.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                searchEdt.setText(chip.getText());
                searchEdt.setSelection(chip.getText().length());
            }
        });
        mChipView.setChipList(searchHistoryList);
        initView();

        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                searchWord = bundle.getString(SEARCHDATA);
                searchEdt.setText(searchWord);
                searchEdt.setSelection(searchWord.length());
                search();
            }
        }
    }

    @FmyClickView({R.id.search_back, R.id.search_s, R.id.search_del, R.id.search_rl})
    @Override
    public void onClick(View view) {
        LogUtils.i("view.getId=" + view.toString());
        switch (view.getId()) {
            case R.id.search_back:
                onBackPressed();
                break;
            case R.id.search_s:
                search();
                break;
            case R.id.search_del:
                searchHistoryList.clear();
                mChipView.setChipList(searchHistoryList);
                spUtils.put(mContext, SPUtils.CACHE_SEARCH_TEXT, gson.toJson(searchHistoryList));
                break;
            case R.id.search_rl:
                appContext.toMainActivity(this, 3);
                break;
            default:
                break;
        }

    }

    int count;

    private void initView() {
        productBeanList = new ArrayList<>();
        superAdapter = new SuperAdapter<ProductBean>(mContext, productBeanList, R.layout.adapter_product_list) {
            @Override
            public void onBind(final SuperViewHolder holder, int viewType, final int layoutPosition, final ProductBean item) {
                GlideManager.showImageDefault(mContext, item.getGoodsBean1().getImage(), (ImageView) holder.findViewById(R.id.product_img), 0, 0);

                holder.findViewById(R.id.product_select).setVisibility(View.GONE);    //消失掉打勾

                final ImageView del = holder.findViewById(R.id.product_del);          //减
                final ImageView add = holder.findViewById(R.id.product_add);          //加
                final TextView countText = holder.findViewById(R.id.product_count);   //中间的数字
                ImageView iv_check = holder.findViewById(R.id.product_select);        //是否选择(这里用不到)
                ImageView iv_kill = holder.findViewById(R.id.product_kill);           //秒杀
                LinearLayout productItem = holder.findViewById(R.id.product_item);
                count = productBeanList.get(layoutPosition).getCount();
                if (count == 0) {
                    del.setVisibility(View.GONE);
                    countText.setVisibility(View.GONE);
                    add.setImageResource(R.drawable.product_add_red);
                } else {
                    countText.setText(item.getCount() + "");
                    del.setVisibility(View.VISIBLE);
                    countText.setVisibility(View.VISIBLE);
                    if (item.isSecondKill()) {
                        del.setImageResource(R.drawable.product_del_gray);
                        add.setImageResource(R.drawable.product_add_gray);
                    } else {
                        del.setImageResource(R.drawable.product_del_red);
                        add.setImageResource(R.drawable.product_add_red);
                    }
                }

                holder.setText(R.id.product_name, item.getGoodsBean1().getProdName());
                holder.setText(R.id.product_description, item.getGoodsBean1().getAlias());


                TextView tv = holder.findViewById(R.id.product_original_price);
                tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                tv.setText(getString(R.string.origin_price_txt, decimalFormat.format(item.getGoodsBean1().getPublicPrice())));
                if (item.isSecondKill()) {
                    holder.setText(R.id.product_real_price, getString(R.string.second_kill_price_txt, decimalFormat.format(item.getSecondKillPrice())));
                    iv_kill.setVisibility(View.VISIBLE);
                } else {
                    holder.setText(R.id.product_real_price, getString(R.string.member_price_txt, decimalFormat.format(item.getGoodsBean1().getProdPrice())));
                    iv_kill.setVisibility(View.GONE);
                }

                if (item.isSellOut()) {
                    holder.setVisibility(R.id.arrival_remind_txt, View.VISIBLE);
                    holder.setVisibility(R.id.goods_operation_rlt, View.GONE);

                    holder.setOnClickListener(R.id.arrival_remind_txt, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            View contentView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.dialog_remind_arrive, null);
                            TextView okTextView = (TextView) contentView.findViewById(R.id.ok);

                            final Dialog dialog = DialogUtil.showDialog(SearchActivity.this, contentView, getResources().getDimensionPixelSize(R.dimen.x460), getResources().getDimensionPixelSize(R.dimen.x290));
                            okTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LogUtils.i("view.getId=" + view.getId());
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                } else {
                    holder.setVisibility(R.id.arrival_remind_txt, View.GONE);
                    holder.setVisibility(R.id.goods_operation_rlt, View.VISIBLE);
                }
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int alreadyAddCount = ShoppingCartUtil.getInstance().getCardAddBeanCount(productBeanList.get(layoutPosition).getGoodsBean1().getId());
                        if (item.isSecondKill() && alreadyAddCount > 0) {
                            ToastUtils.showToast(R.string.second_kill_goods_only_buy_one);
                        }
//                        } else if(alreadyAddCount>=item.get){  //库存信息
//
//                        }
                        else {
                            addCart((ImageView) v);
                            count = productBeanList.get(layoutPosition).getCount() + 1;
                            if (count > 0) {
                                if (item.isSecondKill()) {
                                    del.setImageResource(R.drawable.product_del_gray);
                                    add.setImageResource(R.drawable.product_add_gray);
                                } else {
                                    del.setImageResource(R.drawable.product_del_red);
                                    add.setImageResource(R.drawable.product_add_red);
                                }
                                del.setVisibility(View.VISIBLE);
                                countText.setVisibility(View.VISIBLE);
                            }
                            productBeanList.get(layoutPosition).setCount(count);
                            countText.setText("" + count);
                            superAdapter.notifyDataSetChanged();
                            CartAddBean cartAddBean = new CartAddBean();
                            cartAddBean.setProductId(GoodsBean1Mini.getProductId(item.getGoodsBean1()));
                            cartAddBean.setProductNo(GoodsBean1Mini.getProductNo(item.getGoodsBean1()));
                            cartAddBean.setPicDesc(productBeanList.get(layoutPosition).getGoodsBean1().getImage());
                            cartAddBean.setPicSmall(productBeanList.get(layoutPosition).getGoodsBean1().getImage());
                            cartAddBean.setPublicPrice(productBeanList.get(layoutPosition).getGoodsBean1().getPublicPrice());
                            cartAddBean.setProductName(productBeanList.get(layoutPosition).getGoodsBean1().getProdName());
                            if (item.isSecondKill()) {
                                cartAddBean.setCessor(true);
                                cartAddBean.setCessorPrice(item.getSecondKillPrice());
                            } else {
                                cartAddBean.setCessor(false);
                            }
                            cartAddBean.setSalePrice(item.getGoodsBean1().getProdPrice());
//                cartAddBean.setStock(GoodsBeanMini.get);//Todo 库存待加
                            //Todo 是否下架待加
                            ShoppingCartUtil.getInstance().addGood(1, cartAddBean);
                        }
                    }
                });

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        count = productBeanList.get(layoutPosition).getCount() - 1;
                        productBeanList.get(layoutPosition).setCount(count);
                        countText.setText("" + count);
                        cartCount--;
                        mCount.setText(String.valueOf(cartCount));
                        if (count == 0) {
                            del.setVisibility(View.GONE);
                            countText.setVisibility(View.GONE);
                        }
                        add.setImageResource(R.drawable.product_add_red);
                        superAdapter.notifyDataSetChanged();
                        CartAddBean cartAddBean = new CartAddBean();
                        cartAddBean.setProductId(GoodsBean1Mini.getProductId(item.getGoodsBean1()));
                        cartAddBean.setProductNo(GoodsBean1Mini.getProductNo(item.getGoodsBean1()));
                        cartAddBean.setPicDesc(productBeanList.get(layoutPosition).getGoodsBean1().getImage());
                        cartAddBean.setPicSmall(productBeanList.get(layoutPosition).getGoodsBean1().getImage());
                        cartAddBean.setPublicPrice(productBeanList.get(layoutPosition).getGoodsBean1().getPublicPrice());
                        cartAddBean.setProductName(productBeanList.get(layoutPosition).getGoodsBean1().getProdName());
                        if (item.isSecondKill()) {
                            cartAddBean.setCessor(true);
                            cartAddBean.setCessorPrice(item.getSecondKillPrice());
                        } else {
                            cartAddBean.setCessor(false);
                        }
                        cartAddBean.setSalePrice(item.getGoodsBean1().getProdPrice());
//                cartAddBean.setStock(GoodsBeanMini.get);//Todo 库存待加
                        //Todo 是否下架待加
                        ShoppingCartUtil.getInstance().subtractGood(1, cartAddBean);

                    }
                });

                productItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(GoodsDetailActivity.GOODS_ID_EXTRA, GoodsBean1Mini.getProductId(item.getGoodsBean1()));
                        appContext.startActivity(mContext, GoodsDetailActivity.class, bundle);
                    }
                });
            }
        };
        mListView.setAdapter(superAdapter);
        changeFrame(false);
    }

    private void search() {
        String search = searchEdt.getText().toString().trim();
        if (StringUtils.isNotEmpty(search)) {

            filterText(searchHistoryList, search);

            spUtils.put(mContext, SPUtils.CACHE_SEARCH_TEXT, gson.toJson(searchHistoryList));

            mChipView.setChipList(searchHistoryList);
//            if (search.equals("美女")) {
//                changeFrame(true);
//            } else {
//                changeFrame(false);
//            }
            searchProductList(search);

        } else {
            changeFrame(false);
            ToastUtils.showToast(mContext, "搜索的内容不能为空哦~", 1);
        }

    }

    private void searchProductList(String search) {
        HashMap<String, String> map = new HashMap<>();
        map.put("kword", UrlCodeUtils.toURLEncoded(UrlCodeUtils.toURLEncoded(search)));
        map.put("param12", MyLocationManagerUtil.getInstance().getSubCompanyId());
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        DialogUtil.showProgressDialog(this, null, null);
        httpRequestDao.searchProductList(this, jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                changeFrame(true);
                GoodsClass1 gs = new Gson().fromJson(result, GoodsClass1.class);
                productBeanList.clear();
                if (ObjectUtils.notEmpty(gs.getPojoJson())) {
                    for (GoodBean1 goodsBean1 : gs.getPojoJson()) {
                        ProductBean productBean = new ProductBean();
                        productBean.setGoodsBean1(goodsBean1);
                        if (ObjectUtils.notEmpty(gs.getCessordata())) {
                            for (CessorBean cessorBean : gs.getCessordata()) {
                                if (goodsBean1.getId().equals(cessorBean.getCommodityId())) {
                                    productBean.setSecondKill(true);
                                    productBean.setSecondKillPrice(cessorBean.getSnappingUpPrice());
                                }
                            }
                        }
                        productBean.setCount(ShoppingCartUtil.getInstance().getGoodsCount(productBean.getGoodsBean1().getId()));
                        productBeanList.add(productBean);
                    }
                }
                superAdapter.notifyDataSetHasChanged();
                checkNoData();
            }

        });

    }

    private void checkNoData() {
        if (ObjectUtils.isEmpty(productBeanList)) {
            noDataLinearLayout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            cartIconRlt.setVisibility(View.GONE);
        } else {
            noDataLinearLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            cartIconRlt.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更改显示状态
     *
     * @param search
     */
    private void changeFrame(boolean search) {
        if (search) {
            mProductRv.setVisibility(View.VISIBLE);
            mHistoryRv.setVisibility(View.GONE);
        } else {
            mProductRv.setVisibility(View.GONE);
            mHistoryRv.setVisibility(View.VISIBLE);
        }
    }

    //过滤重复的文字
    private List<Chip> filterText(List<Chip> list, String text) {
        boolean isHas = false;
        for (Chip chip : list) {
            if (chip.getText().equals(text)) {
                isHas = true;
            }
        }
        if (!isHas) {
            list.add(0, new Tag(text));  //插入第一位
        }
        return list;
    }

    public class Tag implements Chip {
        private String mName;
        private int mType = 0;

        public Tag(String name, int type) {
            this(name);
            mType = type;
        }

        public Tag(String name) {
            mName = name;
        }

        @Override
        public String getText() {
            return mName;
        }

        public int getType() {
            return mType;
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
        mProductRv.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        mProductRv.getLocationInWindow(parentLocation);

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
                mProductRv.removeView(goods);
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
