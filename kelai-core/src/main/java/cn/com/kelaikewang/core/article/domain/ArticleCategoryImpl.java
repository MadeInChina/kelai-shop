package cn.com.kelaikewang.core.article.domain;

import org.broadleafcommerce.common.presentation.*;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "ZJCMS_ARTICLE_CATEGORY")
@Entity
@AdminPresentationClass(friendlyName = "ArticleCategoryImpl")
public class ArticleCategoryImpl implements ArticleCategory{
    @Id
    @Column(name = "ARTICLE_CATEGORY_ID")
    @GeneratedValue(generator = "ArticleCategoryId")
    @GenericGenerator(
            name="ArticleCategoryId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ArticleCategoryImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ArticleCategoryImpl")
            }
    )
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL,friendlyName = "ArticleCategoryImpl_Id")
    protected Long id;

    @ManyToOne(targetEntity = ArticleCategoryImpl.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @AdminPresentation(prominent = true,order = 1,gridOrder = 1,friendlyName = "ArticleCategoryImpl_Parent")
    @AdminPresentationToOneLookup(lookupDisplayProperty = "name")
    protected ArticleCategory parent;

    @Column(name = "NAME",nullable = false)
    @AdminPresentation(prominent = true,order = 2,gridOrder = 2,friendlyName = "ArticleCategoryImpl_Name")
    protected String name;

    @Column(name = "IDENTIFY",unique = true,nullable = false)
    @AdminPresentation(prominent = true,order = 3,gridOrder = 3,friendlyName = "ArticleCategoryImpl_Identify")
    protected String identify;

    @Column(name = "DESCRIPTION",length = 1000)
    @AdminPresentation(prominent = true,order = 4,gridOrder = 4,friendlyName = "ArticleCategoryImpl_Description")
    protected String description;

    @Column(name = "CSS_CLASS")
    @AdminPresentation(prominent = true,order = 5,gridOrder = 5,friendlyName = "ArticleCategoryImpl_CssClass")
    protected String cssClass;

    @Column(name = "COUNT")
    @AdminPresentation(prominent = true,visibility = VisibilityEnum.FORM_HIDDEN,order = 5,gridOrder = 5,friendlyName = "ArticleCategoryImpl_Count")
    protected Integer count;

    @Column(name = "ORDER_NUMBER",nullable = false)
    @AdminPresentation(prominent = true,order = 6,gridOrder = 6,friendlyName = "ArticleCategoryImpl_OrderNumber")
    protected Integer orderNumber;

    @Column(name = "META_KEYWORDS",nullable = false)
    @AdminPresentation(prominent = true,order = 7,gridOrder = 7,friendlyName = "ArticleCategoryImpl_MetaKeywords")
    protected String metaKeywords;

    @Column(name = "META_DESCRIPTION",nullable = false)
    @AdminPresentation(prominent = true,order = 8,gridOrder = 8,friendlyName = "ArticleCategoryImpl_MetaDescription")
    protected String metaDescription;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent",targetEntity = ArticleCategoryImpl.class)
    @AdminPresentation(excluded = true)
    protected List<ArticleCategory> children;

    @ManyToMany(targetEntity = ArticleImpl.class,fetch = FetchType.LAZY)
    @JoinTable(name = "ZJCMS_ARTICLE_CATEGORY_MAPPING",
            joinColumns ={
                    @JoinColumn(name =  "ARTICLE_CATEGORY_ID",referencedColumnName = "ARTICLE_CATEGORY_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ARTICLE_ID",referencedColumnName = "ARTICLE_ID")
            })
    @AdminPresentation(excluded = true)
    protected List<Article> articles;

    @Column(name = "CREATED")
    protected Date created;

    @Column(name = "MODIFIED")
    protected Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArticleCategory getParent() {
        return parent;
    }

    public void setParent(ArticleCategory parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public List<ArticleCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleCategory> children) {
        this.children = children;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }

    @Override
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
