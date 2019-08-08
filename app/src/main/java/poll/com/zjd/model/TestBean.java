package poll.com.zjd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  15:11
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class TestBean {

    private String name;

    private String url;

    private float price;

    public TestBean(String name,String url,float price){
        this.name = name;
        this.url = url;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public static List<TestBean> getDates(){
        List<TestBean> dates = new ArrayList<>();
        dates.add(new TestBean("Tommy","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2629193414,4153540306&fm=23&gp=0.jpg",125));
        dates.add(new TestBean("Lucy","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490949704743&di=0dc777cb8a63fe8bad868851fdfc4b4b&imgtype=0&src=http%3A%2F%2Fwww.windows7en.com%2Fuploads%2Fallimg%2F141222%2F1-141222102029394.jpg",12));
        dates.add(new TestBean("Amy","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2715417528,1907297180&fm=23&gp=0.jpg",160));
        dates.add(new TestBean("MiKy","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4154130111,2586826945&fm=23&gp=0.jpg",99));
        dates.add(new TestBean("Marry","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490949642213&di=aba896928bc3886b1ce9499e25a2c26c&imgtype=0&src=http%3A%2F%2Fimg15.3lian.com%2F2015%2Ff2%2F57%2Fd%2F24.jpg",88));
        dates.add(new TestBean("Too","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2629193414,4153540306&fm=23&gp=0.jpg",66));
        dates.add(new TestBean("Koo","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490949704743&di=0dc777cb8a63fe8bad868851fdfc4b4b&imgtype=0&src=http%3A%2F%2Fwww.windows7en.com%2Fuploads%2Fallimg%2F141222%2F1-141222102029394.jpg",111));
        dates.add(new TestBean("Hay","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2715417528,1907297180&fm=23&gp=0.jpg",33));
        dates.add(new TestBean("Yao","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4154130111,2586826945&fm=23&gp=0.jpg",55));

        return dates;
    }
    public static List<TestBean> getDates1(){
        List<TestBean> dates = new ArrayList<>();
        dates.add(new TestBean("Tommy","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2629193414,4153540306&fm=23&gp=0.jpg",999));
        dates.add(new TestBean("Lucy","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490949704743&di=0dc777cb8a63fe8bad868851fdfc4b4b&imgtype=0&src=http%3A%2F%2Fwww.windows7en.com%2Fuploads%2Fallimg%2F141222%2F1-141222102029394.jpg",1110));
        dates.add(new TestBean("Amy","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2715417528,1907297180&fm=23&gp=0.jpg",666));
        dates.add(new TestBean("MiKy","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4154130111,2586826945&fm=23&gp=0.jpg",899));
        dates.add(new TestBean("Marry","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490949642213&di=aba896928bc3886b1ce9499e25a2c26c&imgtype=0&src=http%3A%2F%2Fimg15.3lian.com%2F2015%2Ff2%2F57%2Fd%2F24.jpg",2450));
        dates.add(new TestBean("Too","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2629193414,4153540306&fm=23&gp=0.jpg",788));
        dates.add(new TestBean("Koo","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490949704743&di=0dc777cb8a63fe8bad868851fdfc4b4b&imgtype=0&src=http%3A%2F%2Fwww.windows7en.com%2Fuploads%2Fallimg%2F141222%2F1-141222102029394.jpg",1999));
        dates.add(new TestBean("Hay","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2715417528,1907297180&fm=23&gp=0.jpg",888));
        dates.add(new TestBean("Yao","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4154130111,2586826945&fm=23&gp=0.jpg",996));

        return dates;
    }
    public static List<TestBean> getBuities(){
        List<TestBean> dates = new ArrayList<>();
        dates.add(new TestBean("Tommy","http://pic.jj20.com/up/allimg/911/100416110525/161004110525-4.jpg",999));
        dates.add(new TestBean("Lucy","http://pic.jj20.com/up/allimg/911/100416110525/161004110525-5.jpg",1110));
        dates.add(new TestBean("Amy","http://pic.jj20.com/up/allimg/911/100416110525/161004110525-6.jpg",666));
        dates.add(new TestBean("MiKy","http://pic.jj20.com/up/allimg/911/100416110525/161004110525-8.jpg",899));
        return dates;
    }
    public static List<String> getStringDates(){
        List<String> dates = new ArrayList<>();
        dates.add("テーマツアー");
        dates.add("グッズツアー");
        dates.add("日帰り旅行");
        dates.add("ハイエンド泳ぐ");
        return dates;
    }
}
