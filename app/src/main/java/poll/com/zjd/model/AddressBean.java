package poll.com.zjd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/18
 * 文件描述：地址类
 */
public class AddressBean {
    private String receivingName;
    private String distributionType;
    private String receivingTelephone;
    private String receivingZipCode;
    private int concatType;

    public interface ConcatType {
        int company = 1;
        int school = 3;
        int residential = 2;
        int other = 4;
    }

    private String receivingAddress;
    private String isDefaultAddress;

    public interface IsDefaultAddress {
        String defaultAddress = "1";
        String notDefaultAddress="0";
    }

    private String receivingDistrict;
    private String receivingCity;
    private String receivingProvince;
    private String receivingStreet;
    private String specificAddress;
    private String id;
    private String receivingMobilePhone;

    private String province;
    private String city;
    private String district;
    private String street;


    public AddressBean() {
        super();
    }


    public String getReceivingName() {
        return receivingName;
    }

    public void setReceivingName(String receivingName) {
        this.receivingName = receivingName;
    }

    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    public String getReceivingTelephone() {
        return receivingTelephone;
    }

    public void setReceivingTelephone(String receivingTelephone) {
        this.receivingTelephone = receivingTelephone;
    }

    public String getReceivingZipCode() {
        return receivingZipCode;
    }

    public void setReceivingZipCode(String receivingZipCode) {
        this.receivingZipCode = receivingZipCode;
    }

    public int getConcatType() {
        return concatType;
    }

    public void setConcatType(int concatType) {
        this.concatType = concatType;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public String getIsDefaultAddress() {
        return isDefaultAddress;
    }

    public void setIsDefaultAddress(String isDefaultAddress) {
        this.isDefaultAddress = isDefaultAddress;
    }

    public String getReceivingDistrict() {
        return receivingDistrict;
    }

    public void setReceivingDistrict(String receivingDistrict) {
        this.receivingDistrict = receivingDistrict;
    }

    public String getReceivingCity() {
        return receivingCity;
    }

    public void setReceivingCity(String receivingCity) {
        this.receivingCity = receivingCity;
    }

    public String getReceivingProvince() {
        return receivingProvince;
    }

    public void setReceivingProvince(String receivingProvince) {
        this.receivingProvince = receivingProvince;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceivingMobilePhone() {
        return receivingMobilePhone;
    }

    public void setReceivingMobilePhone(String receivingMobilePhone) {
        this.receivingMobilePhone = receivingMobilePhone;
    }

    public static List<AddressBean> createAddressList(){
        List<AddressBean> addressBeanList=new ArrayList<>();
        return addressBeanList;
    }

    public String getReceivingStreet() {
        return receivingStreet;
    }

    public void setReceivingStreet(String receivingStreet) {
        this.receivingStreet = receivingStreet;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
