package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/11
 * 文件描述：
 */

import java.util.List;

/**
                     "propGroupId":null,
                     "propGroupName":null,
                     "propItemId":null,
                     "propItemValue":"冷藏冷冻冰箱",
                     "propItemNo":"Ksj0b",
                     "propValueId":null,
                     "propValue":null,
                     "propNo":null,
                     "propItemName":"箱冷柜机型",
                     "propValueNo":"9d4a9c66bb3",
                     "childItemList":null
 */
public class ExtendPropList {
    private String propGroupId;
    private String propGroupName;
    private String propItemId;
    private String propItemValue;
    private String propItemNo;
    private String propValueId;
    private String propValue;
    private String propNo;
    private String propItemName;
    private String propValueNo;
//    private List<ExtendPropList> childItemList;

    public ExtendPropList(){
        super();
    }

    public String getPropGroupId() {
        return propGroupId;
    }

    public void setPropGroupId(String propGroupId) {
        this.propGroupId = propGroupId;
    }

    public String getPropGroupName() {
        return propGroupName;
    }

    public void setPropGroupName(String propGroupName) {
        this.propGroupName = propGroupName;
    }

    public String getPropItemId() {
        return propItemId;
    }

    public void setPropItemId(String propItemId) {
        this.propItemId = propItemId;
    }

    public String getPropItemValue() {
        return propItemValue;
    }

    public void setPropItemValue(String propItemValue) {
        this.propItemValue = propItemValue;
    }

    public String getPropItemNo() {
        return propItemNo;
    }

    public void setPropItemNo(String propItemNo) {
        this.propItemNo = propItemNo;
    }

    public String getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(String propValueId) {
        this.propValueId = propValueId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropNo() {
        return propNo;
    }

    public void setPropNo(String propNo) {
        this.propNo = propNo;
    }

    public String getPropItemName() {
        return propItemName;
    }

    public void setPropItemName(String propItemName) {
        this.propItemName = propItemName;
    }

    public String getPropValueNo() {
        return propValueNo;
    }

    public void setPropValueNo(String propValueNo) {
        this.propValueNo = propValueNo;
    }

//    public List<ExtendPropList> getChildItemList() {
//        return childItemList;
//    }
//
//    public void setChildItemList(List<ExtendPropList> childItemList) {
//        this.childItemList = childItemList;
//    }
}
