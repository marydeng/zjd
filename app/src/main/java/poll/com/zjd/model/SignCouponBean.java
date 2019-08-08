package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/9/9  0:07
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class SignCouponBean {


    /**
     * amount : null
     * rule : null
     * endTime : null
     * flag : 0
     */

    private String amount;
    private String rule;
    private String endTime;
    private Integer flag;        //0 其他未知,反正就是没券  1正确领取券的姿势

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
