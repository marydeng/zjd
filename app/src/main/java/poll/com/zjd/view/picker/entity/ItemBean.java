package poll.com.zjd.view.picker.entity;

import java.util.List;

/**
 * Author:matt : addapp.cn
 * DateTime:2016-10-15 19:06
 */
public abstract class ItemBean extends JavaBean {
    private int child;
    private String id;
    private String isleaf;
    private int level;
    private String name;
    private String no;
    private String post;
    private String region;
    private int sort;

    public String getAreaId() {
        return id;
    }

    public void setAreaId(String areaId) {
        this.id = areaId;
    }

    public String getAreaName() {
        return name;
    }

    public void setAreaName(String areaName) {
        this.name = areaName;
    }


    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
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

    @Override
    public String toString() {
        return "areaId=" + id + ",areaName=" + name;
    }
}