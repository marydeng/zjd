package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/6  15:28
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class BrandBean extends BaseModel{

    /**
     * brandDesc :
     * brandName : ??
     * brandNo : Oze4
     * deleteflag : 1
     * englishName : lafei
     * id : 297ead195d011b4d015d014e082b0007
     * isEnabled : 0
     * isShow : 1
     * sortNo : 1
     * speelingName :
     */

    private String brandDesc;
    private String brandName;
    private String brandNo;
    private int deleteflag;
    private String englishName;
    private String id;
    private int isEnabled;
    private int isShow;
    private int sortNo;
    private String speelingName;

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    public int getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(int deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public String getSpeelingName() {
        return speelingName;
    }

    public void setSpeelingName(String speelingName) {
        this.speelingName = speelingName;
    }
}
