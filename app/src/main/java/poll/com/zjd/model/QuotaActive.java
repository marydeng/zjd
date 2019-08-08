package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/20
 * 文件描述：
 */
public class QuotaActive {
    private  double freeFullAmount;
    private double freeFullBalance;
    private boolean isJoin;

    public double getFreeFullAmount() {
        return freeFullAmount;
    }

    public void setFreeFullAmount(double freeFullAmount) {
        this.freeFullAmount = freeFullAmount;
    }

    public double getFreeFullBalance() {
        return freeFullBalance;
    }

    public void setFreeFullBalance(double freeFullBalance) {
        this.freeFullBalance = freeFullBalance;
    }

    public boolean isJoin() {
        return isJoin;
    }

    public void setJoin(boolean join) {
        isJoin = join;
    }
}
