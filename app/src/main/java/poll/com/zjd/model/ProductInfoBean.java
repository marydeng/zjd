package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/20
 * 文件描述：订单信息更新类
 */
public class ProductInfoBean {
    private String productName;
    private String productNo;
    private double salePrice;
    private double cessorPrice;
    private int stock;
    private boolean cessor;
    private int productState;

    public interface ProductState

    {
        int on_shelves = 1;
        int off_shelves = 2;
    }


    public ProductInfoBean() {
        super();
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
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

    public int getProductState() {
        return productState;
    }

    public void setProductState(int productState) {
        this.productState = productState;
    }
}
