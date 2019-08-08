package poll.com.zjd.view.picker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 地市
 * <br/>
 * Author:matt : addapp.cn
 * DateTime:2016-10-15 19:07
 *
 */
public class City extends ItemBean {
    private String provinceId;
    private List<County> childVo = new ArrayList<County>();

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public List<County> getCounties() {
        return childVo;
    }

    public void setCounties(List<County> counties) {
        this.childVo = counties;
    }

    public List<County> getChildVo() {
        return childVo;
    }

    public void setChildVo(List<County> childVo) {
        this.childVo = childVo;
    }
}