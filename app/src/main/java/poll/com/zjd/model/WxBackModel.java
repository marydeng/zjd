package poll.com.zjd.model;

import com.google.gson.annotations.SerializedName;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/24
 * 文件描述：微信支付类
 */
public class WxBackModel {

    /**
     * appid : wx99f59e21dc05108b
     * noncestr : LRSUQHW4NNY4MCS2VF
     * package : Sign=WXPay
     * partnerid : 1248364001
     * prepayid : wx20170816234718a5269d920b0203758653
     * sign : 8DB26A5C05D2F567A60DD82542CF28B2
     * timestamp : 1502898438
     */

    public String appid;
    public String noncestr;
    @SerializedName("package")
    public String packageX;
    public String partnerid;
    public String prepayid;
    public String sign;
    public String timestamp;
}
