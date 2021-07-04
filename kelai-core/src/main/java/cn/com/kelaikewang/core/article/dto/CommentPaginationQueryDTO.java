package cn.com.kelaikewang.core.article.dto;

import cn.com.kelaikewang.infrastructure.dto.PageQueryObject;

public class CommentPaginationQueryDTO extends PageQueryObject {
    private Long articleId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
