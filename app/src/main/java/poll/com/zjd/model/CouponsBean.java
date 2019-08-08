package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/23
 * 文件描述：优惠券元数据类
 */
public class CouponsBean {
    private String orderNo;
    private String useDate;
    private String activeName;
    private String promotionNo;
    private String number;
    private String useEndTime;
    private String state;
    private String parValue;
    private String couponName;
    private String lowestPay;
    private String week;

    public CouponsBean() {
        super();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getPromotionNo() {
        return promotionNo;
    }

    public void setPromotionNo(String promotionNo) {
        this.promotionNo = promotionNo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParValue() {
        return parValue;
    }

    public void setParValue(String parValue) {
        this.parValue = parValue;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getLowestPay() {
        return lowestPay;
    }

    public void setLowestPay(String lowestPay) {
        this.lowestPay = lowestPay;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
