package poll.com.zjd.utils;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import poll.com.zjd.model.ShoppiingCartBean;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/30
 * 文件描述：Json 转换工具类
 */
public class JsonUtils {

    //Hashmap转json
    public static JSONObject convertJSONObject2(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        Set<Map.Entry<String, Object>> m = map.entrySet();
        for (Map.Entry<String, Object> entry : m) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
        return jsonObject;
    }

    //Hashmap转json
    public static JSONObject convertJSONObject(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        Set<Map.Entry<String, String>> m = map.entrySet();
        for (Map.Entry<String, String> entry : m) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
        return jsonObject;
    }

    // string 转 JsonObject
    public static JSONObject convertJSONObject(Object object) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(JSON.toJSONString(object));
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }

        return jsonObject;
    }

    //json 转HashMap
    public static Map<String, ShoppiingCartBean> jsonToHashMap(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, new TypeToken<Map<String, ShoppiingCartBean>>() {
        }.getType());

    }

    public static String mapToStr(Map<String, ShoppiingCartBean> map) {
        Gson gson = new Gson();
        return gson.toJson(map, new TypeToken<Map<String, ShoppiingCartBean>>() {
        }.getType());
    }

}
