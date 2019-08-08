package poll.com.zjd.view.picker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 省份
 * <br/>
 * Author:matt : addapp.cn
 * DateTime:2016-10-15 19:06
 *
 */
public class Province extends ItemBean {
    private List<City> childVo = new ArrayList<City>();

    public List<City> getCities() {
        return childVo;
    }

    public void setCities(List<City> cities) {
        this.childVo = cities;
    }

    public List<City> getChildVo() {
        return childVo;
    }

    public void setChildVo(List<City> childVo) {
        this.childVo = childVo;
    }
}