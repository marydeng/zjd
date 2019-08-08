package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/17  22:56
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class ProductBean {
    private boolean sellOut;    //是否卖完了
    private boolean select;     //是否选择了
    private int count;          //商品数量
    private GoodBean1 goodsBean1;
    private boolean isSecondKill;
    private double secondKillPrice;

    public boolean isSellOut() {
        return sellOut;
    }

    public void setSellOut(boolean sellOut) {
        this.sellOut = sellOut;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public GoodBean1 getGoodsBean1() {
        return goodsBean1;
    }

    public void setGoodsBean1(GoodBean1 goodsBean1) {
        this.goodsBean1 = goodsBean1;
    }

    public boolean isSecondKill() {
        return isSecondKill;
    }

    public void setSecondKill(boolean secondKill) {
        isSecondKill = secondKill;
    }

    public double getSecondKillPrice() {
        return secondKillPrice;
    }

    public void setSecondKillPrice(double secondKillPrice) {
        this.secondKillPrice = secondKillPrice;
    }
}
