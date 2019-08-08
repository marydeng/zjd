package poll.com.zjd.model;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.R;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/16
 * 文件描述：储值卡元数据类
 */
public class CardBean {


    /**
     "couponParValue": 20,
     "couponSchemeId": "297ead19608c5f9e01608c73334e0003",
     "giftAmount": 0,
     "id": "297ead19608c5f9e01608c7549f70006",
     "numb": 1,
     "rechargeAmount": 1,
     "schemeName": "充1送20元券"
     */
    /**
     * 背景图片
     */
    private int drawableId = -1;

    private double giftAmount;
    private String id;
    private double rechargeAmount;
    private String schemeName;
    private int numb;
    private double couponParValue;
    private String couponSchemeId;





    public CardBean() {
        super();
    }


    public int getDrawableId() {
        if (drawableId == -1) {
            if (rechargeAmount <= 200) {
                drawableId = R.drawable.my_card_blue;
            } else if (rechargeAmount <= 1000) {
                drawableId = R.drawable.my_card_purple;
            } else {
                drawableId = R.drawable.my_card_pink;
            }
        }

        return drawableId;
    }

    public double getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(double giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public int getNumb() {
        return numb;
    }

    public void setNumb(int numb) {
        this.numb = numb;
    }

    public double getCouponParValue() {
        return couponParValue;
    }

    public void setCouponParValue(double couponParValue) {
        this.couponParValue = couponParValue;
    }

    public String getCouponSchemeId() {
        return couponSchemeId;
    }

    public void setCouponSchemeId(String couponSchemeId) {
        this.couponSchemeId = couponSchemeId;
    }
}
