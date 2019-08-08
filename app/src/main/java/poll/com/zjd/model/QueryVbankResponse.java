package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/22
 * 文件描述：
 */
public class QueryVbankResponse {
    private double availableAmt;
    private double freezeAmt;
    private String id;
    private double inTotalAmt;
    private double outTotalAmt;

    public QueryVbankResponse(){
        super();
    }

    public double getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(double availableAmt) {
        this.availableAmt = availableAmt;
    }

    public double getFreezeAmt() {
        return freezeAmt;
    }

    public void setFreezeAmt(double freezeAmt) {
        this.freezeAmt = freezeAmt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getInTotalAmt() {
        return inTotalAmt;
    }

    public void setInTotalAmt(double inTotalAmt) {
        this.inTotalAmt = inTotalAmt;
    }

    public double getOutTotalAmt() {
        return outTotalAmt;
    }

    public void setOutTotalAmt(double outTotalAmt) {
        this.outTotalAmt = outTotalAmt;
    }
}
