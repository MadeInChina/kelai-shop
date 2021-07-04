package cn.com.kelaikewang.core.article.domain;

import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProduct;
import org.broadleafcommerce.core.catalog.domain.Indexable;

import java.util.Date;
import java.util.List;

public interface Article extends Indexable {
    Long getId();

    void setId(Long id);

    String getTitle();

    void setTitle(String title);

    String getSummary();

    void setSummary(String summary);

    /*String getContent();

    void setContent(String content);*/

    String getHtmlContent();

    void setHtmlContent(String htmlContent);
    String getThumbnail();

    void setThumbnail(String thumbnail);
    String getAuthor();

    void setAuthor(String author);

    String getStatus();

    void setStatus(String status);

    String getCommentStatus();

    void setCommentStatus(String commentStatus);

    Integer getCommentCount();

    void setCommentCount(Integer commentCount);
    Date getCommentTime();

    void setCommentTime(Date commentTime);

    Integer getViewCount();

    void setViewCount(Integer viewCount);

    Date getModified() ;

    void setModified(Date modified);

    String getMetaKeywords();

    void setMetaKeywords(String metaKeywords);

    String getMetaDescription();

    void setMetaDescription(String metaDescription);

    String getRemarks();

    void setRemarks(String remarks);

    String getLinkTo();

    void setLinkTo(String linkTo);

    List<ArticleCategory> getArticleCategories();

    void setArticleCategories(List<ArticleCategory> articleCategories);

    List<ArticleComment> getArticleComments();

    void setArticleComments(List<ArticleComment> articleComments);

    List<ZaoJiCMSProduct> getRelatedProducts();

    void setRelatedProducts(List<ZaoJiCMSProduct> relatedProducts);

    Date getCreated();

    void setCreated(Date created);
}
