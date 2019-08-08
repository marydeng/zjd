package poll.com.zjd.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import poll.com.zjd.app.AppConfig;
import poll.com.zjd.app.AppContext;
import poll.com.zjd.mini.GoodsBeanMini;
import poll.com.zjd.model.CardBean;
import poll.com.zjd.model.CartAddBean;
import poll.com.zjd.model.CouponsBean;
import poll.com.zjd.model.GoodsBean;
import poll.com.zjd.model.ShoppiingCartBean;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/13
 * 文件描述：购物车管理类
 */
public class ShoppingCartUtil {
    private static ShoppingCartUtil instance;
    public static String GOODS_LIST_DATA = "goods_list_data";
    private Map<String, ShoppiingCartBean> goodsMap;
    public   List<CouponsBean> couponsBeanList;


    private ShoppingCartUtil() {
        SPUtils tokenSpUtils = new SPUtils(AppContext.getInstance().getApplicationContext(), SPUtils.CACHE_ACCESSTOKEN_INFO);
        String loginId = tokenSpUtils.getString(AppContext.getInstance().getApplicationContext(), AppConfig.AccessTokenSPKey.LoginId);
        SPUtils spUtils = new SPUtils(AppContext.getInstance().getApplicationContext(), SPUtils.SHOPP_CART_GOODS_DATA);
        String goodsJsonStr = spUtils.getString(AppContext.getInstance().getApplicationContext(), loginId);
        if (StringUtils.isBlank(goodsJsonStr)) {
            goodsMap = new HashMap<>();
        } else {
            goodsMap = JsonUtils.jsonToHashMap(goodsJsonStr);
        }
    }

    public static synchronized ShoppingCartUtil getInstance() {
        if (null == instance) {
            instance = new ShoppingCartUtil();
        }
        return instance;
    }

