package cn.com.kelaikewang.core.article.dto;

import cn.com.kelaikewang.infrastructure.dto.PageQueryObject;

public class ArticlePaginationQueryDTO extends PageQueryObject {
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
