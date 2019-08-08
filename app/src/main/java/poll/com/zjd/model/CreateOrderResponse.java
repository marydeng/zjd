package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/17
 * 文件描述：提交订单返回
 */
public class CreateOrderResponse {
    private String orderMainNo;
    private String orderSubNo;

    private CreateOrderResponse(){
        super();
    }

    public String getOrderMainNo() {
        return orderMainNo;
    }

    public void setOrderMainNo(String orderMainNo) {
        this.orderMainNo = orderMainNo;
    }

    public String getOrderSubNo() {
        return orderSubNo;
    }

    public void setOrderSubNo(String orderSubNo) {
        this.orderSubNo = orderSubNo;
    }
}
