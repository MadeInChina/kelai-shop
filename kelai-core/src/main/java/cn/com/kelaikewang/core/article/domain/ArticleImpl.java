package cn.com.kelaikewang.core.article.domain;

import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProduct;
import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProductImpl;
import cn.com.kelaikewang.core.search.domain.ZaoJiCMSFieldEntity;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationCollection;
import org.broadleafcommerce.common.presentation.AdminPresentationOperationTypes;
import org.broadleafcommerce.common.presentation.client.AddMethodType;
import org.broadleafcommerce.common.presentation.client.OperationType;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.core.search.domain.FieldEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "ZJCMS_ARTICLE")
@Entity
@AdminPresentationClass(friendlyName = "ArticleImpl")
public class ArticleImpl implements Article , Serializable {
    @Id
    @Column(name = "ARTICLE_ID")
    @GeneratedValue(generator = "ArticleId")
    @GenericGenerator(
            name="ArticleId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ArticleImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ArticleImpl")
            }
    )
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL,friendlyName = "ArticleImpl_Id")
    protected Long id;

    @Column(name = "TITLE",nullable = false)
    @AdminPresentation(prominent = true,order = 1,gridOrder = 1,friendlyName = "ArticleImpl_Title")
    protected String title;

    @Column(name = "SUMMARY",length = 1000,nullable = false)
    @AdminPresentation(prominent = true,order = 2,gridOrder = 2,friendlyName = "ArticleImpl_Summary",fieldType = SupportedFieldType.STRING)
    protected String summary;

  /*  @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT",nullable = false,columnDefinition="longtext",length = 16777215)
    @Lob
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL,
            order = 3,
            gridOrder = 3,
            friendlyName = "ArticleImpl_Content")
    protected String content;*/

    @Column(name = "HTML_CONTENT",nullable = false,columnDefinition="longtext",length = 16777215)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @AdminPresentation(visibility = VisibilityEnum.GRID_HIDDEN,
            order = 3,
            gridOrder = 3,
            fieldType = SupportedFieldType.HTML_BASIC,
            friendlyName = "ArticleImpl_Content")
    protected String htmlContent;

    @Column(name = "AUTHOR",nullable = false)
    @AdminPresentation(prominent = true,order = 4,gridOrder = 4,friendlyName = "ArticleImpl_Author")
    protected String author;

    @Column(name = "STATUS",nullable = false)
    @AdminPresentation(prominent = true,
            order = 5,
            gridOrder = 5,
            friendlyName = "ArticleImpl_Status",
            broadleafEnumeration="cn.com.kelaikewang.core.article.service.type.ArticleStatusType",
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION)
    protected String status;

    @Column(name = "COMMENT_STATUS",nullable = false)
    @AdminPresentation(prominent = true,order = 6,gridOrder = 6,friendlyName = "ArticleImpl_CommentStatus",
            broadleafEnumeration="cn.com.kelaikewang.core.article.service.type.ArticleCommentStatusType",
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION
    )
    protected String commentStatus;

    @Column(name = "COMMENT_COUNT")
    @AdminPresentation(visibility = VisibilityEnum.FORM_HIDDEN,order = 7,gridOrder = 7,friendlyName = "ArticleImpl_CommentCount")
    protected Integer commentCount;

    @Column(name = "COMMENT_TIME")
    @AdminPresentation(visibility = VisibilityEnum.FORM_HIDDEN,order = 8,gridOrder = 8,friendlyName = "ArticleImpl_CommentTime")
    protected Date commentTime;

    @Column(name = "VIEW_COUNT")
    @AdminPresentation(visibility = VisibilityEnum.FORM_HIDDEN,order = 9,gridOrder = 9,friendlyName = "ArticleImpl_ViewCount")
    protected Integer viewCount;

    @Column(name = "MODIFIED")
    @AdminPresentation(visibility = VisibilityEnum.FORM_HIDDEN,order = 10,gridOrder = 10,friendlyName = "ArticleImpl_Modified")
    protected Date modified = new Date();

    @Column(name = "META_KEYWORDS",nullable = false)
    @AdminPresentation(prominent = true,order = 11,gridOrder = 11,friendlyName = "ArticleImpl_MetaKeywords")
    protected String metaKeywords;

    @Column(name = "MATE_DESCRIPTION",nullable = false)
    @AdminPresentation(prominent = true,order = 12,gridOrder =12,friendlyName = "ArticleImpl_MetaDescription")
    protected String metaDescription;

    @Column(name = "REMARKS")
    @AdminPresentation(visibility = VisibilityEnum.GRID_HIDDEN,order = 13,gridOrder = 13,friendlyName = "ArticleImpl_Remarks")
    protected String remarks;

    @Column(name = "LINK_TO")
    @AdminPresentation(visibility = VisibilityEnum.GRID_HIDDEN,order = 14,gridOrder = 14,friendlyName = "ArticleImpl_LinkTo")
    protected String linkTo;

    @Column(name = "THUMBNAIL")
    @AdminPresentation(friendlyName = "ArticleImpl_Thumbnail",
            order =15,
            visibility = VisibilityEnum.GRID_HIDDEN,
            fieldType = SupportedFieldType.ASSET_LOOKUP)
    protected String thumbnail;

    @ManyToMany(fetch = FetchType.LAZY,targetEntity = ArticleCategoryImpl.class)
    @JoinTable(name = "ZJCMS_ARTICLE_CATEGORY_MAPPING",
            joinColumns ={
                    @JoinColumn(name =  "ARTICLE_ID",referencedColumnName = "ARTICLE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ARTICLE_CATEGORY_ID",referencedColumnName = "ARTICLE_CATEGORY_ID")
            })
    @AdminPresentationCollection(friendlyName = "ArticleImpl_Categories",
            addType = AddMethodType.LOOKUP, order = 6000,
            manyToField = "articles",
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationType.NONDESTRUCTIVEREMOVE)
    )
    protected List<ArticleCategory> articleCategories;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "article",targetEntity = ArticleCommentImpl.class)
    @AdminPresentation(excluded = true)
    protected List<ArticleComment> articleComments;

    @ManyToMany(fetch = FetchType.LAZY,targetEntity = ZaoJiCMSProductImpl.class)
    @JoinTable(name = "ZJCMS_ARTICLE_PRODUCT_MAPPING",
            joinColumns ={
                    @JoinColumn(name =  "ARTICLE_ID",referencedColumnName = "ARTICLE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "PRODUCT_ID",referencedColumnName = "PRODUCT_ID")
            })
    @AdminPresentationCollection(addType = AddMethodType.LOOKUP, friendlyName = "ArticleImpl_Products",
            order = 1000,
            manyToField = "articles",
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationType.NONDESTRUCTIVEREMOVE))
    protected List<ZaoJiCMSProduct> relatedProducts;

    @Column(name = "CREATED")
    @AdminPresentation(visibility = VisibilityEnum.FORM_HIDDEN,order = 2000,gridOrder = 2000,friendlyName = "ArticleImpl_Created")
    protected Date created = new Date();

    public Long getId() {
        return id;
    }

    @Override
    public FieldEntity getFieldEntityType() {
        return ZaoJiCMSFieldEntity.ARTICLE;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLinkTo() {
        return linkTo;
    }

    public void setLinkTo(String linkTo) {
        this.linkTo = linkTo;
    }

    public List<ArticleCategory> getArticleCategories() {
        return articleCategories;
    }

    public void setArticleCategories(List<ArticleCategory> articleCategories) {
        this.articleCategories = articleCategories;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ArticleComment> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(List<ArticleComment> articleComments) {
        this.articleComments = articleComments;
    }

    public List<ZaoJiCMSProduct> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<ZaoJiCMSProduct> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
}
