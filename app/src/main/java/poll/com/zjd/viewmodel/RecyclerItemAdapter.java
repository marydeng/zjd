package poll.com.zjd.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

import poll.com.zjd.BR;
import poll.com.zjd.activity.BaseActivity;


public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder> {

    private Context mContext;
    private List dataSources;
    private int itemLayout;
    private BaseViewModel viewModel;
    private BaseActivity activity;
    private String flag;

    public RecyclerItemAdapter(Context _context, List _data, int _itemlayout) {
        this(_context, _data, _itemlayout, null);
    }

    public RecyclerItemAdapter(Context _context, List _data, int _itemlayout, BaseViewModel viewModel) {
        this(_context, _data, _itemlayout, viewModel, null);
    }

    public RecyclerItemAdapter(Context _context, List _data, int _itemlayout, BaseViewModel _viewModel, String _flag) {
        mContext = _context;
        dataSources = _data;
        itemLayout = _itemlayout;
        viewModel = _viewModel;
        flag = _flag;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(itemLayout, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (dataSources.size() > 0) {
            if (i >= dataSources.size()) {
                i = dataSources.size() - 1;
            }
            viewHolder.bind(dataSources.get(i), i);
        }
    }

    @Override
    public int getItemCount() {
        if (dataSources != null) {
            return dataSources.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }

        public void bind(@NonNull Object data, int _index) {
            binding.setVariable(BR.item, data);
            if(activity!=null){
                binding.setVariable(BR.activity, activity);
            }
            try {
                binding.setVariable(BR.index, _index);
//                binding.setVariable(BR.viewModel, viewModel);
                binding.setVariable(BR.flag, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //binding.executePendingBindings();
        }
    }
}
