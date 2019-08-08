package poll.com.zjd.mini;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.model.BasePromotionActiveVO;
import poll.com.zjd.model.DefaultGroupPic;
import poll.com.zjd.model.ExtendPropList;
import poll.com.zjd.model.GoodsBean;
import poll.com.zjd.model.GoodsCommodityDetailVo;
import poll.com.zjd.utils.ObjectUtils;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/10
 * 文件描述：
 */
public class GoodsBeanMini {
    public static String getProdName(GoodsBean goodsBean) {
        String prodName = "";
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            if (null != goodsBean.getGoodsCommodityDetailVo().getProdName()) {
                prodName = goodsBean.getGoodsCommodityDetailVo().getProdName();
            }
        }
        return prodName;
    }

    public static String getAlias(GoodsBean goodsBean) {
        String alias = "";
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            if (null != goodsBean.getGoodsCommodityDetailVo().getAlias()) {
                alias = goodsBean.getGoodsCommodityDetailVo().getAlias();
            }
        }
        return alias;
    }

    public static double getPublicPrice(GoodsBean goodsBean) {
        double publicPrice = 0.0;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            publicPrice = goodsBean.getGoodsCommodityDetailVo().getPublicPrice();
        }
        return publicPrice;
    }

    public static double getSalePrice(GoodsBean goodsBean) {
        double price = 0.0;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            price = goodsBean.getGoodsCommodityDetailVo().getSalePrice();
        }
        return price;
    }

    public static double getKillPrice(GoodsBean goodsBean){
        double price = 0.0;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo() && null!=goodsBean.getGoodsCommodityDetailVo().getBasePromotionActiveVO()) {
            price = goodsBean.getGoodsCommodityDetailVo().getBasePromotionActiveVO().getSnappingUpPrice();
        }
        return price;
    }

    public static String getProductPosterUrl(GoodsBean goodsBean) {
        String url = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            if (null != goodsBean.getGoodsCommodityDetailVo().getCommodityPicVo())
                url = goodsBean.getGoodsCommodityDetailVo().getCommodityPicVo().getPicMiddle();
        }
        return url;
    }

    public static String getProductSmallPosterUrl(GoodsBean goodsBean) {
        String url = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            if (null != goodsBean.getGoodsCommodityDetailVo().getCommodityPicVo())
                url = goodsBean.getGoodsCommodityDetailVo().getCommodityPicVo().getPicSmall();
        }
        return url;
    }

    public static String getProductDetailUrl(GoodsBean goodsBean) {
        String url = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            if (null != goodsBean.getGoodsCommodityDetailVo().getCommodityPicVo())
                url = goodsBean.getGoodsCommodityDetailVo().getCommodityPicVo().getPicDesc();
        }
        return url;
    }

    public static List<ExtendPropList> getProductSpecifications(GoodsBean goodsBean) {
        List<ExtendPropList> specificationList = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            if (!ObjectUtils.isEmpty(goodsBean.getGoodsCommodityDetailVo().getExtendPropList()))
                specificationList = goodsBean.getGoodsCommodityDetailVo().getExtendPropList();
        }
        return specificationList;
    }


    // 获取商品id
    public static String getGoodId(GoodsBean goodsBean) {
        String id = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo() && !ObjectUtils.isEmpty(goodsBean.getGoodsCommodityDetailVo().getLstGoods())) {
            id = goodsBean.getGoodsCommodityDetailVo().getLstGoods().get(0).getGoodsId();
        }
        return id;
    }

    //获取货品编号
    public static String getGoodNo(GoodsBean goodsBean) {
        String No = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo() && !ObjectUtils.isEmpty(goodsBean.getGoodsCommodityDetailVo().getLstGoods())) {
            No = goodsBean.getGoodsCommodityDetailVo().getLstGoods().get(0).getGoodsNo();
        }
        return No;
    }

    //酒品特色文字1
    public static String getWineCharacteristicOne(GoodsBean goodsBean) {
        String str = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            str = goodsBean.getGoodsCommodityDetailVo().getTitle();
        }
        return str;
    }

    //酒品特色文字2
    public static String getWineCharacteristicTwo(GoodsBean goodsBean) {
        String str = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            str = goodsBean.getGoodsCommodityDetailVo().getKeywords();
        }
        return str;
    }

    //酒品特色文字3
    public static String getWineCharacteristicThree(GoodsBean goodsBean) {
        String str = null;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            str = goodsBean.getGoodsCommodityDetailVo().getDesc();
        }
        return str;
    }

    //库存

    //是否秒杀商品
    public static boolean isSecondKillGoods(GoodsBean goodsBean) {
        boolean isSecondKill = false;
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo() && null != goodsBean.getGoodsCommodityDetailVo().getBasePromotionActiveVO()) {
            isSecondKill = BasePromotionActiveVO.ActiveType.SecondKill==goodsBean.getGoodsCommodityDetailVo().getBasePromotionActiveVO().getActiveType();
        }
        return isSecondKill;
    }

    public static void setActiveVO(GoodsBean goodsBean, int activeVO) {
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()&& null != goodsBean.getGoodsCommodityDetailVo().getBasePromotionActiveVO()) {
            goodsBean.getGoodsCommodityDetailVo().getBasePromotionActiveVO().setActiveType(activeVO);
        }
    }

    public static void setSalePrice(GoodsBean goodsBean,double salePrice){
        if (null != goodsBean && null != goodsBean.getGoodsCommodityDetailVo()) {
            goodsBean.getGoodsCommodityDetailVo().setSalePrice(salePrice);
        }
    }

    public static int getStockCount(GoodsBean goodsBean){
        int count=0;
        if(null!=goodsBean && null != goodsBean.getGoodsCommodityDetailVo()){
            if(!ObjectUtils.isEmpty(goodsBean.getGoodsCommodityDetailVo().getLstGoods())){
                count=goodsBean.getGoodsCommodityDetailVo().getLstGoods().get(0).getStockCount();
            }
        }
        return count;
    }

    public static int getSalesCount(GoodsBean goodsBean){
        int count=0;
        if(null!=goodsBean && null != goodsBean.getGoodsCommodityDetailVo()){
            count=goodsBean.getGoodsCommodityDetailVo().getSalesCount();}
        return count;
    }


    public static List<String> getImageUrlList(GoodsBean goodsBean){
        List<String> imageUrlList=new ArrayList<>();
        if(null!=goodsBean && null!=goodsBean.getGoodsCommodityDetailVo() && !ObjectUtils.isEmpty(goodsBean.getGoodsCommodityDetailVo().getDefaultGroupPic())){
            for(DefaultGroupPic defaultGroupPic:goodsBean.getGoodsCommodityDetailVo().getDefaultGroupPic()){
                imageUrlList.add(defaultGroupPic.getPicLargeUrl());
            }
        }
        return imageUrlList;
    }


}
