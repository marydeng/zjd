package poll.com.zjd.model;

import android.databinding.ObservableBoolean;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/26  23:21
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class CityBean extends BaseModel{

    /**
     * brandId :
     * catId :
     * companyEnglishname : vanhmallc
     * companyName : 万惠商城C
     * delFlag : 1
     * departmentCode :
     * id : 8a2a8df44cf5d1cb014cfdd09d2b0312
     * isUse : 1
     * orderSn : 50
     * qqService :
     * remark :
     * storehouseCode :
     * subcompanyAddress :
     * subcompanyAgency :
     * subcompanyContact : 祁琼
     * subcompanyContactinfo : 18788776653
     */

    public String brandId;
    public String catId;
    public String companyEnglishname;
    public String companyName;
    public int delFlag;
    public String departmentCode;
    public String id;
    public int isUse;
    public int orderSn;
    public String qqService;
    public String remark;
    public String storehouseCode;
    public String subcompanyAddress;
    public String subcompanyAgency;
    public String subcompanyContact;
    public String subcompanyContactinfo;

    public ObservableBoolean obsSelect = new ObservableBoolean(false);//是否被选中

}
