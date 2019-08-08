package poll.com.zjd.mini;


import poll.com.zjd.model.GoodBean1;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/9/6
 * 文件描述：
 */
public class GoodsBean1Mini {
    public static String getProductId(GoodBean1 goodsBean1) {
        String no = "";
        if (null != goodsBean1 && goodsBean1.getId() != null) {
            no = goodsBean1.getId();
        }
        return no;
    }

    public static String getProductNo(GoodBean1 goodsBean1) {
        String no = "";
        if (null != goodsBean1 && goodsBean1.getProdNo() != null) {
            no = goodsBean1.getProdNo();
        }
        return no;
    }
}
