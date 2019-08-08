package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/9
 * 文件描述：
 */

import java.util.List;

/**
 *
 *  "lstPropItem": [
 {
 "propItemId": "402885d837caba7f0137cb1985e70007",
 "propItemNo": "color",
 "propItemName": "颜色",
 "dispalyType": 0,
 "propItemDesc": null,
 "LstPropValueVo": [
 {
 *  "propValueId": "402885d837caba7f0137cb1986210008",
 "propValueNo": "red",
 "propValueName": "红色",
 "propValue": "红色",
 "propValuePicUrl": null,
 "lstGoodsPicVo": null,
 "isRelate": null,
 "sortNo": 0,
 "relateCommodityId": null,
 "relateCommodityNo": null,
 "isDown": null
 }
 ],
 "sortNo": 0
 },
 */
public class PropItem {
    private String propItemId;
    private String propItemNo;
    private String propItemName;
    private int dispalyType;
    private String propItemDesc;
    private List<LstPropValueVo> lstPropValueVo;
    private int sortNo;

    public PropItem(){
        super();
    }

    public String getPropItemId() {
        return propItemId;
    }

    public void setPropItemId(String propItemId) {
        this.propItemId = propItemId;
    }
}
