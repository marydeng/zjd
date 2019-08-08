package poll.com.zjd.model;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/1  23:22
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 * 首页分类的商品对象
 */

public class Product {

    /**
     * brandName : 麦香
     * catName : 红酒
     * catNo : vx
     * commnetCount : 0
     * commodityAlias : ceshijiu
     * commodityId : 402899965cde7dc9015cdec8a6ac001c
     * commodityName : 测试酒
     * commodityNo : 062510958172
     * commodityPicVo : {"picBig":"/upload/image/201707/4bf4629d70c840038ae76152ec81341d_big.jpg","picDesc":"/upload/image/201707/4bf4629d70c840038ae76152ec81341d_desc.jpg","picLarge":"l","picMiddle":"/upload/image/201707/4bf4629d70c840038ae76152ec81341d_middle.jpg","picSmall":"/upload/image/201707/4bf4629d70c840038ae76152ec81341d_small.jpg","picSmallLest":"c","picThumbnail":"/upload/image/201707/4bf4629d70c840038ae76152ec81341d_thumbnail.jpg"}
     * integral : 0
     * origin :
     * publicPrice : 10
     * reason :
     * saleCount : 0
     * salePrice : 1.2
     * sortNo : 0
     * tagsId :
     * tagsNo :
     */

    private String brandName;
    private String catName;
    private String catNo;
    private int commnetCount;
    private String commodityAlias;
    private String commodityId;
    private String commodityName;
    private String commodityNo;
    /**
     * picBig : /upload/image/201707/4bf4629d70c840038ae76152ec81341d_big.jpg
     * picDesc : /upload/image/201707/4bf4629d70c840038ae76152ec81341d_desc.jpg
     * picLarge : l
     * picMiddle : /upload/image/201707/4bf4629d70c840038ae76152ec81341d_middle.jpg
     * picSmall : /upload/image/201707/4bf4629d70c840038ae76152ec81341d_small.jpg
     * picSmallLest : c
     * picThumbnail : /upload/image/201707/4bf4629d70c840038ae76152ec81341d_thumbnail.jpg
     */

    private CommodityPicVoBean commodityPicVo;
    private int integral;
    private String origin;
    private int publicPrice;
    private String reason;
    private int saleCount;
    private double salePrice;
    private int sortNo;
    private String tagsId;
    private String tagsNo;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatNo() {
        return catNo;
    }

    public void setCatNo(String catNo) {
        this.catNo = catNo;
    }

    public int getCommnetCount() {
        return commnetCount;
    }

    public void setCommnetCount(int commnetCount) {
        this.commnetCount = commnetCount;
    }

    public String getCommodityAlias() {
        return commodityAlias;
    }

    public void setCommodityAlias(String commodityAlias) {
        this.commodityAlias = commodityAlias;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public CommodityPicVoBean getCommodityPicVo() {
        return commodityPicVo;
    }

    public void setCommodityPicVo(CommodityPicVoBean commodityPicVo) {
        this.commodityPicVo = commodityPicVo;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(int publicPrice) {
        this.publicPrice = publicPrice;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public String getTagsId() {
        return tagsId;
    }

    public void setTagsId(String tagsId) {
        this.tagsId = tagsId;
    }

    public String getTagsNo() {
        return tagsNo;
    }

    public void setTagsNo(String tagsNo) {
        this.tagsNo = tagsNo;
    }

    public static class CommodityPicVoBean {
        private String picBig;
        private String picDesc;
        private String picLarge;
        private String picMiddle;
        private String picSmall;
        private String picSmallLest;
        private String picThumbnail;

        public String getPicBig() {
            return picBig;
        }

        public void setPicBig(String picBig) {
            this.picBig = picBig;
        }

        public String getPicDesc() {
            return picDesc;
        }

        public void setPicDesc(String picDesc) {
            this.picDesc = picDesc;
        }

        public String getPicLarge() {
            return picLarge;
        }

        public void setPicLarge(String picLarge) {
            this.picLarge = picLarge;
        }

        public String getPicMiddle() {
            return picMiddle;
        }

        public void setPicMiddle(String picMiddle) {
            this.picMiddle = picMiddle;
        }

        public String getPicSmall() {
            return picSmall;
        }

        public void setPicSmall(String picSmall) {
            this.picSmall = picSmall;
        }

        public String getPicSmallLest() {
            return picSmallLest;
        }

        public void setPicSmallLest(String picSmallLest) {
            this.picSmallLest = picSmallLest;
        }

        public String getPicThumbnail() {
            return picThumbnail;
        }

        public void setPicThumbnail(String picThumbnail) {
            this.picThumbnail = picThumbnail;
        }
    }
}
