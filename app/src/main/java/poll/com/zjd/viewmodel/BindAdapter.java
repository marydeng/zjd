package poll.com.zjd.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.activity.BaseActivity;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.fragment.BaseFragment;
import poll.com.zjd.manager.GlideManager;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.UiUtil;
import poll.com.zjd.view.CircleImageView;


/**
 * Description: databinding自定义bindadapter，公用方法
 */
public class BindAdapter {
    private final static String TAG = "BindAdapter";

    @BindingConversion
    public static int convertBindable(ObservableInt _int) {
        return _int.get();
    }

    /**
     * 设置listview itemlayout
     *
     * @param view
     * @param itemlayoutid
     */
    @BindingAdapter({"itemList", "itemLayout"})
    public static void bindItemLayout(ListView view, List itemlist, int itemlayoutid) {
        ListViewItemAdapter listViewItemAdapter = null;
        if (view.getTag(R.id.bindRecycleView) == null) {
            view.setTag(R.id.bindRecycleView, true);
            listViewItemAdapter = new ListViewItemAdapter(view.getContext(), itemlist, itemlayoutid);
            view.setAdapter(listViewItemAdapter);
        } else {
            //view.deferNotifyDataSetChanged();
            ((ListViewItemAdapter) view.getAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * 设置listview itemlayout
     *
     * @param view
     * @param itemlayoutid
     */
    @BindingAdapter({"itemList", "itemLayout", "emptyView", "viewModel", "flag"})
    public static void bindRecycleItemLayout(final RecyclerView view, final List itemlist,
                                             int itemlayoutid, int emptyviewId, BaseViewModel viewModel, String flag) {
        if (view.getAdapter() == null) {
            //view.setTag(R.id.bindRecycleView, true);
            RecyclerItemAdapter listViewItemAdapter = new RecyclerItemAdapter(view.getContext(), itemlist, itemlayoutid, viewModel, flag);
            view.setAdapter(listViewItemAdapter);
        } else {
            if (view instanceof LoadMoreRecyclerView) {
                LoadMoreRecyclerView recyclerView = (LoadMoreRecyclerView) view;
                if (recyclerView.getUPDATE_STATE() == LoadMoreRecyclerView.LoadMoreRVUpdateState.UPDATE_INSERT.getValue()) {
                    view.getAdapter().notifyItemInserted(view.getAdapter().getItemCount());
                } else {
                    view.getAdapter().notifyDataSetChanged();
                    recyclerView.setUpdateState(LoadMoreRecyclerView.LoadMoreRVUpdateState.UPDATE_INSERT);
                }
            } else {
                view.getAdapter().notifyDataSetChanged();
            }
        }
        View rootView = view.getRootView();
        final View emptyView = rootView.findViewById(emptyviewId);
        UiUtil.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                if (emptyView != null) {
                    if (view.getAdapter() != null && itemlist.size() == 0) {
                        if (view.getParent() instanceof SwipeRefreshLayout) {
                            ((View) view.getParent()).setVisibility(View.GONE);
                        }
                        view.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        if (view.getParent() instanceof SwipeRefreshLayout) {
                            ((View) view.getParent()).setVisibility(View.VISIBLE);
                        }
                        view.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                }
            }
        }, 800);
    }

    @BindingAdapter({"itemList", "itemLayout", "emptyView"})
    public static void bindRecycleItemLayout(RecyclerView view, List itemlist, int itemlayoutid, int emptyviewId) {
        bindRecycleItemLayout(view, itemlist, itemlayoutid, emptyviewId, null, null);
    }

    @BindingAdapter({"itemList", "itemLayout", "emptyView", "viewModel"})
    public static void bindRecycleItemLayout(RecyclerView view, List itemlist, int itemlayoutid, int emptyviewId, BaseViewModel viewModel) {
        bindRecycleItemLayout(view, itemlist, itemlayoutid, emptyviewId, viewModel, null);
    }

    @BindingAdapter({"rvManager"})
    public static void setRecyclerViewLayoutManager(RecyclerView view, RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) {
            view.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            view.setLayoutManager(layoutManager);
        }
    }

    @BindingAdapter({"rvAddLine"})
    public static void addRecyclerViewLine(RecyclerView view, boolean isVertical) {
        if (isVertical) {
            view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL_LIST));
        } else {
            view.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL_LIST));
        }
    }

    /**
     * 加载图片
     */
//    @BindingAdapter({"imageUrl", "defPhoto", "haveAnim"})
//    public static void bindImageView(ImageView view, String imageUrl, Drawable defPhoto, boolean haveAnim) {
//        if (!StringUtils.isEmpty(imageUrl)) {
//            imageUrl = imageUrl.contains("http://") ? imageUrl : AppConfig.HOST_URL + imageUrl;
//            DrawableTypeRequest<String> drawableTypeRequest = Glide.with(view.getContext()).load(imageUrl);
//            if (defPhoto == null) {
//                if (haveAnim) {
//                    drawableTypeRequest.dontAnimate().dontTransform().placeholder(R.drawable.default_img).into(view);
//                } else {
//                    drawableTypeRequest.placeholder(R.drawable.default_img).into(view);
//                }
//            } else {
//                if (haveAnim) {
//                    drawableTypeRequest.dontAnimate().dontTransform().placeholder(defPhoto).into(view);
//                } else {
//                    drawableTypeRequest.placeholder(defPhoto).into(view);
//                }
//            }
//        } else if (defPhoto != null) {
//            view.setImageDrawable(defPhoto);
//        }
//    }

    /**
     * 加载图片
     */
//    @BindingAdapter({"imageUrl", "defPhoto"})
//    public static void bindImageViewwithdef(ImageView view, String imageUrl, Drawable defPhoto) {
//        if (view instanceof CircleImageView) {
//            bindImageView(view, imageUrl, defPhoto, true);
//        } else {
//            bindImageView(view, imageUrl, defPhoto, false);
//        }
//    }

    @BindingAdapter({"imageUrl"})
    public static void bindImage(final ImageView view, String imageUrl) {
        GlideManager.showImageDefault(view.getContext(),imageUrl,view,0,0);
//        Glide.with(view.getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                if (resource != null) {
//                    view.setBackground(new BitmapDrawable(resource));
//                }
//            }
//        });
    }

    @BindingAdapter({"image_animate"})
    public static void bindImageAnimate(ImageView view, int image_animate) {
        Animation imageAnim = AnimationUtils.loadAnimation(view.getContext(), image_animate);
        view.setAnimation(imageAnim);
    }

    /**
     * 设置GridView itemlayout
     */
    @android.databinding.BindingAdapter(value = {"itemList", "itemLayout", "flag", "activity", "fragment", "viewModel"}, requireAll = false)
    public static void bindGridViewItemLayout(GridView view, List itemlist, int itemlayoutid, String flag, BaseActivity activity, BaseFragment fragment, BaseViewModel viewModel) {
        GridViewItemAdapter adapter = new GridViewItemAdapter(view.getContext(), itemlist, itemlayoutid);
        adapter.setFlag(flag);
        adapter.setActivity(activity);
        adapter.setFragment(fragment);
        adapter.setViewModel(viewModel);
        view.setAdapter(adapter);
    }


    @BindingAdapter({"height"})
    public static void bindLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }
}
