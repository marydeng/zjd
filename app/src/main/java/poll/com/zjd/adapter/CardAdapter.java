package poll.com.zjd.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.model.CardBean;
import poll.com.zjd.utils.DoubleCalculateUtil;
import poll.com.zjd.utils.LogUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/20
 * 文件描述：储值卡适配器类
 */
public class CardAdapter extends SuperAdapter<CardBean> {
    private static final String TAG = CardAdapter.class.getSimpleName();
    private Context context;
    /**
     * 选中的卡片的位置
     */
    private int selectedPosition = 0;

    public CardAdapter(Context context, List<CardBean> items, @LayoutRes int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, CardBean item) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        holder.setBackgroundResource(R.id.card_relative_layout, item.getDrawableId());
        holder.setText(R.id.cost_value, context.getResources().getString(R.string.yuan, decimalFormat.format(item.getRechargeAmount())));


        TextView gettingTextView = holder.getView(R.id.monkey_get);
        if (item.getGiftAmount() > 0 && item.getNumb() > 0) {
            double gettingValue = DoubleCalculateUtil.add(item.getRechargeAmount(), item.getGiftAmount());
            double gettingCouponsValue=DoubleCalculateUtil.multiply(item.getNumb(),item.getCouponParValue());
            String gettingValueTxt = context.getString(R.string.both_get, decimalFormat.format(gettingValue),decimalFormat.format(gettingCouponsValue));
            int start = 1;
            int end = start + decimalFormat.format(gettingValue).length();
            int start2=gettingValueTxt.indexOf("券")+1;
            int end2=start2+decimalFormat.format(gettingCouponsValue).length();
            SpannableString spannableString = new SpannableString(gettingValueTxt);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_e54956)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_e54956)), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            gettingTextView.setText(spannableString);
        } else if (item.getGiftAmount() > 0 && item.getNumb() <= 0) {
            double gettingValue = DoubleCalculateUtil.add(item.getRechargeAmount(), item.getGiftAmount());
            String gettingValueTxt = context.getString(R.string.monkey_get, decimalFormat.format(gettingValue));
            int start = gettingValueTxt.indexOf(decimalFormat.format(gettingValue));
            int end = start + decimalFormat.format(gettingValue).length();
            SpannableString spannableString = new SpannableString(gettingValueTxt);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_e54956)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            gettingTextView.setText(spannableString);
        } else {
            double gettingCouponsValue=DoubleCalculateUtil.multiply(item.getNumb(),item.getCouponParValue());
            String gettingValueTxt = context.getString(R.string.coupons_get, decimalFormat.format(gettingCouponsValue));
            int start = gettingValueTxt.indexOf(decimalFormat.format(gettingCouponsValue));
            int end = start + decimalFormat.format(gettingCouponsValue).length();
            SpannableString spannableString = new SpannableString(gettingValueTxt);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_e54956)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            gettingTextView.setText(spannableString);
        }
        if (selectedPosition == layoutPosition) {
            holder.getView(R.id.checked_image_view).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.checked_image_view).setVisibility(View.GONE);
        }
        holder.setOnClickListener(R.id.card_relative_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG + ",view.getId=" + view.toString());
                if (selectedPosition != layoutPosition) {
                    selectedPosition = layoutPosition;
                    notifyDataSetHasChanged();
                }
            }
        });
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
