package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/9
 * 文件描述：
 */
public class LstPropValueVo {
    private String propValueId;
    private String propValueNo;
    private String propValueName;
    private String propValue;
    private String propValuePicUrl;
    private String lstGoodsPicVo;
    private String isRelate;
    private int sortNo;
    private String relateCommodityId;
    private String relateCommodityNo;
    private String isDown;

    public LstPropValueVo(){
        super();
    }

    public String getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(String propValueId) {
        this.propValueId = propValueId;
    }

    public String getPropValueNo() {
        return propValueNo;
    }

    public void setPropValueNo(String propValueNo) {
        this.propValueNo = propValueNo;
    }

    public String getPropValueName() {
        return propValueName;
    }

    public void setPropValueName(String propValueName) {
        this.propValueName = propValueName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropValuePicUrl() {
        return propValuePicUrl;
    }

    public void setPropValuePicUrl(String propValuePicUrl) {
        this.propValuePicUrl = propValuePicUrl;
    }

    public String getLstGoodsPicVo() {
        return lstGoodsPicVo;
    }

    public void setLstGoodsPicVo(String lstGoodsPicVo) {
        this.lstGoodsPicVo = lstGoodsPicVo;
    }

    public String getIsRelate() {
        return isRelate;
    }

    public void setIsRelate(String isRelate) {
        this.isRelate = isRelate;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public String getRelateCommodityId() {
        return relateCommodityId;
    }

    public void setRelateCommodityId(String relateCommodityId) {
        this.relateCommodityId = relateCommodityId;
    }

    public String getRelateCommodityNo() {
        return relateCommodityNo;
    }

    public void setRelateCommodityNo(String relateCommodityNo) {
        this.relateCommodityNo = relateCommodityNo;
    }

    public String getIsDown() {
        return isDown;
    }

    public void setIsDown(String isDown) {
        this.isDown = isDown;
    }
}
