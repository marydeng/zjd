package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/17
 * 文件描述：提交订单前的检查
 */
public class OrderCheckResponse {
    /**
     "snappingUpAmount": 0.20000000298023224,
     "preferentialAmount": 1,
     "quotaActive": {
     "freeFullAmount": 99,
     "freeFullBalance": 84,
     "isJoin": true
     },
     "saleAmount": 16,
     "memberAmount": 15,
     "useCouponAmount": 0,
     "freightAmount": 0,
     "twoHoursDelivery": true,
     "buyAmount": 15
     */
    private double snappingUpAmount;
    private double memberAmount;
    private double useCouponAmount;
    private double preferentialAmount;
    private double saleAmount;
    private double freightAmount;
    private double buyAmount;
    private boolean twoHoursDelivery;
    private QuotaActive quotaActive;


    private OrderCheckResponse() {
        super();
    }

    public double getPreferentialAmount() {
        return preferentialAmount;
    }

    public void setPreferentialAmount(double preferentialAmount) {
        this.preferentialAmount = preferentialAmount;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public boolean isTwoHoursDelivery() {
        return twoHoursDelivery;
    }

    public void setTwoHoursDelivery(boolean twoHoursDelivery) {
        this.twoHoursDelivery = twoHoursDelivery;
    }

    public QuotaActive getQuotaActive() {
        return quotaActive;
    }

    public void setQuotaActive(QuotaActive quotaActive) {
        this.quotaActive = quotaActive;
    }

    public double getSnappingUpAmount() {
        return snappingUpAmount;
    }

    public void setSnappingUpAmount(double snappingUpAmount) {
        this.snappingUpAmount = snappingUpAmount;
    }

    public double getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(double memberAmount) {
        this.memberAmount = memberAmount;
    }

    public double getUseCouponAmount() {
        return useCouponAmount;
    }

    public void setUseCouponAmount(double useCouponAmount) {
        this.useCouponAmount = useCouponAmount;
    }
}
