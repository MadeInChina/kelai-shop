package cn.com.kelaikewang.core.article.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface ArticleCategory  extends Serializable {
    Long getId();

    void setId(Long id);

    ArticleCategory getParent() ;

    void setParent(ArticleCategory parent);

    String getName();

    void setName(String name);

    String getMetaKeywords();

    void setMetaKeywords(String metaKeywords);

    String getMetaDescription();

    void setMetaDescription(String metaDescription);

    Date getCreated();

    void setCreated(Date created);

    Date getModified();

    void setModified(Date modified);

    List<ArticleCategory> getChildren();

    void setChildren(List<ArticleCategory> children);

    List<Article> getArticles();

    void setArticles(List<Article> articles);

    String getDescription();

    void setDescription(String description);

    String getCssClass();

    void setCssClass(String icon);

    Integer getCount();

    void setCount(Integer count);

    Integer getOrderNumber();

    void setOrderNumber(Integer orderNumber);
    String getIdentify();

    void setIdentify(String identify);
}
