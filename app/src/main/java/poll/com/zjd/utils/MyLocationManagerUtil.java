package poll.com.zjd.utils;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.baidu.LocationService;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.GetSubcompanyIdResponse;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/26
 * 文件描述：用户选择的位置管理
 */
public class MyLocationManagerUtil {
    private static  MyLocationManagerUtil instance;

    private String addressName;
    private String provinceNO;
    private String cityNo;
    private String districtNo;
    private String streetNo;

    private String subCompanyId;
    private boolean twoHoursDelivery;

    private MyLocationCallBack callBack;

    private LocationService locationService;
    private HttpRequestDao httpRequestDao;

    public interface MyLocationCallBack{
         void onMyLocationChanged();
    }

    private MyLocationManagerUtil() {
        httpRequestDao=new HttpRequestDao();
        locationService = new LocationService(AppContext.getInstance());
        locationService.registerListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                AppContext.getInstance().city = bdLocation.getCity();
                final Address address = bdLocation.getAddress();
                LogUtils.e("2地址:"+address.street+"----"+AppContext.getInstance().city);

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
                List<String> cityNoList=CityVoUtil.getInstance().getNo(locationCityNameList);

                String addressName=null;
                if(StringUtils.isNotEmpty(AppContext.getInstance().city)){
                    addressName=address.city+address.street+address.streetNumber;
                }

                update(addressName,cityNoList);

                locationService.stop();
                locationService.unregisterListener(this);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });

        locationService.start();
    }

    public static MyLocationManagerUtil getInstance(){
        if(instance==null){
            instance=new MyLocationManagerUtil();
        }
        return instance;
    }

    public void setCallBack(MyLocationCallBack callBack) {
        this.callBack = callBack;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getProvinceNO() {
        return provinceNO;
    }

    public void setProvinceNO(String provinceNO) {
        this.provinceNO = provinceNO;
    }

    public String getCityNo() {
        return cityNo;
    }

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo;
    }

    public String getDistrictNo() {
        return districtNo;
    }

    public void setDistrictNo(String districtNo) {
        this.districtNo = districtNo;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public boolean isTwoHoursDelivery() {
        return twoHoursDelivery;
    }

    public void setTwoHoursDelivery(boolean twoHoursDelivery) {
        this.twoHoursDelivery = twoHoursDelivery;
    }

    public void update(String addressName, List<String> cityNoList){
        if(!ObjectUtils.isEmpty(cityNoList)){
            provinceNO=cityNoList.get(0);
            if(cityNoList.size()>1){
                cityNo=cityNoList.get(1);
            }
            if(cityNoList.size()>2){
                districtNo=cityNoList.get(2);
            }
            if(cityNoList.size()>3){
                streetNo=cityNoList.get(3);
            }
        }

        this.addressName=addressName;

        HashMap<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(provinceNO)) {
            map.put("province", provinceNO);
            if (!StringUtils.isBlank(cityNo)) {
                map.put("city", cityNo);
                if (!StringUtils.isBlank(districtNo)) {
                    map.put("area", districtNo);
                    if (!StringUtils.isBlank(streetNo)) {
                        map.put("town", streetNo);
                    }
                }
            }
        }
        JSONObject jsonObject = JsonUtils.convertJSONObject(map);
        httpRequestDao.getSubcompayId(AppContext.getInstance().getApplicationContext(), jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                DialogUtil.hideProgressDialog();
                Gson gson = new Gson();
                GetSubcompanyIdResponse getSubcompanyIdResponse = gson.fromJson(result, GetSubcompanyIdResponse.class);
                if(null!=getSubcompanyIdResponse){
                    subCompanyId=getSubcompanyIdResponse.getSubcompanyId();
                    twoHoursDelivery=getSubcompanyIdResponse.isTwoHoursDelivery();
                    if(null!=callBack){
                        AppContext.getMainHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onMyLocationChanged();
                            }
                        },0);

                    }
                }
            }
        });
    }
}
