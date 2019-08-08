package poll.com.zjd.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import poll.com.zjd.R;
import poll.com.zjd.app.AppContext;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/17
 * 文件描述：虚拟账户交易流水类
 */

//        "id": "402899a15d4ae0f4015d4ae5a68d0002",
//                "numberId": "20170716181550961730",
//                "orderNum": "",
//                "transAmt": 100.0,
//                "transDate": "2017-07-16 18:15:50",
//                "transSummary": "用户于 2017-07-16 18:15:50 充值金额: 100.0",
//                "transType": 0

public class BalanceBean {
    private String id;
    private String numberId;
    private String orderNum;
    private float transAmt;
    private String transDate;
    private String transSummary;
    private int transType;
    public interface TansType{
        int recharge=0;
        int rechargeGet=1;
        int consume=2;
        int refund =3;
    }

    public BalanceBean() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public float getTransAmt() {
        return transAmt;
    }

    public void setTransAmt(float transAmt) {
        this.transAmt = transAmt;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransSummary() {
        return transSummary;
    }

    public void setTransSummary(String transSummary) {
        this.transSummary = transSummary;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public static List<BalanceBean> createBalanceBeanList() {
        List<BalanceBean> balanceBeanList = new ArrayList<>();
        return balanceBeanList;
    }
}
