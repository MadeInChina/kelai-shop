package cn.com.kelaikewang.infrastructure.dto;


import java.io.Serializable;
import java.util.List;

/**
 * Created by kratos on 4/6/2016.
 */
public class PageOfItems<T> implements Serializable {
    private List<T> items;

    private int pageIndex;
    private long recordCount;
    private int pageSize;

    public static final int DEFAULT_PAGE_SIZE = 10;

    public PageOfItems(){

    }

    public PageOfItems(PageQueryObject pageQuery){
        this.pageIndex = pageQuery.getPageIndex();
        this.pageSize = pageQuery.getPageSize();
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPageSize() {
        if(this.pageSize <= 0){
            return DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }
    public int getPageCount(){
        return  (int)Math.ceil((float)this.recordCount/(float) getPageSize());
    }
}

