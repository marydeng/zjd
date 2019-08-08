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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.adapter.superadapter.SuperAdapter;
import poll.com.zjd.adapter.superadapter.SuperViewHolder;
import poll.com.zjd.annotation.FmyClickView;
import poll.com.zjd.annotation.FmyContentView;
import poll.com.zjd.annotation.FmyViewView;
import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.baidu.LocationService;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.fragment.HomeFragment;
import poll.com.zjd.model.AddressBean;
import poll.com.zjd.model.AddressListBean;
import poll.com.zjd.utils.AddressUtil;
import poll.com.zjd.utils.CityVoUtil;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.MyLocationManagerUtil;
import poll.com.zjd.utils.StringUtils;
import poll.com.zjd.utils.ToastUtils;
import poll.com.zjd.view.ListViewInScroll;
import poll.com.zjd.view.picker.AddressPickTask;
import poll.com.zjd.view.picker.entity.City;
import poll.com.zjd.view.picker.entity.County;
import poll.com.zjd.view.picker.entity.Province;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/23  10:10
 * 包名:     poll.com.zjd.activity
 * 项目名:   zjd
 */
@FmyContentView(R.layout.activity_selector_location)
public class SelectorLocationActivity extends BaseActivity implements View.OnClickListener {
    public static final String LOCATIONINFO = "locationInfo";
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";

    @FmyViewView(R.id.head_text)
    private TextView mHead;
    @FmyViewView(R.id.head_text_right)
    private TextView mHeadRight;
    @FmyViewView(R.id.head_back)
    private ImageView mBack;
    @FmyViewView(R.id.location_city)
    private TextView mCity;
    @FmyViewView(R.id.search_location)
    private TextView mLocation;
    @FmyViewView(R.id.location_my_list)
    private ListViewInScroll mMyLocationList;
    @FmyViewView(R.id.location_bd_list)
    private ListViewInScroll mBdLocationList;
    @FmyViewView(R.id.search_key)
    private ListView mSearchKeyWordList;
    @FmyViewView(R.id.search_edt)
    private EditText mEdSearch;
    @FmyViewView(R.id.search_myLo)
    private LinearLayout mMyLocation;
    @FmyViewView(R.id.choose_city_rlt)
    private RelativeLayout chooseCityRlt;


    private List<Poi> poiList = new ArrayList<>();       //定位的地址
    private List<PoiInfo> searchPoi = new ArrayList<>(); //搜索地址
    private List<AddressBean> myLocationList = new ArrayList<>();  //我的地址

    private SuperAdapter myLocation;
    private SuperAdapter BdLocation;
    private SuperAdapter searchAdapter;

    private LocationService locationService;
    private PoiSearch mPoiSearch;
    private boolean isSearchBack = true;
    private HttpRequestDao httpRequestDao;

    //定位的城市编码
    private List<String> locationCityNoList;
    //当前选择位置的城市编码
    private List<String> cityNoList;
    //地理编码
    private GeoCoder mSearchGeoCoder = null;
    //选择的poi AddressName
    private String poiAddressName;
    //是否修改过城市
    private boolean isChangedCity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBarManager.setStatusBarView();  //设置沉浸式

