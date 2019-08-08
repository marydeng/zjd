package poll.com.zjd.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.model.Response;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import poll.com.zjd.ClassFragmentBinding;
import poll.com.zjd.ClassPopBinding;
import poll.com.zjd.R;
import poll.com.zjd.activity.GoodsDetailActivity;
import poll.com.zjd.activity.SearchActivity;
import poll.com.zjd.activity.SelectorLocationActivity;
import poll.com.zjd.adapter.SelectAdapter;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.BrandBean;
import poll.com.zjd.model.CessorBean;
import poll.com.zjd.model.ClassPriceModel;
import poll.com.zjd.model.FirstClassModel;
import poll.com.zjd.model.GoodBean1;
import poll.com.zjd.model.GoodsClass;
import poll.com.zjd.model.GoodsClass1;
import poll.com.zjd.model.PriceSelectBean;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.PopW;
import poll.com.zjd.viewmodel.ListViewItemAdapter;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  14:58
 * 包名:     poll.com.zjd.fragment
 * 项目名:   zjd
 */

public class ClassFragment extends BaseFragment {

    public static String isHomeAction = "";

    private ViewPager fragmentTwoPager;
    private MagicIndicator magicIndicator;
    private PopW popW;
    private ClassFragmentBinding binding;
    private ClassPopBinding popBinding;
    private RelativeLayout mNonetwork;

    public ObservableList<List<GoodsClass>> leftListDates = new ObservableArrayList<>();        //分类
    public List<List<GoodsClass>> leftListBack = new ArrayList<>();                             //保存分类数据

    public ObservableInt sales = new ObservableInt(0); //0没选 1 下 2上
    public ObservableInt price = new ObservableInt(0); //0没选 1 下 2上
    public ObservableBoolean isFilter = new ObservableBoolean(false);

    public ObservableList<PriceSelectBean> priceList = new ObservableArrayList<>();             //价格筛选
    public ObservableList<PriceSelectBean> locationList = new ObservableArrayList<>();          //产地pop
    public ObservableField<String> obsChangeText = new ObservableField<>("切换品牌");            //切换品牌
    public ObservableField<String> obsminPrice = new ObservableField<>("");      //最低价格
    public ObservableField<String> obsmaxPrice = new ObservableField<>(""); //最高价格
    public ObservableField<String> obslocationL = new ObservableField<>("");      //选择产地
    public ObservableField<String> obslocation = new ObservableField<>("");       //头部收货地址
    public ObservableField<Boolean> obsIsTowHoursGet = new ObservableField<>(false);       //头部两小时达

    private String sort = "";  //排序 1 按销量升序排列 2 按上架时间降序 3按价格升序 4按价格降序 5按上架时间升序 6按（销售价/市场价）升序 7按（销售价/市场价）降序

    private List<GoodsClass> allClassList = new ArrayList<>();  // 一大波class
    private List<FirstClassModel> firstList = new ArrayList<>();
    private List<GoodsClass> mTitleDataList = new ArrayList<>();// 指示器数据
    private List<GoodsClass> thrList = new ArrayList<>(); //分类数据

    public List<String> param = new ArrayList<>();  //分类条件

    public ObservableList<List<GoodBean1>> GoodsList = new ObservableArrayList<>();        //分类商品各种列表

    private int currentPosition = 0;
    private HttpRequestDao httpRequestDao = new HttpRequestDao();

    private PagerViewAdapter mPagerViewAdapter;
    private List<ListViewItemAdapter> GoodListAdapters = new ArrayList<>();
    private List<ListViewItemAdapter> ClassListAdapters = new ArrayList<>();

    private SelectAdapter selectAdapter;  //选择器

    private List<BrandBean> brandBeanList = new ArrayList<>();  //品牌list

    private List<GoodBean1> lastList = new ArrayList<>();    //用于转换
    private List<GoodBean1> killGlist = new ArrayList<>();
    private List<GoodBean1> normalGlist = new ArrayList<>();

    private HashMap<Integer,String> hashMap = new HashMap<>();
    private Gson gson = new Gson();

