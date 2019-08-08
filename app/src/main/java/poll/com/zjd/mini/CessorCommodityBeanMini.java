package poll.com.zjd.mini;

import poll.com.zjd.model.CessorCommodityBean;
import poll.com.zjd.utils.ObjectUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/9/4
 * 文件描述：
 */
public class CessorCommodityBeanMini {
    public static String getProductId(CessorCommodityBean cessorCommodityBean){
        String productId="";
        if(null!=cessorCommodityBean && cessorCommodityBean.getCommodityDetailVo()!=null && !ObjectUtils.isEmpty(cessorCommodityBean.getCommodityDetailVo().getLstGoods())){
            productId=cessorCommodityBean.getCommodityDetailVo().getLstGoods().get(0).getGoodsId();
        }
        return productId;
    }

    public static String getProductNo(CessorCommodityBean cessorCommodityBean){
        String productNo="";
        if(null!=cessorCommodityBean && cessorCommodityBean.getCommodityDetailVo()!=null && !ObjectUtils.isEmpty(cessorCommodityBean.getCommodityDetailVo().getLstGoods())){
            productNo=cessorCommodityBean.getCommodityDetailVo().getLstGoods().get(0).getGoodsNo();
        }
        return productNo;
    }

}
