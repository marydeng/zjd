package poll.com.zjd.api;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import poll.com.zjd.activity.LoginActivity;
import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.manager.ActivityManager;
import poll.com.zjd.utils.DialogUtil;
import poll.com.zjd.utils.LogUtils;
import poll.com.zjd.utils.ToastUtils;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/24  20:58
 * 包名:     poll.com.zjd.api
 * 项目名:   zjd
 */

public abstract class OkGoStringCallback extends StringCallback {


    @Override
    public void onSuccess(Response<String> response) {
        ResponseModel responseModel = null;
        Gson gson = new Gson();
        try{
            LogUtils.i("response->body="+response.body());
            responseModel = gson.fromJson(response.body(),ResponseModel.class);
        }catch (Exception e){
            LogUtils.e("转换出错");
            onError(response);
        }
        if (responseModel==null){
            //请求失败
            onError(response);
        }else {

            if (responseModel.code == AppConfig.ResultCode.success ){
                //请求成功
                onSuccessEvent(gson.toJson(responseModel.data));
            }else if(responseModel.code == AppConfig.ResultCode.unAuthorize) {
                //跳转登录页面
                AppContext.getInstance().startActivity(ActivityManager.getAppManager().currentActivity(), LoginActivity.class, null);
            }else{
                onError(response);
                //请求失败
                ToastUtils.showToast(AppContext.getInstance(),responseModel.msg,1);
            }
        }

    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        LogUtils.i("response->body="+response.body());
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
    }

    @Override
    public void onFinish() {
        super.onFinish();
//        DialogUtil.hideProgressDialog();
    }

    public abstract void onSuccessEvent(String result);
}
