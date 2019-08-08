package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/9
 * 文件描述：
 */

/**
 {
 "goodsId": "402899965cde7dc9015cdec8a6ac001c",
 "goodsNo": "062510958172001",
 "publicPrice": 10,
 "salePrice": 1.2,
 "save": 8.8,
 "discount": "1.2",
 "stockCount": "0",
 "isFreeFreight": 0,
 "commodityWeight": "0.0",
 "propKey": "color_red:size_20"
 }
 */
public class LstGoods {
    private String goodsId;
    private String goodsNo;
    private double publicPrice;
    private double salePrice;
    private double save;
    private String discount;
    private int stockCount;
    private int isFreeFreight;
    private String commodityWeight;
    private String propKey;

    public LstGoods(){
        super();
    }


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public double getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(double publicPrice) {
        this.publicPrice = publicPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getSave() {
        return save;
    }

    public void setSave(double save) {
        this.save = save;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public int getIsFreeFreight() {
        return isFreeFreight;
    }

    public void setIsFreeFreight(int isFreeFreight) {
        this.isFreeFreight = isFreeFreight;
    }

    public String getCommodityWeight() {
        return commodityWeight;
    }

    public void setCommodityWeight(String commodityWeight) {
        this.commodityWeight = commodityWeight;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }
}
