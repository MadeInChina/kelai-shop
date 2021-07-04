package cn.com.kelaikewang.core.article.domain;

import java.io.Serializable;

public interface ArticleFloorComment extends Serializable {
    Long getId();
    void setId(Long id);
    ArticleComment getFloorComment();
    void setFloorComment(ArticleComment floorComment);
    Integer getFloorNumber();
    void setFloorNumber(Integer floorNumber);
    ArticleComment getTrunkComment();
    void setTrunkComment(ArticleComment quoteComment);
}
