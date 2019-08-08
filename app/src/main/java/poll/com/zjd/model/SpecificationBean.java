package poll.com.zjd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/22
 * 文件描述：商品规格数据类
 */
public class SpecificationBean {
    private String name;
    private String value;

    public SpecificationBean() {
        super();
    }

    public SpecificationBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<SpecificationBean> getSpecificationList(){
        List<SpecificationBean> specificationBeanList=new ArrayList<>();
        specificationBeanList.add(new SpecificationBean("规则","1瓶"));
        specificationBeanList.add(new SpecificationBean("重量","750ml"));
        specificationBeanList.add(new SpecificationBean("包装","瓶装"));
        specificationBeanList.add(new SpecificationBean("保质期","3600天"));
        specificationBeanList.add(new SpecificationBean("储存方法","常温"));
        return specificationBeanList;
    }
}
