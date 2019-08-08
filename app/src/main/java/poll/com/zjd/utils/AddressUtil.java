package poll.com.zjd.utils;

import poll.com.zjd.R;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.model.AddressBean;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/8
 * 文件描述：地址相关公共方法
 */
public class AddressUtil {
    public static String convertAddressTypeName(int type) {
        String addressStr;
        if (AddressBean.ConcatType.company == type) {
            addressStr = AppContext.getInstance().getString(R.string.company);
        } else if (AddressBean.ConcatType.school == type) {
            addressStr = AppContext.getInstance().getString(R.string.school);
        } else if (AddressBean.ConcatType.residential == type) {
            addressStr = AppContext.getInstance().getString(R.string.residential);
        } else {
            addressStr = AppContext.getInstance().getString(R.string.other);
        }
        return addressStr;
    }

    public static int convertAddressType(String addressStr) {
        int addressType;
        if (addressStr.equals(AppContext.getInstance().getString(R.string.company))) {
            addressType = AddressBean.ConcatType.company;
        } else if (addressStr.equals(AppContext.getInstance().getString(R.string.school))) {
            addressType = AddressBean.ConcatType.school;
        } else if (addressStr.equals(AppContext.getInstance().getString(R.string.residential))) {
            addressType = AddressBean.ConcatType.residential;
        } else {
            addressType = AddressBean.ConcatType.other;
        }
        return addressType;
    }
}
