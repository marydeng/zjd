package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/18
 * 文件描述：优惠券返回类
 */
public class CouponsListBean {
    private int pageCount;
    private int startOfPage;
    private List<CouponsBean> data;
    private int pageNo;
    private boolean hasPrevious;
    private int pageSize;
    private boolean hasNext;
    private int rowCount;

    private CouponsListBean() {
        super();
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStartOfPage() {
        return startOfPage;
    }

    public void setStartOfPage(int startOfPage) {
        this.startOfPage = startOfPage;
    }

    public List<CouponsBean> getData() {
        return data;
    }

    public void setData(List<CouponsBean> data) {
        this.data = data;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
