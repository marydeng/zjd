package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/9
 * 文件描述：商品类的子公司类
 */
public class SubCompany {
    private String id;
    private String companyName;
    private String companyEnglishname;
    private String subcompanyContact;
    private String subcompanyContactinfo;
    private String subcompanyAddress;
    private String subcompanyAgency;
    private int delFlag;
    private String remark;
    private int isUse;
    private String storehouseCode;
    private String departmentCode;
    private int orderSn;
    private String catId;
    private String brandId;
    private String qqService;

    public SubCompany(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEnglishname() {
        return companyEnglishname;
    }

    public void setCompanyEnglishname(String companyEnglishname) {
        this.companyEnglishname = companyEnglishname;
    }

    public String getSubcompanyContact() {
        return subcompanyContact;
    }

    public void setSubcompanyContact(String subcompanyContact) {
        this.subcompanyContact = subcompanyContact;
    }

    public String getSubcompanyContactinfo() {
        return subcompanyContactinfo;
    }

    public void setSubcompanyContactinfo(String subcompanyContactinfo) {
        this.subcompanyContactinfo = subcompanyContactinfo;
    }

    public String getSubcompanyAddress() {
        return subcompanyAddress;
    }

    public void setSubcompanyAddress(String subcompanyAddress) {
        this.subcompanyAddress = subcompanyAddress;
    }

    public String getSubcompanyAgency() {
        return subcompanyAgency;
    }

    public void setSubcompanyAgency(String subcompanyAgency) {
        this.subcompanyAgency = subcompanyAgency;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public String getStorehouseCode() {
        return storehouseCode;
    }

    public void setStorehouseCode(String storehouseCode) {
        this.storehouseCode = storehouseCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public int getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(int orderSn) {
        this.orderSn = orderSn;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getQqService() {
        return qqService;
    }

    public void setQqService(String qqService) {
        this.qqService = qqService;
    }
}
