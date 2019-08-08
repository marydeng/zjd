package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/9/22  0:27
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

/**
 * /api/user/validatePhone.sc?phone=12345678901
 * isRegister=0,代表未注册， isRegister=1 代表已注册，
 * isBind=0代表该手机号未绑定微信，isBind=1代表该手机号已绑定微信
 */
public class CheckPhone {
    private Integer isRegister;
    private Integer isBind;

    public Integer getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(Integer isRegister) {
        this.isRegister = isRegister;
    }

    public Integer getIsBind() {
        return isBind;
    }

    public void setIsBind(Integer isBind) {
        this.isBind = isBind;
    }
}
