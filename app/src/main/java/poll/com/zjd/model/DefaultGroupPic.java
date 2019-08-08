package poll.com.zjd.model;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/10
 * 文件描述：
 */

/**
 * "propKey": null,
 * "picSmallUrl": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_small.jpg",
 * "pic100x100": null,
 * "picBigUrl": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_big.jpg",
 * "picLargeUrl": "/upload/image/201707/4bf4629d70c840038ae76152ec81341d_large.jpg",
 * "sortNo": 0
 */
public class DefaultGroupPic {
    private String propKey;
    private String picSmallUrl;
    private String pic100x100;
    private String picBigUrl;
    private String picLargeUrl;
    private int sortNo;

    public DefaultGroupPic() {
        super();
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getPicSmallUrl() {
        return picSmallUrl;
    }

    public void setPicSmallUrl(String picSmallUrl) {
        this.picSmallUrl = picSmallUrl;
    }

    public String getPic100x100() {
        return pic100x100;
    }

    public void setPic100x100(String pic100x100) {
        this.pic100x100 = pic100x100;
    }

    public String getPicBigUrl() {
        return picBigUrl;
    }

    public void setPicBigUrl(String picBigUrl) {
        this.picBigUrl = picBigUrl;
    }

    public String getPicLargeUrl() {
        return picLargeUrl;
    }

    public void setPicLargeUrl(String picLargeUrl) {
        this.picLargeUrl = picLargeUrl;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }
}