    public void addGood(int count, CartAddBean cartAddBean) {
        if (goodsMap.containsKey(cartAddBean.getProductId())) {
            ShoppiingCartBean cartBean = goodsMap.get(cartAddBean.getProductId());
            cartBean.setCount(cartBean.getCount() + count);
            cartBean.setCartAddBean(cartAddBean);
        } else {
            ShoppiingCartBean cartBean = new ShoppiingCartBean(count, cartAddBean);
            if (!StringUtils.isBlank(cartAddBean.getProductId())) {
                goodsMap.put(cartAddBean.getProductId(), cartBean);
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveDataSP();
            }
        });
        thread.start();
    }

    public void subtractGood(int count, CartAddBean cartAddBean) {
        if (goodsMap.containsKey(cartAddBean.getProductId())) {
            ShoppiingCartBean cartBean = goodsMap.get(cartAddBean.getProductId());
            if (cartBean.getCount() - count <= 0) {
                goodsMap.remove(cartAddBean.getProductId());
            } else {
                cartBean.setCount(cartBean.getCount() - count);
                cartBean.setCartAddBean(cartAddBean);
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveDataSP();
            }
        });
        thread.start();
    }

    public void removeGood(String goodId) {
        if (goodsMap.containsKey(goodId)) {
            goodsMap.remove(goodId);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveDataSP();
            }
        });
        thread.start();
    }

    public List<ShoppiingCartBean> getShoppingCartBeanList() {
        List<ShoppiingCartBean> shoppiingCartBeanList = new ArrayList<>();
        shoppiingCartBeanList.addAll(goodsMap.values());
        return shoppiingCartBeanList;
    }

    public List<ShoppiingCartBean> getChooseBuyList(){
        List<ShoppiingCartBean> shoppiingCartBeanList = new ArrayList<>();
       for(ShoppiingCartBean cartBean:goodsMap.values()){
           if(cartBean.isChosen()){
               shoppiingCartBeanList.add(cartBean);
           }
       }
        return shoppiingCartBeanList;
    }

    public void saveDataSP() {
        SPUtils tokenSpUtils = new SPUtils(AppContext.getInstance().getApplicationContext(), SPUtils.CACHE_ACCESSTOKEN_INFO);
        String loginId = tokenSpUtils.getString(AppContext.getInstance().getApplicationContext(), AppConfig.AccessTokenSPKey.LoginId);
        SPUtils spUtils = new SPUtils(AppContext.getInstance().getApplicationContext(), SPUtils.SHOPP_CART_GOODS_DATA);
        if (!StringUtils.isBlank(loginId)) {
            if (goodsMap != null && !ObjectUtils.isEmpty(goodsMap.values())) {
                String goodsJsonStr = JsonUtils.mapToStr(goodsMap);
                spUtils.put(AppContext.getInstance().getApplicationContext(), loginId, goodsJsonStr);
            } else {
                spUtils.remove(AppContext.getInstance().getApplicationContext(), loginId);
            }
        }
    }

    public void clearSoldOutGoods() {
        Iterator<String> it = goodsMap.keySet().iterator();
        String key = null;
        while (it.hasNext()) {
            key = it.next();
            ShoppiingCartBean shoppiingCartBean = goodsMap.get(key);
            if (shoppiingCartBean.isSoldOut()) {
                it.remove();
            }
        }
        saveDataSP();
    }

    public int getGoodsNum() {
        int num = 0;
        List<ShoppiingCartBean> shoppiingCartBeanList = new ArrayList<>();
        shoppiingCartBeanList.addAll(goodsMap.values());
        if (!ObjectUtils.isEmpty(shoppiingCartBeanList)) {
            for (ShoppiingCartBean cartBean : shoppiingCartBeanList) {
                num = num + cartBean.getCount();
            }
        }
        sortGoodsList(shoppiingCartBeanList);
        return num;
    }

    public int getGoodsCount(String goodsId){
        int count=0;
        if (goodsMap.containsKey(goodsId)) {
            ShoppiingCartBean cardBean=goodsMap.get(goodsId);
            count=cardBean.getCount();
        }
        return count;
    }

    public void sortGoodsList(List<ShoppiingCartBean> shoppiingCartBeanList){
        Collections.sort(shoppiingCartBeanList, new Comparator<ShoppiingCartBean>() {
            @Override
            public int compare(ShoppiingCartBean o1, ShoppiingCartBean o2) {
                LogUtils.i("o1.getId=" + o1.getCartAddBean().getProductId() + ",o2.getId=" + o2.getCartAddBean().getProductId());
                int index = 0;
                if ((o1.isOffShelves() || o1.isSoldOut()) && (!o2.isOffShelves() && !o2.isSoldOut())) {
                    index = 1;
                } else if ((o2.isOffShelves() || o2.isSoldOut()) && (!o1.isOffShelves() && !o1.isSoldOut())) {
                    index = -1;
                } else if (!o1.getCartAddBean().isCessor() && o2.getCartAddBean().isCessor()) {
                    index = 1;
                } else if (!o2.getCartAddBean().isCessor() && o1.getCartAddBean().isCessor()) {
                    index = -1;
                }
                LogUtils.i("o1,index=" + index);
                return index;
            }
        });
    }

    public int getGoodsNum(List<ShoppiingCartBean> shoppiingCartBeenList){
        int count=0;
        if(!ObjectUtils.isEmpty(shoppiingCartBeenList)){
            for(ShoppiingCartBean shoppiingCartBean:shoppiingCartBeenList){
                count+=shoppiingCartBean.getCount();
            }
        }
        return count;
    }

    public int  getCardAddBeanCount(String goodId) {
        int count=0;
        if (goodsMap.containsKey(goodId)) {
            ShoppiingCartBean cartBean = goodsMap.get(goodId);
            count=cartBean.getCount();
        }
        return count;
    }

    public void clearAllGoods() {
        goodsMap = new HashMap<>();
        saveDataSP();
    }

    public void removeChooseGoods(){
        List<ShoppiingCartBean> shoppiingCartBeanList = new ArrayList<>();
        shoppiingCartBeanList.addAll(goodsMap.values());
        if (!ObjectUtils.isEmpty(shoppiingCartBeanList)) {
            for (ShoppiingCartBean cartBean : shoppiingCartBeanList) {
                if(cartBean.isChosen()){
                    goodsMap.remove(cartBean.getCartAddBean().getProductId());
                }
            }
        }
        saveDataSP();
    }

    public static void clearInstance() {
        instance = null;
    }
}
