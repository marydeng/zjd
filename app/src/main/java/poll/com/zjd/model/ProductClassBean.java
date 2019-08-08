package poll.com.zjd.model;

import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.BR;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/26  21:04
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class ProductClassBean extends BaseModel{

    @Bindable
    private boolean select;

    private String className;

    public ProductClassBean(boolean select, String className){
        this.select = select;
        this.className = className;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public static List<ProductClassBean> getProductClassList(){
        List<ProductClassBean> productClassBeens = new ArrayList<>();
        productClassBeens.add(new ProductClassBean(true,"全部"));
        productClassBeens.add(new ProductClassBean(false,"喜宴用酒"));
        productClassBeens.add(new ProductClassBean(false,"家乡名酒"));
        productClassBeens.add(new ProductClassBean(false,"年份名酒"));
        productClassBeens.add(new ProductClassBean(false,"礼品酒"));
        productClassBeens.add(new ProductClassBean(false,"实惠大坛"));
        productClassBeens.add(new ProductClassBean(false,"礼尚往来"));
        productClassBeens.add(new ProductClassBean(false,"海外直采"));
        productClassBeens.add(new ProductClassBean(false,"商务用酒"));
        productClassBeens.add(new ProductClassBean(false,"名店特卖"));
        productClassBeens.add(new ProductClassBean(false,"82年的拉菲"));
        productClassBeens.add(new ProductClassBean(false,"75年的鸡爪"));
        productClassBeens.add(new ProductClassBean(false,"哇哈哈"));
        productClassBeens.add(new ProductClassBean(false,"礼品礼品"));
        productClassBeens.add(new ProductClassBean(false,"送礼"));
        productClassBeens.add(new ProductClassBean(false,"土豪金"));
        productClassBeens.add(new ProductClassBean(false,"尊贵白"));
        productClassBeens.add(new ProductClassBean(false,"钢琴黑"));
        productClassBeens.add(new ProductClassBean(false,"中国红"));
        productClassBeens.add(new ProductClassBean(false,"斯蒂芬的"));
        productClassBeens.add(new ProductClassBean(false,"过分过分"));


        return productClassBeens;
    }

}
