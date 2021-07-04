package cn.com.kelaikewang.infrastructure.dto;

import java.io.Serializable;

public class PageQueryObject implements Serializable {

    private Integer pageSize = 10;

    private Integer pageIndex;

    private static final int MAX_PAGE_SIZE = 50;

    public Integer getPageSize() {
        //防止pageSize过大造成内存溢出
        if(pageSize > MAX_PAGE_SIZE){
            return MAX_PAGE_SIZE;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize > MAX_PAGE_SIZE){
            this.pageSize = MAX_PAGE_SIZE;
            return;
        }
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex <= 0){
            return 1;
        }
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getOffset(){
        return (this.getPageIndex()-1) * this.getPageSize();
    }
}

