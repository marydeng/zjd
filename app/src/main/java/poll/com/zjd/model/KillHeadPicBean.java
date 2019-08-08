package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/9/10  11:07
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class KillHeadPicBean {


    /**
     * imgUrl : /images/advpic/upload/20170820miaosha.png
     * id : 8a294c115dfa6a92015dfe961b7400a2
     * imgName : 秒杀专区广告图
     * title :
     * sort : 1
     * imgType : null
     * tagName : 秒杀专区广告图
     * commonId : -1
     * clickUrl : /ylymall/commodityshow/activitiesShow/toActivitiesHtml.sc?id=-1
     * tagSubCompanyId : 402899965cdd4235015cdd68223a0003
     * advPicId : 8a294c115dfa6a92015dfe630fcf008e
     */

    private List<Row15Site6Bean> row15_site6;

    public List<Row15Site6Bean> getRow15_site6() {
        return row15_site6;
    }

    public void setRow15_site6(List<Row15Site6Bean> row15_site6) {
        this.row15_site6 = row15_site6;
    }

    public static class Row15Site6Bean {
        private String imgUrl;
        private String id;
        private String imgName;
        private String title;
        private String sort;
        private Object imgType;
        private String tagName;
        private String commonId;
        private String clickUrl;
        private String tagSubCompanyId;
        private String advPicId;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public Object getImgType() {
            return imgType;
        }

        public void setImgType(Object imgType) {
            this.imgType = imgType;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getCommonId() {
            return commonId;
        }

        public void setCommonId(String commonId) {
            this.commonId = commonId;
        }

        public String getClickUrl() {
            return clickUrl;
        }

        public void setClickUrl(String clickUrl) {
            this.clickUrl = clickUrl;
        }

        public String getTagSubCompanyId() {
            return tagSubCompanyId;
        }

        public void setTagSubCompanyId(String tagSubCompanyId) {
            this.tagSubCompanyId = tagSubCompanyId;
        }

        public String getAdvPicId() {
            return advPicId;
        }

        public void setAdvPicId(String advPicId) {
            this.advPicId = advPicId;
        }
    }
}
