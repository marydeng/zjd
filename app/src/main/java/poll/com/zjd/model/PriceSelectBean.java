package poll.com.zjd.model;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/30  15:31
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class PriceSelectBean extends BaseModel{
    private static String[] priceStr = {"0-49","50-99","100-199","200-599","600-999","1000以上"};
    private static String[] locationStr = {
            "中国大陆","法国","意大利","西班牙","瑞典","荷兰",
            "德国","澳大利亚","南非","俄罗斯","波兰","英国",
            "加拿大","智利","摩尔多瓦","罗马尼亚","墨西哥","古巴",
            "美国","比利时","丹麦","捷克","阿根廷","新西兰",
            "苏格兰","葡萄牙","奥地利","匈牙利","格鲁吉亚"};
    private String price;  //价格区间
    public ObservableBoolean obsSelect = new ObservableBoolean(false);//是否被选中

    public PriceSelectBean(String price,boolean select){
        this.price = price;
        this.obsSelect.set(select);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static List<PriceSelectBean> productClassBeanList(){
        List<PriceSelectBean> list = new ArrayList<>();
        for (String s : priceStr) {
            PriceSelectBean p = new PriceSelectBean(s,false);
            list.add(p);
        }
        return list;
    }

    public static List<PriceSelectBean> classLocationBeanList(){
        List<PriceSelectBean> list = new ArrayList<>();
        for (String s : locationStr) {
            PriceSelectBean p = new PriceSelectBean(s,false);
            list.add(p);
        }
        return list;
    }
}
