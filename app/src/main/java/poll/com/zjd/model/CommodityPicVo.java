package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/9
 * 文件描述：商品类的图片url类
 */

/**
 * "picLarge": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_large.jpg",
 * "picDesc": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_desc.jpg",
 * "picSmall": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_small.jpg",
 * "picBig": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_big.jpg",
 * "picMiddle": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_middle.jpg",
 * "picThumbnail": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_thumbnail.jpg",
 * "picSmallLest": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_smalllest.jpg"
 * },
 */
public class CommodityPicVo {
    private String picLarge;
    private String picDesc;
    private String picSmall;
    private String picBig;
    private String picMiddle;
    private String picThumbnail;
    private String picSmallLest;


    public CommodityPicVo() {
        super();
    }

    public String getPicLarge() {
        return picLarge;
    }

    public void setPicLarge(String picLarge) {
        this.picLarge = picLarge;
    }

    public String getPicDesc() {
        return picDesc;
    }

    public void setPicDesc(String picDesc) {
        this.picDesc = picDesc;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public String getPicBig() {
        return picBig;
    }

    public void setPicBig(String picBig) {
        this.picBig = picBig;
    }

    public String getPicMiddle() {
        return picMiddle;
    }

    public void setPicMiddle(String picMiddle) {
        this.picMiddle = picMiddle;
    }

    public String getPicThumbnail() {
        return picThumbnail;
    }

    public void setPicThumbnail(String picThumbnail) {
        this.picThumbnail = picThumbnail;
    }

    public String getPicSmallLest() {
        return picSmallLest;
    }

    public void setPicSmallLest(String picSmallLest) {
        this.picSmallLest = picSmallLest;
    }
}
