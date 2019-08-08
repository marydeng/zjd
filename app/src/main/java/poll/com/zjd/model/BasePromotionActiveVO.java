package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/22
 * 文件描述：
 */
public class BasePromotionActiveVO {

    /**
                     "id":"8a294c115e054a26015e057acf370011",
                     "activeType":2,
                     "activeName":"秒杀",
                     "activeState":null,
                     "oneManyCount":2,
                     "onePayCount":1,
                     "manyPayCount":0,
                     "activeStartDate":"2017-08-20 16:05",
                     "activeEndDate":"2017-08-31 16:05",
                     "levelId":"all",
                     "levelName":"all",
                     "commodityId":"8a294c115dfa6a92015dfccf9418001e",
                     "brandId":null,
                     "thirdCategoriesId":null,
                     "isSupportCoupon":0,
                     "redemptionCommodityVoList":null,
                     "sendGiftVoList":null
     */

    private String id;
    private int activeType;

    public interface  ActiveType{
        int NormalType=1;
        int SecondKill=2;
    }
    private String activeName;
    private String activeState;
    private int oneManyCount;
    private int onePayCount;
    private int manyPayCount;
    private String activeStartDate;
    private String activeEndDate;
    private String levelId;
    private String levelName;
    private String commodityId;
    private String brandId;
    private String thirdCategoriesId;
    private int isSupportCoupon;
    private List<String> redemptionCommodityVoList;
    private List<String> sendGiftVoList;
    // "snappingUpPrice":50,
    private double snappingUpPrice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getActiveType() {
        return activeType;
    }

    public void setActiveType(int activeType) {
        this.activeType = activeType;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getActiveState() {
        return activeState;
    }

    public void setActiveState(String activeState) {
        this.activeState = activeState;
    }

    public int getOneManyCount() {
        return oneManyCount;
    }

    public void setOneManyCount(int oneManyCount) {
        this.oneManyCount = oneManyCount;
    }

    public int getOnePayCount() {
        return onePayCount;
    }

    public void setOnePayCount(int onePayCount) {
        this.onePayCount = onePayCount;
    }

    public int getManyPayCount() {
        return manyPayCount;
    }

    public void setManyPayCount(int manyPayCount) {
        this.manyPayCount = manyPayCount;
    }

    public String getActiveStartDate() {
        return activeStartDate;
    }

    public void setActiveStartDate(String activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public String getActiveEndDate() {
        return activeEndDate;
    }

    public void setActiveEndDate(String activeEndDate) {
        this.activeEndDate = activeEndDate;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getThirdCategoriesId() {
        return thirdCategoriesId;
    }

    public void setThirdCategoriesId(String thirdCategoriesId) {
        this.thirdCategoriesId = thirdCategoriesId;
    }

    public int getIsSupportCoupon() {
        return isSupportCoupon;
    }

    public void setIsSupportCoupon(int isSupportCoupon) {
        this.isSupportCoupon = isSupportCoupon;
    }

    public List<String> getRedemptionCommodityVoList() {
        return redemptionCommodityVoList;
    }

    public void setRedemptionCommodityVoList(List<String> redemptionCommodityVoList) {
        this.redemptionCommodityVoList = redemptionCommodityVoList;
    }

    public List<String> getSendGiftVoList() {
        return sendGiftVoList;
    }

    public void setSendGiftVoList(List<String> sendGiftVoList) {
        this.sendGiftVoList = sendGiftVoList;
    }

    public double getSnappingUpPrice() {
        return snappingUpPrice;
    }

    public void setSnappingUpPrice(double snappingUpPrice) {
        this.snappingUpPrice = snappingUpPrice;
    }
}
