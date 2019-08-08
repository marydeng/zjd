package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/21
 * 文件描述：购物车更新商品信息请求类
 */
public class UpdaeProductInfoRequest {
    private String subcompanyId;
    private List<String> productNos;
    private String province;
    private String city;
    private String area;
    private String town;

    public UpdaeProductInfoRequest() {
        super();
    }

    public String getSubcompanyId() {
        return subcompanyId;
    }

    public void setSubcompanyId(String subcompanyId) {
        this.subcompanyId = subcompanyId;
    }

    public List<String> getProductNos() {
        return productNos;
    }

    public void setProductNos(List<String> productNos) {
        this.productNos = productNos;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
