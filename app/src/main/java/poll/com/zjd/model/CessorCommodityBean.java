package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/30
 * 文件描述：获取秒杀列表返回类
 */
public class CessorCommodityBean {
    /**
     * "participationUsersNo": "1111111111111",
     * "delFlag": 1,
     * "splitFlag": 1,
     * "commodityId": "8a294c115dfa6a92015dfccf93c40019",
     * "promotionCommodityName": "秒杀",
     * "commodityNumber": "072408410912",
     * "commodityName": "黄尾袋鼠梅洛红葡萄酒-半干红",
     * "snappingUpPrice": 30,
     * "lessBuyAmount": null,
     * "moreBuyAmount": null,
     * "discount": null,
     * "presentId": null,
     * "presentNumber": null,
     * "presentName": null,
     * "presentCount": null,
     * "promotionActiveId": "8a294c115e054a26015e057acf370011"
     */

    private String participationUsersNo;
    private int delFlag;
    private int splitFlag;
    private String commodityId;
    private String promotionCommodityName;
    private String commodityNumber;
    private String commodityName;
    private double snappingUpPrice;
    private String promotionActiveId;
    private int count;//当前添加的个数
    private GoodsCommodityDetailVo commodityDetailVo;


    public String getParticipationUsersNo() {
        return participationUsersNo;
    }

    public void setParticipationUsersNo(String participationUsersNo) {
        this.participationUsersNo = participationUsersNo;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getSplitFlag() {
        return splitFlag;
    }

    public void setSplitFlag(int splitFlag) {
        this.splitFlag = splitFlag;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getPromotionCommodityName() {
        return promotionCommodityName;
    }

    public void setPromotionCommodityName(String promotionCommodityName) {
        this.promotionCommodityName = promotionCommodityName;
    }

    public String getCommodityNumber() {
        return commodityNumber;
    }

    public void setCommodityNumber(String commodityNumber) {
        this.commodityNumber = commodityNumber;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public double getSnappingUpPrice() {
        return snappingUpPrice;
    }

    public void setSnappingUpPrice(double snappingUpPrice) {
        this.snappingUpPrice = snappingUpPrice;
    }

    public String getPromotionActiveId() {
        return promotionActiveId;
    }

    public void setPromotionActiveId(String promotionActiveId) {
        this.promotionActiveId = promotionActiveId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSoldOut() {
        return null!=commodityDetailVo && commodityDetailVo.getStockCount()<1;
    }

    public boolean isOffShelves(){
        return false;//Todo
    }

    public GoodsCommodityDetailVo getCommodityDetailVo() {
        return commodityDetailVo;
    }

    public void setCommodityDetailVo(GoodsCommodityDetailVo commodityDetailVo) {
        this.commodityDetailVo = commodityDetailVo;
    }
}
