package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/10/15  22:23
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class ClassPriceModel {

    /**
     * id : 297ead195df5b263015df5e292d00029
     * maxPrice : 49
     * minPrice : 0
     * priceScope : 49元以下
     */

    private String id;
    private int maxPrice;
    private int minPrice;
    private String priceScope;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public String getPriceScope() {
        return priceScope;
    }

    public void setPriceScope(String priceScope) {
        this.priceScope = priceScope;
    }
}
