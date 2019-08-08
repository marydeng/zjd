package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/19
 * 文件描述：生成支付信息返回
 */
public class CreatePayInfoResponse {
    private String createTime;
    private String id;
    private double orderPayTotalAmont;


    public CreatePayInfoResponse() {
        super();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getOrderPayTotalAmont() {
        return orderPayTotalAmont;
    }

    public void setOrderPayTotalAmont(double orderPayTotalAmont) {
        this.orderPayTotalAmont = orderPayTotalAmont;
    }
}
