package poll.com.zjd.model;

import android.databinding.Bindable;

import poll.com.zjd.BR;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/6  11:47
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class GoodsClass extends BaseModel{


    /**
     * id : 297ead195d011b4d015d0143f3400003
     * catName : 酒
     * isRecommend : 1
     * isShow : 0
     * sortNo : 1
     * updatePerson : admin
     * createDate : 1498964816000
     * updateDate : 1498964816000
     * structName : 20
     * catAlias : null
     * keywords : 酒
     * catb2cDesc :
     * isEnabled : 1
     * deleteFlag : 1
     * no : yP
     * level : 1
     * updateTimestamp : 1498964816704
     * seoKeyword :
     * seoTitle :
     * seoDescription :
     */

    private String id;
    private String catName;
    private int isRecommend;
    private int isShow;
    private int sortNo;
    private String updatePerson;
    private long createDate;
    private long updateDate;
    private String structName;
    private Object catAlias;
    private String keywords;
    private String catb2cDesc;
    private int isEnabled;
    private int deleteFlag;
    private String no;
    private int level;
    private long updateTimestamp;
    private String seoKeyword;
    private String seoTitle;
    private String seoDescription;

    @Bindable
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
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

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public Object getCatAlias() {
        return catAlias;
    }

    public void setCatAlias(Object catAlias) {
        this.catAlias = catAlias;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCatb2cDesc() {
        return catb2cDesc;
    }

    public void setCatb2cDesc(String catb2cDesc) {
        this.catb2cDesc = catb2cDesc;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }
}
