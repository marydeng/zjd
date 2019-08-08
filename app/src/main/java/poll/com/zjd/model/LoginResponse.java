package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/17
 * 文件描述：登录返回
 */
public class LoginResponse {
    private String id;
    private String phone;
    private String logName;
    private String openId;

    private LoginResponse() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
