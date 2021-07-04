package cn.com.kelaikewang.core.catalog.dto;

import cn.com.kelaikewang.infrastructure.dto.PageQueryObject;

public class ProductFilterDTO extends PageQueryObject {
    private Long categoryId;
    private String keyword;


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
