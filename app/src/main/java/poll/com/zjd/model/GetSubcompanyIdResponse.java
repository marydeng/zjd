package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/9/3
 * 文件描述：获取运营点id返回
 */
public class GetSubcompanyIdResponse {
    /**
     * "subcompanyId": "402899965cdd4235015cdd68223a0003",
     * "twoHoursDelivery": false
     */
    private String subcompanyId;
    private boolean twoHoursDelivery;
    private String startTime;
    private String endTime;

    public String getSubcompanyId() {
        return subcompanyId;
    }

    public void setSubcompanyId(String subcompanyId) {
        this.subcompanyId = subcompanyId;
    }

    public boolean isTwoHoursDelivery() {
        return twoHoursDelivery;
    }

    public void setTwoHoursDelivery(boolean twoHoursDelivery) {
        this.twoHoursDelivery = twoHoursDelivery;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