        initData();
        initView();

    }

    private void initData() {
        // 初始化搜索模块，注册事件监听
        mSearchGeoCoder = GeoCoder.newInstance();
        mSearchGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(SelectorLocationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                ArrayList<String> cityNameList = new ArrayList<>();
                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().province)) {
                    cityNameList.add(reverseGeoCodeResult.getAddressDetail().province);
                }
                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().city)) {
                    cityNameList.add(reverseGeoCodeResult.getAddressDetail().city);
                }
                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().district)) {
                    cityNameList.add(reverseGeoCodeResult.getAddressDetail().district);
                }

                if (!StringUtils.isBlank(reverseGeoCodeResult.getAddressDetail().street)) {
                    cityNameList.add(reverseGeoCodeResult.getAddressDetail().street);
                }
                cityNoList = CityVoUtil.getInstance().getNo(cityNameList);
                responseBack(poiAddressName);
            }
        });
    }

    @FmyClickView({R.id.head_back, R.id.head_text_right, R.id.choose_city_rlt,R.id.current_location_linear})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;
            case R.id.head_text_right:
                appContext.startActivityForResult(this, MyAddressEditActivity.class, 0, null);
                break;
            case R.id.choose_city_rlt:
                showCityChooseDialog();
                break;
            case R.id.current_location_linear:
                cityNoList=locationCityNoList;
                responseBack(mLocation.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    private void showCityChooseDialog() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastUtils.showToast(SelectorLocationActivity.this, "数据初始化失败", Toast.LENGTH_LONG);
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                AppContext.getInstance().chooseCity = city.getAreaName();
                mCity.setText(city.getAreaName());
                isChangedCity = true;
                cityNoList=new ArrayList<>();
                cityNoList.add(province.getNo());
                cityNoList.add(city.getNo());
            }
        });
        task.execute("", "");
    }

    private void getLocation() {
        locationService = new LocationService(AppContext.getInstance());
        locationService.registerListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                AppContext.getInstance().city = bdLocation.getCity();
                final Address address = bdLocation.getAddress();
                poiList = bdLocation.getPoiList();
                LogUtils.e("2地址:" + address.street + "----" + AppContext.getInstance().city);

                for (Poi poi : poiList) {
                    LogUtils.e("2附近地址:" + poi.getName() + "------" + poi.getRank());
                }

                if (StringUtils.isNotEmpty(AppContext.getInstance().city)) {

                    AppContext.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (poiList.size() > 0) {
                                BdLocation.setData(poiList);
                                BdLocation.notifyDataSetHasChanged();
                            }
                            mLocation.setText(address.city + address.street + address.streetNumber);
                        }
                    });

                    locationService.stop();
                }


                //更新当前地址信息
                List<String> locationCityNameList = new ArrayList<>();
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
                locationCityNoList = CityVoUtil.getInstance().getCityName(locationCityNameList);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });

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
                isSearchBack = true;
                if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
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
                .city(mCity.getText().toString().trim())
                .keyword(keyWord)
                .pageNum(0));
    }

    //获取个人地址list
    private void getMyLocationList() {
        LogUtils.i("获取个人地址");
        if (httpRequestDao == null) httpRequestDao = new HttpRequestDao();
        httpRequestDao.getUserAddress(mContext, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                LogUtils.i("获取个人地址成功...." + result);
                AddressListBean ad = new Gson().fromJson(result, AddressListBean.class);
                myLocationList = ad.getData();
                myLocation.setData(myLocationList);
                AppContext.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myLocation.notifyDataSetHasChanged();
                    }
                });
            }
        });
    }

    private void initView() {
        mHead.setText("选择收货地址");
        mHeadRight.setText("新增地址");

        if (StringUtils.isBlank(AppContext.getInstance().chooseCity)) {
            mCity.setText(AppContext.getInstance().city);
        } else {
            mCity.setText(AppContext.getInstance().chooseCity);
        }

        myLocation = new SuperAdapter<AddressBean>(mContext, myLocationList, R.layout.adapter_location_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition,final AddressBean item) {
                holder.setText(R.id.location_userName, item.getReceivingName());
                holder.setText(R.id.location_userPhone, item.getReceivingMobilePhone());
                holder.setText(R.id.location_name, AddressUtil.convertAddressTypeName(item.getConcatType()));

                String addressStr = item.getProvince() + item.getCity() + item.getReceivingAddress() + item.getSpecificAddress();
                String indentation = getString(R.string.indentation_address);
                SpannableString spannableString = new SpannableString(indentation + addressStr);
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.transparent)), 0, indentation.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setText(R.id.location_address, spannableString);

                holder.setOnClickListener(R.id.address_root,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        LogUtils.i("v.getId="+v.toString());
                        cityNoList = new ArrayList<>();
                        if (!StringUtils.isBlank(item.getReceivingProvince())) {
                            cityNoList.add(item.getReceivingProvince());
                            if (!StringUtils.isBlank(item.getReceivingCity())) {
                                cityNoList.add(item.getReceivingCity());
                                if (!StringUtils.isBlank(item.getReceivingDistrict())) {
                                    cityNoList.add(item.getReceivingDistrict());
                                    if (!StringUtils.isBlank(item.getReceivingStreet())) {
                                        cityNoList.add(item.getReceivingStreet());
                                    }
                                }
                            }
                        }
                        responseBackX(item.getReceivingAddress() + item.getSpecificAddress(),item.getReceivingName(),item.getReceivingMobilePhone(),item.getProvince(),item.getCity(),item.getDistrict());
                    }
                });

            }
        };

        mMyLocationList.setAdapter(myLocation);

        BdLocation = new SuperAdapter<Poi>(mContext, poiList, R.layout.adapter_location_bd_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final Poi item) {
                holder.setText(R.id.location_bd_text, item.getName());
                holder.setOnClickListener(R.id.location_RV, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cityNoList = locationCityNoList;
                        responseBack(item.getName());
                    }
                });
            }
        };

        mBdLocationList.setAdapter(BdLocation);

        searchAdapter = new SuperAdapter<PoiInfo>(mContext, searchPoi, R.layout.adapter_location_bd_list) {
            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, final PoiInfo item) {
                holder.setText(R.id.location_bd_text, item.name);
                holder.setOnClickListener(R.id.location_RV, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        poiAddressName = item.name;
                        // 反Geo搜索
                        mSearchGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(item.location));
                    }
                });
            }
        };

        mSearchKeyWordList.setAdapter(searchAdapter);

        getLocation();//定位
        getMyLocationList();//获取个人地址
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
                if (StringUtils.isNotEmpty(s.toString().trim())) {
                    if (mMyLocation.isShown()) mMyLocation.setVisibility(View.GONE);
                    isSearchBack = false;
                    searchLocation(s.toString().trim());
                } else {
                    if (!mMyLocation.isShown()) mMyLocation.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //返回地址
    private void responseBack(String location) {
        Intent intent = new Intent();
        intent.putExtra(LOCATIONINFO, location);
        this.setResult(HomeFragment.SELECTADDRESS, intent);
        MyLocationManagerUtil.getInstance().update(location, cityNoList);
        back();
    }
    //返回地址
    private void responseBackX(String location,String userName,String phone,String province,String city,String district) {
        Intent intent = new Intent();
        intent.putExtra(LOCATIONINFO, location);
        intent.putExtra(USERNAME, userName);
        intent.putExtra(PHONE, phone);
        intent.putExtra(PROVINCE, province);
        intent.putExtra(CITY, city);
        intent.putExtra(DISTRICT, district);
        this.setResult(HomeFragment.SELECTADDRESS, intent);
        MyLocationManagerUtil.getInstance().update(location, cityNoList);
        back();
    }

    @Override
    protected void onDestroy() {
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }
        if (locationService != null) {
            locationService.stop();
        }
        super.onDestroy();
    }

    @Override
    public synchronized void onBackPressed() {
        if (isChangedCity) {
            MyLocationManagerUtil.getInstance().update(mCity.getText().toString().trim(), cityNoList);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MyAddressActivity.ResultCode.add_address_success ) {
            //请求数据
            getMyLocationList();
        }
    }
}
