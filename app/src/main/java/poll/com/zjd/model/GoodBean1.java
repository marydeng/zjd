package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/9/8  2:34
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class GoodBean1 {
    /**
     * id : 8a294c115dfa6a92015dfccf94a2002d
     * commodityId : null
     * prodNo : 072408158600001
     * prodName : 灵蛇红葡萄酒
     * seoKeyWord : 半干红
     * seoTitle : null
     * prodPrice : 115
     * publicPrice : 215
     * giftObject : null
     * origin : null
     * image : /upload/image/201708/56508e67fc644294a5c393d9f305876a_middle.jpg
     * threeLevelNo : 0D
     * threeLevelName : 半干红
     * secondLevelName : 红葡萄酒
     * priceScpe : 100-199元
     * brandName : 臻酒到
     * addedTime : 1503185048000
     * commodityNo : 072408158600
     * salesCount : 0
     * alias : 13.5°碧琦灵蛇半干红葡萄酒 意大利进口 单瓶装750ml
     * inventory : 0
     * subcompanyId : null
     * warehouseId : null
     */

    private String id;
    private Object commodityId;
    private String prodNo;
    private String prodName;
    private String seoKeyWord;
    private Object seoTitle;
    private double prodPrice;
    private double publicPrice;
    private Object giftObject;
    private Object origin;
    private String image;
    private String threeLevelNo;
    private String threeLevelName;
    private String secondLevelName;
    private String priceScpe;
    private String brandName;
    private long addedTime;
    private String commodityNo;
    private int salesCount;
    private String alias;
    private int inventory;
    private Object subcompanyId;
    private Object warehouseId;
    private boolean kill;
    private double killPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Object commodityId) {
        this.commodityId = commodityId;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getSeoKeyWord() {
        return seoKeyWord;
    }

    public void setSeoKeyWord(String seoKeyWord) {
        this.seoKeyWord = seoKeyWord;
    }

    public Object getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(Object seoTitle) {
        this.seoTitle = seoTitle;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public double getPublicPrice() {
        return publicPrice;
    }

    public String getProStr(){
        return "￥" + prodPrice;
    }

    public String getPubStr(){
        return "<span style='text-decoration:line-through;'>￥" + publicPrice +"</span>";
    }

    public void setPublicPrice(int publicPrice) {
        this.publicPrice = publicPrice;
    }

    public Object getGiftObject() {
        return giftObject;
    }

    public void setGiftObject(Object giftObject) {
        this.giftObject = giftObject;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThreeLevelNo() {
        return threeLevelNo;
    }

    public void setThreeLevelNo(String threeLevelNo) {
        this.threeLevelNo = threeLevelNo;
    }

    public String getThreeLevelName() {
        return threeLevelName;
    }

    public void setThreeLevelName(String threeLevelName) {
        this.threeLevelName = threeLevelName;
    }

    public String getSecondLevelName() {
        return secondLevelName;
    }

    public void setSecondLevelName(String secondLevelName) {
        this.secondLevelName = secondLevelName;
    }

    public String getPriceScpe() {
        return priceScpe;
    }

    public void setPriceScpe(String priceScpe) {
        this.priceScpe = priceScpe;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public long getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(long addedTime) {
        this.addedTime = addedTime;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Object getSubcompanyId() {
        return subcompanyId;
    }

    public void setSubcompanyId(Object subcompanyId) {
        this.subcompanyId = subcompanyId;
    }

    public Object getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Object warehouseId) {
        this.warehouseId = warehouseId;
    }

    public boolean isKill() {
        return kill;
    }

    public void setKill(boolean kill) {
        this.kill = kill;
    }

    public double getKillPrice() {
        return killPrice;
    }

    public void setKillPrice(double killPrice) {
        this.killPrice = killPrice;
    }
}
