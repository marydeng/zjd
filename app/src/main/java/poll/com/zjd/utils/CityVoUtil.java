package poll.com.zjd.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.api.OkGoStringCallback;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.dao.HttpRequestDao;
import poll.com.zjd.model.ChildVoBean;
import poll.com.zjd.model.CityBean;
import poll.com.zjd.model.CityListResponse;
import poll.com.zjd.view.picker.entity.Province;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/21
 * 文件描述：省，市，区，城镇 数节点编号工具类
 */
public class CityVoUtil {

    private static CityVoUtil instance;
    private List<ChildVoBean> data;

    private CityVoUtil() {
        data = new ArrayList<>();
        try {
            data = loadFromFile();
            if (ObjectUtils.isEmpty(data)) {
                String json = ConvertUtils.toString(AppContext.getInstance().getApplicationContext().getAssets().open("city3.json"));
                data.addAll(JSON.parseArray(json, ChildVoBean.class));
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }

    }

    public static CityVoUtil getInstance() {
        if (null == instance) {
            instance = new CityVoUtil();
        }
        return instance;
    }

    public String getProvinceNO(String provinceName) {
        String provinceNO = null;
        for (ChildVoBean province : data) {
            if (province.getName().equals(provinceName)) {
                provinceNO = province.getNo();
                break;
            }
        }
        return provinceNO;
    }

    public List<String> getNo(List<String> cityNameList) {
        List<String> NoList = new ArrayList<>();
        if (cityNameList.size() > 0) {
            String provinceName = cityNameList.get(0);
            //获取到省得编号
            for (ChildVoBean province : data) {
                if (province.getName().equals(provinceName)) {
                    NoList.add(province.getNo());
                    //获取到市编号
                    if (cityNameList.size() > 1) {
                        String cityName = cityNameList.get(1);
                        for (ChildVoBean city : province.getChildVo()) {
                            if (city.getName().equals(cityName)) {
                                NoList.add(city.getNo());
                                //获取到区编号
                                if (cityNameList.size() > 2) {
                                    String districtName = cityNameList.get(2);
                                    for (ChildVoBean district : city.getChildVo()) {
                                        if (district.getName().equals(districtName)) {
                                            NoList.add(district.getNo());
                                            //获取街道编号
                                            if (cityNameList.size() > 3) {
                                                String streetName = cityNameList.get(3);
                                                for (ChildVoBean street : district.getChildVo()) {
                                                    if (street.getName().equals(streetName)) {
                                                        NoList.add(street.getNo());
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return NoList;
    }

    public List<String> getCityName(List<String> childVoNoList) {
        List<String> cityNameList = new ArrayList<>();
        if(childVoNoList.size()>0){
        String provinceNo = childVoNoList.get(0);
        //获取到省名称
        for (ChildVoBean province : data) {
            if (province.getNo().equals(provinceNo)) {
                cityNameList.add(province.getName());
                //获取到市名称
                if (childVoNoList.size() > 1) {
                    String cityNo = childVoNoList.get(1);
                    for (ChildVoBean city : province.getChildVo()) {
                        if (city.getNo().equals(cityNo)) {
                            cityNameList.add(city.getName());
                            //获取到区名称
                            if (childVoNoList.size() > 2) {
                                String districtNo = childVoNoList.get(2);
                                for (ChildVoBean district : city.getChildVo()) {
                                    if (district.getNo().equals(districtNo)) {
                                        cityNameList.add(district.getNo());
                                        //获取街道名称
                                        if (childVoNoList.size() > 3) {
                                            String streetNo = childVoNoList.get(3);
                                            for (ChildVoBean street : district.getChildVo()) {
                                                if (street.getNo().equals(streetNo)) {
                                                    cityNameList.add(street.getName());
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
        }}
        return cityNameList;
    }


    public static void saveCityData(Context context) {
        HttpRequestDao httpRequestDao = new HttpRequestDao();
        httpRequestDao.getCityListFile(context, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                Gson gson = new Gson();
                final CityListResponse cityListResponse = gson.fromJson(result, CityListResponse.class);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            saveToFile(cityListResponse.getChildVo());
                        } catch (Exception e) {
                            LogUtils.e(e.toString());
                        }
                    }
                });
                thread.start();
            }
        });
    }

    public static void saveToFile(List<ChildVoBean> cityBeanList) throws
            IOException {

        String jsonStr = JSON.toJSONString(cityBeanList);

        Writer writer = null;
        try {
            OutputStream out = AppContext.getInstance().getApplicationContext().openFileOutput("city3.json",
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonStr);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public List<ChildVoBean> loadFromFile() throws IOException {
        List<ChildVoBean> childVoBeanList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStream in = AppContext.getInstance().getApplicationContext().openFileInput("city3.json");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            childVoBeanList.addAll(JSON.parseArray(jsonString.toString(), ChildVoBean.class));
        } catch (FileNotFoundException e) {
            LogUtils.e(e.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return childVoBeanList;
    }
}
