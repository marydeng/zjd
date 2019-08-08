package poll.com.zjd.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

import poll.com.zjd.BR;
import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.fragment.BaseFragment;


public class GridViewItemAdapter extends BaseAdapter {

    private Context mContext;

    private List   dataSources;
    private int    itemLayout;
    private String flag;

    private BaseFragment  fragment;
    private BaseActivity  activity;
    private BaseViewModel viewModel;

    public GridViewItemAdapter(Context _context, List _data, int _itemlayout) {
        mContext = _context;
        dataSources = _data;
        itemLayout = _itemlayout;
        if (_data == null) {
            dataSources = Collections.emptyList();
        }
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public void setViewModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
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
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), itemLayout, parent, false);
        if (fragment != null) {
            binding.setVariable(BR.fragment, fragment);
        }
        if (activity != null) {
            binding.setVariable(BR.activity, activity);
        }
        if (viewModel != null) {
//            binding.setVariable(BR.viewModel, viewModel);
        }
        if (flag != null) {
            binding.setVariable(BR.flag, flag);
        }

        try {
            binding.setVariable(BR.index, position);
            binding.setVariable(BR.item, dataSources.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return binding.getRoot();
    }


}
