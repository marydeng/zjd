package poll.com.zjd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.baidu.LocationService;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.StringUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/19
 * 文件描述：地址搜寻页面
 */
@FmyContentView(R.layout.activity_my_address_search)
public class MyAddressSearchActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MyAddressSearchActivity.class.getSimpleName();
    public static final String EXTRA_CITY = "MyAddressSearchActivity.city";
    @FmyViewView(R.id.head_back)
    private ImageView backImageView;
    @FmyViewView(R.id.head_text)
    private TextView titleTextView;
    @FmyViewView(R.id.my_address_location_list)
    private ListView mLocation;
    @FmyViewView(R.id.my_address_search_list)
    private ListView mSearch;
    @FmyViewView(R.id.search_edt)
    private EditText mEdSearch;
    @FmyViewView(R.id.notice_txt)
    private TextView noticeTxt;


    private List<Poi> poiList = new ArrayList<>();       //定位的地址
    private SuperAdapter superAdapter, searchAdapter;
    private LocationService locationService;
    private PoiSearch mPoiSearch;
    private boolean isSearchBack = true;
    List<PoiInfo> searchPoi = new ArrayList<>(); //搜索地址
    private BaiduListener listener;

    //地理编码
    private GeoCoder mSearchGeoCoder = null;
    private String cityName;
    private String location;
    private ArrayList<String> locationCityNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initView() {

        superAdapter = new SuperAdapter<Poi>(mContext, poiList, R.layout.adapter_location_bd_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final Poi item) {
                holder.setText(R.id.location_bd_text, item.getName());
                holder.setOnClickListener(R.id.location_RV, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        responseBack(item.getName(), locationCityNameList);
                    }
                });
            }
        };

        searchAdapter = new SuperAdapter<PoiInfo>(mContext, searchPoi, R.layout.adapter_location_bd_list_x) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final PoiInfo item) {

                holder.setText(R.id.location_search_text, item.name);
                holder.setText(R.id.location_bd_text, item.address);
                holder.setOnClickListener(R.id.location_RV, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        location = item.name;
                        // 反Geo搜索
                        mSearchGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(item.location));
                    }
                });

            }
        };

        mLocation.setAdapter(superAdapter);
        mSearch.setAdapter(searchAdapter);

        if (cityName.equals(AppContext.getInstance().city)) {
            mLocation.setVisibility(View.VISIBLE);
            getLocation();  //获取定位地址
        }

        //Edict的变化监听
        mEdSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mLocation.isShown()) {
                    mLocation.setVisibility(View.GONE);
                }
                if (StringUtils.isNotEmpty(s.toString().trim())) {
                    isSearchBack = false;
                    searchLocation(s.toString().trim());
                } else {
                    noticeTxt.setVisibility(View.VISIBLE);
                    String notice = getString(R.string.input_keyword_in_city, cityName);
                    int start = notice.indexOf(String.valueOf(cityName));
                    int end = start + String.valueOf(cityName).length();
                    SpannableString spannableString = new SpannableString(notice);
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_e54956)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    noticeTxt.setText(spannableString);
                    mSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        titleBarManager.setStatusBarView();  //设置沉浸式
        titleTextView.setText(R.string.address_search);

        getDataFromIntent();
        String notice = getString(R.string.input_keyword_in_city, cityName);
        int start = notice.indexOf(String.valueOf(cityName));
        int end = start + String.valueOf(cityName).length();
        SpannableString spannableString = new SpannableString(notice);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_e54956)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        noticeTxt.setText(spannableString);

        // 初始化搜索模块，注册事件监听
        mSearchGeoCoder = GeoCoder.newInstance();
        mSearchGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MyAddressSearchActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                ArrayList<String> cityNo = new ArrayList<>();
                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().province)) {
                    cityNo.add(reverseGeoCodeResult.getAddressDetail().province);
                }
                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().city)) {
                    cityNo.add(reverseGeoCodeResult.getAddressDetail().city);
                }
                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().district)) {
                    cityNo.add(reverseGeoCodeResult.getAddressDetail().district);
                }

                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().street)) {
                    cityNo.add(reverseGeoCodeResult.getAddressDetail().street);
                }
                responseBack(location, cityNo);
            }
        });
    }

    private void getDataFromIntent() {
        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (null != bundle) {
                cityName = bundle.getString(EXTRA_CITY);
            }
        }
    }

    private class BaiduListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            AppContext.getInstance().city = bdLocation.getCity();
            final Address address = bdLocation.getAddress();
            poiList = bdLocation.getPoiList();
            LogUtils.e("2地址:" + address.street + "----" + AppContext.getInstance().city);
            locationCityNameList = new ArrayList<>();
            if (!StringUtils.isBlank(address.province)) {
                locationCityNameList.add(address.province);
            }
            if (!StringUtils.isBlank(address.city)) {
                locationCityNameList.add(address.city);
            }
            if (!StringUtils.isBlank(address.district)) {
                locationCityNameList.add(address.district);
            }

            if (!StringUtils.isBlank(address.street)) {
                locationCityNameList.add(address.street);
            }


            for (Poi poi : poiList) {
                LogUtils.e("2附近地址:" + poi.getName() + "------" + poi.getRank());
            }

            if (StringUtils.isNotEmpty(AppContext.getInstance().city)) {

                AppContext.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (poiList.size() > 0) {
                            superAdapter.setData(poiList);
                            superAdapter.notifyDataSetHasChanged();
                        }
                    }
                });

                locationService.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //百度定位
    private void getLocation() {
        locationService = new LocationService(AppContext.getInstance());
        listener = new BaiduListener();
        locationService.registerListener(listener);
        locationService.start();
    }

    //搜索地址
    private void searchLocation(String keyWord) {
        if (mPoiSearch == null) {
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {

                if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                    noticeTxt.setVisibility(View.GONE);
                    mSearch.setVisibility(View.VISIBLE);
                    for (PoiInfo info : poiResult.getAllPoi()) {
                        LogUtils.e("搜索地址:" + info.address + "----" + info.name);
                    }
                    searchAdapter.setData(poiResult.getAllPoi());
                    AppContext.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            searchAdapter.notifyDataSetHasChanged();
                        }
                    });
                } else {
                    noticeTxt.setVisibility(View.VISIBLE);
                    noticeTxt.setText(getString(R.string.no_relative_result));
                    mSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                LogUtils.e("poiDetailResult" + poiDetailResult);
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                LogUtils.e("poiIndoorResult" + poiIndoorResult);
            }
        });

        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityName)
                .keyword(keyWord)
                .pageNum(0));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null) {
            locationService.stop();
            locationService.unregisterListener(listener);
        }
        if (null != mPoiSearch) {
            mPoiSearch.destroy();
        }
    }

    //返回地址
    private void responseBack(String location, ArrayList<String> cityNO) {
        Intent intent = new Intent();
        intent.putExtra(MyAddressEditActivity.EXTRA_ADDRESS_FROM_SEARCH, location);
        intent.putStringArrayListExtra(MyAddressEditActivity.EXTRA_CITY_NO, cityNO);
        this.setResult(MyAddressEditActivity.RESULT_CODE_ADDRESS_SEARCH, intent);
        onBackPressed();
    }
}
