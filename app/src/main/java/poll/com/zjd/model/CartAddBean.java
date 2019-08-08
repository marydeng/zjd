package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/28
 * 文件描述：添加到购物车时传入的数据
 */
public class CartAddBean {
    private String productId;
    private String productNo;
    private String picDesc;
    private String picSmall;
    private double publicPrice;
    private double salePrice;
    private double cessorPrice;
    private String desc;
    private String productName;
    private int stock;
    private boolean cessor;
    private boolean off_shelves;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getPicDesc() {
        return picDesc;
    }

    public void setPicDesc(String picDesc) {
        this.picDesc = picDesc;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public double getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(double publicPrice) {
        this.publicPrice = publicPrice;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getCessorPrice() {
        return cessorPrice;
    }

    public void setCessorPrice(double cessorPrice) {
        this.cessorPrice = cessorPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isCessor() {
        return cessor;
    }

    public void setCessor(boolean cessor) {
        this.cessor = cessor;
    }

    public boolean isOff_shelves() {
        return off_shelves;
    }

    public void setOff_shelves(boolean off_shelves) {
        this.off_shelves = off_shelves;
    }
}
