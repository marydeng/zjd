package poll.com.zjd.api;

import android.content.Context;

import com.lzy.okgo.model.Response;

import java.util.HashMap;

import poll.com.zjd.utils.JsonUtils;
import poll.com.zjd.utils.LogUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/19  16:54
 * 包名:     poll.com.zjd.api
 * 项目名:   zjd
 */

public class TestApi {

    public static void testPost(Context context){
        HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("consigneeAddress", "sssssss");
        requestParams.put("userName", "mary");
        requestParams.put("province", "root-20");
        requestParams.put("town", "root-20-3-7-1");
        requestParams.put("area", "root-20-3-7");
        requestParams.put("productNos", "105431898095001-2");
        requestParams.put("city", "root-20-3");

        org.json.JSONObject jsonObject = JsonUtils.convertJSONObject(requestParams);
        LogUtils.e("test请求POST测试start-->"+jsonObject.toString());
        HttpRequestApi.post(context, "http://120.24.157.236:8080/api/order/orderCheck.sc", jsonObject, new OkGoStringCallback() {
            @Override
            public void onSuccessEvent(String result) {
                LogUtils.e("test请求POST测试success-->"+result);
            }

            @Override
            public void onError(Response<String> response) {
                LogUtils.e("test请求POST测试fault-->"+response.message()+"--code--"+response.code());
            }
        });
    }
}
