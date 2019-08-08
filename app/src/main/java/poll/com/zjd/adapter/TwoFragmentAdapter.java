package poll.com.zjd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.model.ProductClassBean;
import poll.com.zjd.model.TestBean;
import poll.com.zjd.utils.LogUtils;


/**
 * Created by Tommy on 2017/7/3.
 */

public class TwoFragmentAdapter extends PagerAdapter {

    private Context mContext;

    private List<List<ProductClassBean>> leftList;
    private List<List<TestBean>> rightList;
    private ListView mLeftList,mRightList;
    private SuperAdapter leftAdapter;
    private Set<SuperAdapter> listAdapter = new HashSet<>();

    private int currentPosition = 0;

    public void setPosition(int position){
        this.currentPosition = position;
    }

    public TwoFragmentAdapter(Context context,List<List<ProductClassBean>> leftList,List<List<TestBean>> rightList){
        this.mContext = context;
        this.leftList = leftList;
        this.rightList = rightList;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        final View view = View.inflate(mContext, R.layout.fragment_two_layout,null);

        mLeftList = (ListView) view.findViewById(R.id.frg_two_typeList);
        mRightList = (ListView) view.findViewById(R.id.frg_two_productList);

        leftAdapter = new SuperAdapter<ProductClassBean>(mContext,leftList.get(position),R.layout.fragment_two_layout_left) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, ProductClassBean item) {
//                final RelativeLayout bg = holder.findViewById(R.id.left_Rv);
//                if (item.isSelect()){
//                    bg.setBackground(mContext.getResources().getDrawable(R.drawable.select_bg));
//                }else {
//                    bg.setBackgroundColor(mContext.getResources().getColor(R.color.black_bg));
//                }
//                TextView tv = holder.findViewById(R.id.left_text);
//                tv.setText(item.getClassName());
//
//                holder.setOnClickListener(R.id.left_Rv, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        for (int i=0;i<leftList.get(currentPosition).size();i++){
//                            leftList.get(currentPosition).get(i).setSelect(false);
//                        }
//                        leftList.get(currentPosition).get(layoutPosition).setSelect(true);
//                        bg.setBackground(mContext.getResources().getDrawable(R.drawable.select_bg));
//
//                        LogUtils.e("刷新了"+currentPosition);

//                        listAdapter.get(currentPosition).notifyDataSetChanged();
//                        mLeftList.setAdapter(listAdapter.get(currentPosition));
//                        leftAdapter.notifyDataSetChanged();
//                        for (SuperAdapter adapter : listAdapter) {
//                            LogUtils.e("-------------");
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
            }
        };

//        listAdapter.add(leftAdapter);
//
//        mLeftList.setAdapter(leftAdapter);
//
//
//        SuperAdapter rightAdapter = new SuperAdapter<TestBean>(mContext,rightList.get(position),R.layout.fragment_two_layout_right) {
//            @Override
//            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final TestBean item) {
//                holder.setText(R.id.right_name,item.getName());
//                holder.setText(R.id.right_price,"価格: "+item.getPrice() +" 円");
//
//                GlideManager.showImage(mContext,item.getUrl(),(ImageView)holder.findViewById(R.id.right_img),0,0);
//
//                holder.findViewById(R.id.right_layout).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //相当于在这里跳转
//                        Toast.makeText(mContext,""+item.getName(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        };
//        mRightList.setAdapter(rightAdapter);



        container.addView(view);

        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
