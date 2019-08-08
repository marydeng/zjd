package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/21
 * 文件描述：省，市，区，城镇节点类
 */
public class ChildVoBean {
    private int child;
    private List<ChildVoBean> childVo;
    private String id;
    private String isleaf;
    private int level;
    private String name;
    private String no;
    private String post;
    private String region;
    private int sort;

    public ChildVoBean(){
        super();
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public List<ChildVoBean> getChildVo() {
        return childVo;
    }

    public void setChildVo(List<ChildVoBean> childVo) {
        this.childVo = childVo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