    private String currentListName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e("创建");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_two, container, false);
        binding.setFragment(this);
        mNonetwork = binding.fraNonetwork;
        if (AppContext.isNetworkAvailable(mActivity)) {
            initDates();
//        initView();
            initPopWindows(inflater);
        }else {
            mNonetwork.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initDates() {
        //清空数据
        allClassList.clear();
        mTitleDataList.clear();
        leftListDates.clear();
        thrList.clear();
        leftListBack.clear();
        priceList.clear();
        LogUtils.e("数据清空");
        //初始化param
        for (int i = 0; i < 10; i++) {
            param.add("");
        }
        //初始化价格区间数据
        httpRequestDao.getPriceList(gContext, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                List<ClassPriceModel> priceModelList = JSON.parseArray(result, ClassPriceModel.class);
                for (ClassPriceModel classPriceModel : priceModelList) {
                    PriceSelectBean p = new PriceSelectBean(classPriceModel.getPriceScope(),false);
                    priceList.add(p);
                }
            }
        });

//        if (priceList.size() == 0) {
//            priceList.addAll(PriceSelectBean.productClassBeanList());
//        }

        if (locationList.size() == 0) {
            locationList.addAll(PriceSelectBean.classLocationBeanList());
        }
        //请求分类
        httpRequestDao.getClass(gContext, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                allClassList = JSON.parseArray(result, GoodsClass.class);
                httpRequestDao.getFirstClass(gContext, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        firstList = JSON.parseArray(result,FirstClassModel.class);
                        LogUtils.e("分类数据:" + allClassList.size());
                        if (allClassList.size() > 0) {
                            buildData();  //组装分配数据
                            initView();
                        }
                    }
                });

            }
        });
        //拿品牌
