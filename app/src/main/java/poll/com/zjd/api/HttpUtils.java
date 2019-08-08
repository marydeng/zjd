package poll.com.zjd.api;

import java.net.URLDecoder;
import java.util.Iterator;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import poll.com.zjd.utils.LogUtils;


public class HttpUtils {
    public static void get(JSONObject headerJsonObject,JSONObject queryJsonObject,String uri,AsyncHttpResponseHandler handle)
    {
    	AsyncHttpClient client = new AsyncHttpClient();
    	try {
	    	if(headerJsonObject!=null)
	    	{
	        	client.addHeader("accesstoken", headerJsonObject.getString("accesstoken"));
				Log.i("HTTPUTILS",headerJsonObject.getString("accesstoken"));
	    	}
			RequestParams params = new RequestParams();
			//params.put("userTele", queryJsonObject.getString("userTele"));
			if(queryJsonObject!=null)
			{
				Iterator<?> it = queryJsonObject.keys();
		        String key = null;  
		        while(it.hasNext()){
		        	key = (String) it.next().toString();  
					params.put(key, queryJsonObject.getString(key));
		        }
			}
			client.get(uri, params,handle);
    	} catch (Exception e) {
    		LogUtils.e ("网络访问异常");
			e.printStackTrace();
		}
    }
    
    public static void post(Context context,JSONObject headerJsonObject,JSONObject queryJsonObject,String uri,AsyncHttpResponseHandler handle)
    {
    	AsyncHttpClient client = new AsyncHttpClient();
    	try {
    		client.addHeader("Content-Type", "application/json;charset=UTF-8;");
    		URLDecoder.decode("utf-8");
	    	if(headerJsonObject!=null)
	    	{
	        	client.addHeader("accesstoken", headerJsonObject.getString("accesstoken"));
				Log.i("HTTPUTILS",headerJsonObject.getString("accesstoken"));
	    	}
			org.apache.http.entity.StringEntity entity = new StringEntity(queryJsonObject.toString(),"UTF-8");
			Log.i("ENTIFY", entity.toString());
			client.post(context, uri, entity, "application/json;charset=utf-8", handle);
			//client.post(AppManager.getAppManager().currentActivity(),uri,params,handle);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void put(Context context,JSONObject headerJsonObject,JSONObject queryJsonObject,String uri,AsyncHttpResponseHandler handle)
    {
    	AsyncHttpClient client = new AsyncHttpClient();
    	try {
    		client.addHeader("Content-Type", "application/json;charset=UTF-8;");
//	    	if(headerJsonObject!=null)
//	    	{
//	        	client.addHeader("accesstoken", headerJsonObject.getString("accesstoken"));
//				Log.i("HTTPUTILS",headerJsonObject.getString("accesstoken"));
//	    	}
			org.apache.http.entity.StringEntity entity = new StringEntity(queryJsonObject.toString(),"UTF-8");
			Log.i("ENTIFY", entity.toString());
			client.put(context, uri, entity, "application/json;charset=utf-8", handle);
			//client.post(AppManager.getAppManager().currentActivity(),uri,params,handle);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void delete(Context context,JSONObject headerJsonObject,JSONObject queryJsonObject,String uri,AsyncHttpResponseHandler handle){
    	AsyncHttpClient client = new AsyncHttpClient();
    	
		client.addHeader("Content-Type", "application/json;charset=UTF-8;");
		//		org.apache.http.entity.StringEntity entity = new StringEntity(queryJsonObject.toString(),"UTF-8");
    	client.delete(context, uri, handle);
    }
    
}
