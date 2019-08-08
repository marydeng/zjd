package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/28
 * 文件描述：
 */
public class CessorBean {
    private String participationUsersNo;
    private int delFlag;
    private int splitFlag;
    private String commodityId;
    private String promotionCommodityName;
    private String commodityNumber;
    private String commodityName;
    private double snappingUpPrice;
    private String promotionActiveId;

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
}
