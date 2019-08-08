package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/1  21:51
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class HomeClassBean {

    private String title;
    private String adUrl;
    private String clickUrl;
    private List<Product> productList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
