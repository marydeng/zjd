package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/17
 * 文件描述：
 */

/**
 "integrals": "0",
 "balance": "2198.0",
 "couponNum": "0",
 */
public class UserInfoBean {
    private String id;
    private String openId;
    private String phone;
    private String logName;
    private String wxUserName;
    private String isBindPhone;
    private String experienceTotal;
    private String currentLevel;
    private String nextLevel;
    private String nextLevelAmplitude;
    private String nextLevelExperience;
    private String integrals;
    private double balance;
    private String couponNum;
    private int isSavePWD;
    public interface  IsSavePWD{
        int hasPWD=1;
        int notPWD=0;
    }
    private String headImageUrl;

    private int couponAmount;

    private UserInfoBean() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getWxUserName() {
        return wxUserName;
    }

    public void setWxUserName(String wxUserName) {
        this.wxUserName = wxUserName;
    }

    public String getIsBindPhone() {
        return isBindPhone;
    }

    public void setIsBindPhone(String isBindPhone) {
        this.isBindPhone = isBindPhone;
    }

    public String getExperienceTotal() {
        return experienceTotal;
    }

    public void setExperienceTotal(String experienceTotal) {
        this.experienceTotal = experienceTotal;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(String nextLevel) {
        this.nextLevel = nextLevel;
    }

    public String getNextLevelAmplitude() {
        return nextLevelAmplitude;
    }

    public void setNextLevelAmplitude(String nextLevelAmplitude) {
        this.nextLevelAmplitude = nextLevelAmplitude;
    }

    public String getNextLevelExperience() {
        return nextLevelExperience;
    }

    public void setNextLevelExperience(String nextLevelExperience) {
        this.nextLevelExperience = nextLevelExperience;
    }

    public String getIntegrals() {
        return integrals;
    }

    public void setIntegrals(String integrals) {
        this.integrals = integrals;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(String couponNum) {
        this.couponNum = couponNum;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public int getIsSavePWD() {
        return isSavePWD;
    }

    public void setIsSavePWD(int isSavePWD) {
        this.isSavePWD = isSavePWD;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }
}
