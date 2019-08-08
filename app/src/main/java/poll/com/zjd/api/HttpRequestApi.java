package poll.com.zjd.api;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import poll.com.zjd.app.AppContext;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.StringUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/12  20:41
 * 包名:     poll.com.zjd.api
 * 项目名:   zjd
 */

public class HttpRequestApi {


    //统一的头这里设置
    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        if(!StringUtils.isBlank(AppContext.getInstance().token)) {
//        headers.put(Urls.TOKEN, "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ3d3cuempkLmNvbSIsImlhdCI6MTUwMjAwOTIzMywiZXhwIjoxNTEwNjQ5MjMzLCJwaG9uZSI6IjE1MDEyNzE1NTMxIn0.C3_u2YY7G_Gv0QkDCSAf7GoyFJg00RZY4xyyV2a2LtvKT86vmPUxaLpsfk5p3jBIAlj3octW_RZnWbRH86xeEg");}
        headers.put(Urls.TOKEN,AppContext.getInstance().token);
        }
        return headers;
    }

    //String  GET
    public static void get(Context context, String url, JSONObject jsonObject, StringCallback callBack){
        try {
            LogUtils.i("Get-->"+url);
            LogUtils.i("Headers-->"+getHeaders());
            HttpParams params = new HttpParams();

            if(jsonObject!=null){
                LogUtils.i("params-->"+jsonObject.toString());
                Iterator<?> it = jsonObject.keys();
                String key;
                while(it.hasNext()){
                    key = it.next().toString();
                    params.put(key, jsonObject.getString(key));
                }
            }

            OkGo.<String>get(url)
                    .tag(context)
                    .params(params)
                    .headers(getHeaders())  //统一头
                    .execute(callBack);

        } catch (JSONException e) {
            LogUtils.e ("网络访问异常_GET");
            e.printStackTrace();
        }


    }

    //String  POST
    public static void post(Context context, String url, JSONObject jsonObject, StringCallback callBack){
        try {
            LogUtils.i("Post-->"+url);
            LogUtils.i("PostHeader-->"+getHeaders());
            if(jsonObject==null){
                return;
            }
            LogUtils.i("body-->"+jsonObject.toString());
            OkGo.<String>post(url)
                    .tag(context)
                    .upJson(jsonObject.toString())
                    .headers(getHeaders())  //统一头
                    .execute(callBack);

        } catch (Exception e) {
            LogUtils.e ("网络访问异常_POST");
            e.printStackTrace();
        }


    }


    /**
     * 根据对象标记取消请求
     * @param object
     */
    public static void cancelTag(Object object){
        OkGo.getInstance().cancelTag(object);
    }
}
