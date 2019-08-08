package poll.com.zjd.api;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpResponseHandler;

import poll.com.zjd.app.AppContext;

public abstract class ApiHttpResponseHandler extends AsyncHttpResponseHandler {
	private Context mcontext=null;
//	private LoadingDialog dialog=null;
	
	public ApiHttpResponseHandler() {
	}
	public ApiHttpResponseHandler(Context context)
	{
		mcontext=context;
	}
	public void onStart() {
		if(!AppContext.getInstance().isNetworkConnected()){
			AppContext.getInstance().displayNotConnectedNetwork();
			this.onCancel();
//			return;
		}
//		if(mcontext!=null)
//		{
//			dialog=AppTemple.getInstance().displayLoadingDialog(mcontext,null,false);
//		}
	}

	@Override
	public void onFailure(int statusCode, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		Log.i("ONFaileure statusCode", statusCode+"");
		if(arg2!=null&&arg2.length!=0){
			String result = new String(arg2);
			Log.i("onFailure",result);
			Toast.makeText(AppContext.getInstance(), arg3.getMessage(), Toast.LENGTH_LONG).show();
			}
//			Gson gson=new Gson();
//			HttpResultModel httpResultModel=gson.fromJson(result, HttpResultModel.class);
//			if(mcontext!=null)
//			{
//				Toast.makeText(mcontext, httpResultModel.message, Toast.LENGTH_LONG).show();
//			}
//		}else
//		{
//			Toast.makeText(AppTemple.getInstance(), arg3.getMessage(), Toast.LENGTH_LONG).show();
//		}
//		if(dialog!=null)
//        {
//        	dialog.dismiss();
//        }
	}

	
	
	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		try{
//	        if(dialog!=null)
//	        {
//	        	dialog.dismiss();
//	        }
	    	String result = new String(arg2);
			Log.i("On Success",result);
	        this.onSuccessEvent(arg0, arg1, result);
		}
		catch(Exception ex){
			ex.printStackTrace();
			this.onCancel();
		}
	}
	
	/***
	 * 隐藏loading窗口
	 */
	public void hideDialog(){
//		if(dialog!=null)
//        {
//        	dialog.dismiss();
//        }
	}
	
	public abstract void onSuccessEvent(int arg0, Header[] arg1, String result);
	
	public void onRetry(int retryNo) {
	}

}
