package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/24
 * 文件描述：充值储值卡支付信息返回类
 */
public class CreateRechargePayInfoResponse {
    /**
     "giftAmount": 10,
     "id": "8a294c115e148f6a015e1c7e3ea20083",
     "rechargeAmount": 100,
     "transDate": "2017-08-26 11:03:09"
     */

    private String id;
    private String transDate;
    private double rechargeAmount;
    private double giftAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public double getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(double giftAmount) {
        this.giftAmount = giftAmount;
    }
}
