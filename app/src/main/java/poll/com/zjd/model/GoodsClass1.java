package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/13  10:00
 * 包名:     poll.com.zjd.model
 * 项目名:   zjd
 */

public class GoodsClass1 {
    private List<GoodBean1> pojoJson;
    private SearchVoBean searchVo;
    private int total;
    private List<CessorBean> cessordata;


    public static class SearchVoBean{
        private String param2;
        private String param3;
        private String param4;
        private String param6;
        private String param8;
        private String param9;

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        public String getParam3() {
            return param3;
        }

        public void setParam3(String param3) {
            this.param3 = param3;
        }

        public String getParam4() {
            return param4;
        }

        public void setParam4(String param4) {
            this.param4 = param4;
        }

        public String getParam6() {
            return param6;
        }

        public void setParam6(String param6) {
            this.param6 = param6;
        }

        public String getParam8() {
            return param8;
        }

        public void setParam8(String param8) {
            this.param8 = param8;
        }

        public String getParam9() {
            return param9;
        }

        public void setParam9(String param9) {
            this.param9 = param9;
        }
    }

    public List<GoodBean1> getPojoJson() {
        return pojoJson;
    }

    public void setPojoJson(List<GoodBean1> pojoJson) {
        this.pojoJson = pojoJson;
    }

    public SearchVoBean getSearchVo() {
        return searchVo;
    }

    public void setSearchVo(SearchVoBean searchVo) {
        this.searchVo = searchVo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CessorBean> getCessordata() {
        return cessordata;
    }

    public void setCessordata(List<CessorBean> cessordata) {
        this.cessordata = cessordata;
    }
}
