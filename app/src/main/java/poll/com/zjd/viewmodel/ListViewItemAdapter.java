package poll.com.zjd.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

import poll.com.zjd.BR;
import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.fragment.BaseFragment;


public class ListViewItemAdapter extends BaseAdapter {
    private Context mContext;
    private  List dataSources;
    private  int itemLayout;
    private BaseActivity activity;
    private BaseFragment fragment;
    public ListViewItemAdapter(Context _context, List _data, int _itemlayout){
        mContext=_context;
        dataSources=_data;
        itemLayout=_itemlayout;
    }
    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }
    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }
    @Override
    public int getCount() {
        return dataSources.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView==null){
            viewHolder = new ViewHolder();
            viewHolder.viewDataBinding= DataBindingUtil.inflate(LayoutInflater
                    .from(mContext), itemLayout, parent, false);
            convertView=viewHolder.viewDataBinding.getRoot();
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.viewDataBinding.setVariable(BR.item, dataSources.get(position));
        viewHolder.viewDataBinding.setVariable(BR.index, position);
        if(activity!=null){
            viewHolder.viewDataBinding.setVariable(BR.activity, activity);
        }
        if (fragment!=null){
            viewHolder.viewDataBinding.setVariable(BR.fragment, fragment);
        }
        return viewHolder.viewDataBinding.getRoot();
    }
    class ViewHolder {
        ViewDataBinding viewDataBinding;
    }

}
