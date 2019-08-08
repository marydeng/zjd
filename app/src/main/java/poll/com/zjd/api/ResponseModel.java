package poll.com.zjd.api;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/24  21:03
 * 包名:     poll.com.zjd.api
 * 项目名:   zjd
 *
 * 请求接口返回model
 */

public class ResponseModel {

    public int code;    //请求返回状态 0 失败 1 成功
    public int flag;    //请求返回状态 0 失败 1 成功
    public String msg;  //返回的成功失败说明
    public Object data; //返回的数据
}
