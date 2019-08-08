package poll.com.zjd.view.picker.listener;


import poll.com.zjd.view.picker.entity.City;
import poll.com.zjd.view.picker.entity.County;
import poll.com.zjd.view.picker.entity.Province;

/**
 * @author matt
 * blog: addapp.cn
 */

public interface OnLinkageListener {
    /**
     * 选择地址
     *
     * @param province the province
     * @param city    the city
     * @param county   the county ，if {@code hideCounty} is true，this is null
     */
    void onAddressPicked(Province province, City city, County county);
}
