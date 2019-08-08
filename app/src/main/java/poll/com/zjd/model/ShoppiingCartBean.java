package poll.com.zjd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/13
 * 文件描述：加入购物车的商品类
 */
public class ShoppiingCartBean {
    private boolean isChosen = true;
    private int count;
    private CartAddBean cartAddBean;


    public ShoppiingCartBean(int count, CartAddBean cartAddBean) {
        this.count = count;
        this.cartAddBean = cartAddBean;
    }

    public boolean isChosen() {
        return isChosen &&!isSoldOut() && !isOffShelves();
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSoldOut() {
        return cartAddBean.getStock()==0;
        //Todo 目前返回数据有问题，测试用
//        return false;
    }

    public int getStockCount() {
        return cartAddBean.getStock();
    }


    public boolean isOffShelves() {
        return  cartAddBean.isOff_shelves();
    }

    public CartAddBean getCartAddBean() {
        return cartAddBean;
    }

    public void setCartAddBean(CartAddBean cartAddBean) {
        this.cartAddBean = cartAddBean;
    }
}
