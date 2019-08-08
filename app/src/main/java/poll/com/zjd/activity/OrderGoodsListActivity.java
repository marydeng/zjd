package poll.com.zjd.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.mini.GoodsBeanMini;
import poll.com.zjd.model.ShoppiingCartBean;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ShoppingCartUtil;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/24
 * 文件描述：提交订单-商品列表
 */
@FmyContentView(R.layout.activity_order_goods_list)
public class OrderGoodsListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OrderGoodsListActivity.class.getSimpleName();
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.goods_list_recycle_view)
    private RecyclerView recyclerView;

    private SuperAdapter<ShoppiingCartBean> superAdapter;
    private DecimalFormat decimalFormat = new DecimalFormat("0.0#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }


    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式
        titleTextView.setText(R.string.goods_list);

        superAdapter = new SuperAdapter<ShoppiingCartBean>(this, ShoppingCartUtil.getInstance().getChooseBuyList(), R.layout.item_goods) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, ShoppiingCartBean item) {
                TextView originTextView = holder.findViewById(R.id.goods_origin_price);
                originTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                GlideManager.showImageSqu(OrderGoodsListActivity.this, item.getCartAddBean().getPicSmall(), (ImageView) holder.findViewById(R.id.goods_poster), 0, 0);
                holder.setText(R.id.goods_name, item.getCartAddBean().getProductName());

                ImageView iv_kill = holder.findViewById(R.id.product_kill);
                if (item.getCartAddBean().isCessor()) {//是否秒杀商品
                    String originPrice = getString(R.string.origin_price_txt, decimalFormat.format(item.getCartAddBean().getPublicPrice()));
                    holder.setText(R.id.goods_origin_price, originPrice);
                    holder.setText(R.id.goods_current_price, getString(R.string.second_kill_price_txt, decimalFormat.format(item.getCartAddBean().getCessorPrice())));
                    iv_kill.setVisibility(View.VISIBLE);
                } else {
                    holder.setText(R.id.goods_origin_price, "");
                    holder.setText(R.id.goods_current_price, getString(R.string.member_price_txt, decimalFormat.format(item.getCartAddBean().getSalePrice())));
                    iv_kill.setVisibility(View.GONE);

                }
                String numStr = "×" + item.getCount();
                holder.setText(R.id.goods_num, numStr);

                if (item.isSoldOut()) {
                    holder.setVisibility(R.id.sold_out_txt, View.VISIBLE);
                    holder.setVisibility(R.id.mask, View.VISIBLE);
                    holder.setTextColor(R.id.goods_name, getResources().getColor(R.color.gray_999999));
                    holder.setTextColor(R.id.goods_current_price, getResources().getColor(R.color.gray_999999));
                } else {
                    holder.setVisibility(R.id.sold_out_txt, View.GONE);
                    holder.setVisibility(R.id.mask, View.GONE);
                    holder.setTextColor(R.id.goods_name, getResources().getColor(R.color.gray_333333));
                    holder.setTextColor(R.id.goods_current_price, getResources().getColor(R.color.red_e51728));
                }
            }
        };
        recyclerView.setAdapter(superAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
}