//        httpRequestDao.getBrand(gContext, new OkGoStringCallback() {
//            @Override
//            public void onSuccessEvent(String result) {
//                brandBeanList = JSON.parseArray(result,BrandBean.class);
//                LogUtils.e("品牌数据"+brandBeanList.size());
//
//            }
//        });
    }

    private void buildData() {
        LogUtils.e("data");
        GoodsClass one = new GoodsClass();
        one.setCatName("一键选酒");
        one.setStructName("");
        mTitleDataList.add(one);
        for (GoodsClass aClass : allClassList) {
            if (aClass.getLevel() == 2) {
                //最上面那排
                mTitleDataList.add(aClass);
            }
            if (aClass.getLevel() == 3) {
                thrList.add(aClass);
            }
        }
        //一键选酒数据填充
        List<GoodsClass> glistOne = new ArrayList<>();
        for (int i=0;i<firstList.size();i++) {
            GoodsClass g = new GoodsClass();
            if (i==0) g.setSelect(true);  //第一个设置红色
            g.setKeywords(firstList.get(i).getTagName());
            glistOne.add(g);
        }
        leftListDates.add(glistOne);

        for (int i=0;i<mTitleDataList.size();i++){
            List<GoodsClass> glist = new ArrayList<>();
            GoodsClass g = new GoodsClass();
            g.setSelect(true);
            g.setKeywords("全部");
            glist.add(g);
            for (GoodsClass thr : thrList) {
                if (thr.getStructName().contains(mTitleDataList.get(i).getStructName())) {
                    glist.add(thr);
                }
            }
            if (i==0) continue;
            leftListDates.add(glist);
        }

        leftListBack.addAll(leftListDates);
        //填充null
        for (GoodsClass aClass : mTitleDataList) {
            GoodsList.add(null);
            GoodListAdapters.add(null);
            ClassListAdapters.add(null);
        }
    }

    private void initView() {
        LogUtils.e("view");
        magicIndicator = binding.magicIndicator;
        fragmentTwoPager = binding.fragmentTwoPager;

        final CommonNavigator commonNavigator = new CommonNavigator(gContext);
        commonNavigator.setEnablePivotScroll(true);  //设置可以滑动
        if (mTitleDataList.size() < 5) {
            commonNavigator.setAdjustMode(true);     //设置自适应屏幕(设置后不可滑动)
        }
        commonNavigator.setAdapter(new SelectAdapter(mTitleDataList, fragmentTwoPager));
        magicIndicator.setNavigator(commonNavigator);

//        mPagerViewAdapter = new PagerViewAdapter();
        fragmentTwoPager.setAdapter(new PagerViewAdapter());

        ViewPagerHelper.bind(magicIndicator, fragmentTwoPager);

        fragmentTwoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.e("当前位置" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    currentPosition = fragmentTwoPager.getCurrentItem();
                    //设置滑动ViewPager导航同步变化
                    if (StringUtils.isNotEmpty(hashMap.get(currentPosition))){
                        obsChangeText.set("切换分类");
                    }else {
                        obsChangeText.set("切换品牌");
                    }
                    itemClick(null, 0);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppContext.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null!=mTitleDataList && mTitleDataList.size()>0){
                            if (currentPosition==0){
                                String cat = null;
                                try {
                                    cat = leftListDates.get(0).get(0).getKeywords();
                                }catch (Exception e){

                                }
                                filterGoods(cat, mTitleDataList.get(currentPosition).getCatName(),null, null);//刷一波
                            }else {
                                filterGoods(null, mTitleDataList.get(currentPosition).getCatName(),null, null);//刷一波
                            }

                        }
                        if (null != obslocation)
                            obslocation.set(MyLocationManagerUtil.getInstance().getAddressName());
                        if(null!=obsIsTowHoursGet){
                            obsIsTowHoursGet.set(MyLocationManagerUtil.getInstance().isTwoHoursDelivery());
                        }
                    }
                }, 2000);
            }
        }).start();

        if (null != obslocation)
            obslocation.set(MyLocationManagerUtil.getInstance().getAddressName());
        if(null!=obsIsTowHoursGet){
            obsIsTowHoursGet.set(MyLocationManagerUtil.getInstance().isTwoHoursDelivery());
        }
    }

    private void initPopWindows(LayoutInflater inflater) {
        if (popBinding == null) {
            popBinding = DataBindingUtil.inflate(inflater, R.layout.pop_class_filter, (ViewGroup) binding.getRoot(), false);
            popBinding.setFragment(this);
            popW = new PopW(mActivity, popBinding.getRoot(), 0);
        }
    }


    //左边分类的点击事件
    public synchronized void dismiss(View view) {
        popW.dismiss();
    }

    //左边分类的点击事件
    public synchronized void itemClick(View view, int index) {
        for (int i = 0; i < leftListDates.get(currentPosition).size(); i++) {
            leftListDates.get(currentPosition).get(i).setSelect(false);
        }
        leftListDates.get(currentPosition).get(index).setSelect(true);

        filterGoods(leftListDates.get(currentPosition).get(index).getKeywords(),  mTitleDataList.get(currentPosition).getCatName(),null, null);
    }

    //销量
    public synchronized void salesClick(View view) {
        price.set(0); //0是没有
        switch (sales.get()) {
            case 0:
                sales.set(1);  //向下
                sort = "1";
                break;
            case 1:
                sales.set(2);  //向上
                sort = "8";
                break;
            case 2:
                sales.set(1);  //向下
                sort = "1";
                break;
        }
        try{
            filterGoods(currentListName, mTitleDataList.get(currentPosition).getCatName(),null, null);//刷一波
        }catch (Exception e){

        }
    }

    //价格
    public synchronized void priceClick(View view) {
        sales.set(0);
        switch (price.get()) {
            case 0:
                price.set(1);   //升
                sort = "4";
                break;
            case 1:
                price.set(2);   //降
                sort = "3";
                break;
            case 2:
                price.set(1);
                sort = "4";
                break;
        }
        filterGoods(currentListName, mTitleDataList.get(currentPosition).getCatName(),null, null);//刷一波
    }

    //跳转到商品详情
    public synchronized void toProductDetail(View view, GoodBean1 item) {
        Bundle bundle = new Bundle();
        bundle.putString(GoodsDetailActivity.GOODS_ID_EXTRA, item.getId());
        appContext.startActivity(gContext, GoodsDetailActivity.class, bundle);
    }

    //筛选
    public synchronized void filterClick(View view) {
        popW.showAtLocation(binding.getRoot(), Gravity.RIGHT, 0, 0);
    }

    //价格区间
    public synchronized void itemPriceClick(View view, PriceSelectBean item) {
        selectPop(priceList, item);
    }

    //产地
    public synchronized void itemLocationClick(View view, PriceSelectBean item) {
        selectPop(locationList, item);
    }

    //确定筛选
    public synchronized void itemConfirmClick(View view) {
        popW.dismiss();
        if (StringUtils.isNotEmpty(obslocationL.get())||StringUtils.isNotEmpty(obsmaxPrice.get())){
            isFilter.set(true);
        }
        filterGoods(currentListName, mTitleDataList.get(currentPosition).getCatName(),null, null);//刷一波
    }

    //重置筛选
    public synchronized void itemResetClick(View view) {
        reSet();
    }

    //切换品牌
    public synchronized void changeClick(View view) {
        if (currentPosition!=0) { //第一个不要切换
            final List<GoodsClass> gs = leftListDates.get(currentPosition);
            if (obsChangeText.get().equals("切换品牌")) {
                obsChangeText.set("切换分类");
                hashMap.put(currentPosition, gson.toJson(gs));
                gs.clear();
                final List<GoodsClass> brand = new ArrayList<>();
                GoodsClass goodsc = new GoodsClass();
                goodsc.setKeywords("全部");
                brand.add(goodsc);
                GoodsClass aClass = mTitleDataList.get(currentPosition);
                //拿品牌
                httpRequestDao.getBrandWithClass(gContext, aClass.getStructName(), aClass.getId(), new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        brandBeanList = JSON.parseArray(result, BrandBean.class);
                        LogUtils.e("品牌数据" + brandBeanList.size());
                        for (BrandBean bean : brandBeanList) {
                            GoodsClass goodsClass = new GoodsClass();
                            goodsClass.setKeywords(bean.getBrandName());
                            brand.add(goodsClass);
                        }
                        gs.addAll(brand);
                        ClassListAdapters.get(currentPosition).notifyDataSetChanged();
                    }
                });
            } else {
                gs.clear();
                obsChangeText.set("切换品牌");
                List<GoodsClass> gss = gson.fromJson(hashMap.get(currentPosition), new TypeToken<List<GoodsClass>>() {
                }.getType());
                hashMap.remove(currentPosition);
                gs.addAll(gss);
                ClassListAdapters.get(currentPosition).notifyDataSetChanged();
            }
        }
    }


    //地址
    public synchronized void locationClick(View view) {
        appContext.startActivity(mActivity, SelectorLocationActivity.class, null);
    }

    //搜索
    public synchronized void searchClick(View view) {
        appContext.startActivity(mActivity, SearchActivity.class, null);
    }


    //改变选择筛选条件的状态
    private void selectPop(List<PriceSelectBean> list, PriceSelectBean item) {
        for (PriceSelectBean bean : list) {
            bean.obsSelect.set(false);
        }
        item.obsSelect.set(true);
        if (item.getPrice().contains("-") || item.getPrice().contains("元")) {
//            String[] s = item.getPrice().split("-");
//            obsminPrice.set(s[0]);
//            obsmaxPrice.set(s[1]);
            obsmaxPrice.set(item.getPrice());
        } else {
            obslocationL.set(item.getPrice());
        }
    }

    //重置
    private void reSet() {
        isFilter.set(false);
        obslocationL.set("");
        obsmaxPrice.set("");
        for (PriceSelectBean bean : priceList) {
            bean.obsSelect.set(false);
        }
        for (PriceSelectBean bean : locationList) {
            bean.obsSelect.set(false);
        }
    }

    public class PagerViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTitleDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LogUtils.e("刷"+position);
            View view = View.inflate(gContext, R.layout.fragment_two_layout, null);

            ListView mLeftList = (ListView) view.findViewById(R.id.frg_two_typeList);
            final ListView mRightList = (ListView) view.findViewById(R.id.frg_two_productList);

            ListViewItemAdapter leftAdapter = new ListViewItemAdapter(gContext, leftListDates.get(position), R.layout.fragment_two_layout_left);
            ClassListAdapters.set(position,leftAdapter);
            leftAdapter.setFragment(ClassFragment.this);
            mLeftList.setAdapter(leftAdapter);

            if (mTitleDataList.size() > 0) {
                param.set(0, mTitleDataList.get(position).getCatName());  //二级分类
                param.set(1, "");    //三级分类
                param.set(2, "");    //品牌
                param.set(3, obslocationL.get());    //产地
                param.set(4, sort);    //排序 1 按销量升序排列 2 按上架时间降序 3按价格升序 4按价格降序 5按上架时间升序 6按（销售价/市场价）升序 7按（销售价/市场价）降序
                param.set(5, obsmaxPrice.get());     //价格区间

//            param.set(5,obsminPrice.get()+"-"+obsmaxPrice.get()+"元");    //价格区间 1-500
                LogUtils.e("请求列表数据");
                httpRequestDao.getProductList(mActivity, param, new OkGoStringCallback() {
                    @Override
                    public void onSuccessEvent(String result) {
                        GoodsClass1 gs = new Gson().fromJson(result, GoodsClass1.class);
                        LogUtils.e("数据返回"+gs.getPojoJson().size());
                        GoodsList.set(position, gs.getPojoJson());
//
                        ListViewItemAdapter rightAdapter = new ListViewItemAdapter(gContext, GoodsList.get(position), R.layout.fragment_two_layout_right);

                        GoodListAdapters.set(position, rightAdapter);

                        rightAdapter.setFragment(ClassFragment.this);
                        mRightList.setAdapter(rightAdapter);
                    }
                });
            }
            container.addView(view);
            return view;
        }
    }


    //筛选商品list
    private void filterGoods(String thrClass, String secClass,String brand, String location) {
        lastList.clear();
        killGlist.clear();
        normalGlist.clear();
        DialogUtil.showProgressDialog(mActivity, null, null);
        List<String> param = new ArrayList<>();  //分类条件
        for (int i = 0; i < 7; i++) {
            param.add("");
        }

        if (currentPosition!=0) param.add(0, secClass);  //二级分类(固定)

        if (currentPosition==0){
            param.set(6, thrClass);  //一键选酒列表
        }else {
            if (isClassItem()){
                if (StringUtils.isNotEmpty(thrClass) && !thrClass.equals("全部")) {
                    param.set(1, thrClass);          //三级分类
                }
            }else {
                if (StringUtils.isNotEmpty(thrClass) && !thrClass.equals("全部")) {
                    param.set(2, thrClass);  //品牌
                }
            }
        }


//        if (StringUtils.isNotEmpty(brand) && !thrClass.equals("全部")) {
//            param.set(2, brand);             //品牌
//        }

        param.set(3, obslocationL.get());  //产地
        param.set(4, sort);//排序
        param.set(5, obsmaxPrice.get());   //价格区间

        currentListName = thrClass;

        httpRequestDao.getProductList(mActivity, param, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                GoodsClass1 gs = new Gson().fromJson(result, GoodsClass1.class);
                if (mTitleDataList.size()==0) return;

                LogUtils.e("共计:" + gs.getTotal() + mTitleDataList.get(currentPosition).getCatName() + "位置-->" + currentPosition);

                List<GoodBean1> baseList = gs.getPojoJson();
                List<CessorBean> killList = gs.getCessordata();

                boolean isKill;
                for (GoodBean1 normal : baseList) {
                    isKill = false;
                    for (CessorBean kill : killList) {
                        if (normal.getId().equals(kill.getCommodityId())){
                            normal.setKill(true);
                            normal.setProdPrice(kill.getSnappingUpPrice());
                            killGlist.add(normal);
                            isKill = true;
                        }
                    }
                    if (!isKill){
                        normalGlist.add(normal);
                    }
                }
                if (killGlist.size()!=0){
                    lastList.addAll(killGlist);
                }
                lastList.addAll(normalGlist);
                LogUtils.e("最终长度:"+lastList.size());

                List<GoodBean1> good = GoodsList.get(currentPosition);

                if (good==null){
                    GoodsList.set(currentPosition, lastList);
                }else {
                    GoodsList.get(currentPosition).clear();
                    GoodsList.get(currentPosition).addAll(lastList);
                }

                if(GoodListAdapters.get(currentPosition)!=null){
                    GoodListAdapters.get(currentPosition).notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Response<String> response) {
                DialogUtil.hideProgressDialog();
                ToastUtils.showToast(mActivity, "网络异常", 1);
            }
        });
    }

    //当前是否分类列表
    private boolean isClassItem(){
        return obsChangeText.get().equals("切换品牌");
    }

    @Override
    public void onLocationChanged() {
        //没用了
    }

    @Override
    public void onChangeToFront() {
        super.onChangeToFront();
        if (null != obslocation)
            obslocation.set(MyLocationManagerUtil.getInstance().getAddressName());
        if(null!=obsIsTowHoursGet){
            obsIsTowHoursGet.set(MyLocationManagerUtil.getInstance().isTwoHoursDelivery());
        }
        if (StringUtils.isNotEmpty(isHomeAction)){
            for (int i=0;i<mTitleDataList.size();i++){
                if (isHomeAction.equals(mTitleDataList.get(i).getCatName())){
                    fragmentTwoPager.setCurrentItem(i);
                }
            }
            isHomeAction = null;
        }
    }

}
