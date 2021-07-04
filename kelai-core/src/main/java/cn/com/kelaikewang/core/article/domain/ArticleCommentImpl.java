package cn.com.kelaikewang.core.article.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "ZJCMS_ARTICLE_COMMENT")
@Entity
@AdminPresentationClass(friendlyName = "ArticleCommentImpl")
public class ArticleCommentImpl implements ArticleComment {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ArticleCommentId")
    @GenericGenerator(
            name="ArticleCommentId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ArticleCommentImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ArticleCommentImpl")
            }
    )
    protected Long id;

    @ManyToOne(targetEntity = ArticleCommentImpl.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    protected ArticleComment parent;

    @ManyToOne(targetEntity = ArticleImpl.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    @AdminPresentation(prominent = true,order = 1,gridOrder = 1,friendlyName = "ArticleCommentImpl_Article")
    protected Article article;

    @Column(name = "USER_ID")
    protected Long userId;

    @AdminPresentation(prominent = true,order = 2,gridOrder = 2,friendlyName = "ArticleCommentImpl_Content")
    @Column(name = "CONTENT",length = 1000,nullable = false)
    protected String content;

    @Column(name = "REPLY_COUNT")
    @AdminPresentation(prominent = true,order = 3,gridOrder = 3,friendlyName = "ArticleCommentImpl_ReplyCount")
    protected Integer replyCount;

    @Column(name = "VOTE_UP")
    @AdminPresentation(prominent = true,order = 4,gridOrder = 4,friendlyName = "ArticleCommentImpl_VoteUp")
    protected Integer voteUp;

    @Column(name = "VOTE_DOWN")
    @AdminPresentation(prominent = true,order = 5,gridOrder = 5,friendlyName = "ArticleCommentImpl_VoteDown")
    protected Integer voteDown;

    @Column(name = "STATUS",nullable = false)
    @AdminPresentation(prominent = true,order = 6,gridOrder = 6,friendlyName = "ArticleCommentImpl_Status")
    protected String status;

    @Column(name = "CREATED")
    @AdminPresentation(prominent = true,order = 7,gridOrder = 7,friendlyName = "ArticleCommentImpl_Created")
    protected Date created;

    @Column(name = "IP")
    protected String ip;

    @Column(name = "IS_REPORT")
    protected Boolean isReport;
    @Column(name = "REPORT_IP")
    protected String reportIp;
    @Column(name = "REPORT_DATE")
    protected Date reportDate;
    @Column(name = "REPORT_BY_USER_ID")
    protected Long reportByUserId;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent",targetEntity = ArticleCommentImpl.class)
    protected List<ArticleComment> children;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "trunkComment",targetEntity = ArticleFloorCommentImpl.class)
    protected List<ArticleFloorComment> floors;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "floorComment",targetEntity = ArticleFloorCommentImpl.class)
    protected List<ArticleFloorComment> quotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArticleComment getParent() {
        return parent;
    }

    public void setParent(ArticleComment parent) {
        this.parent = parent;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getVoteUp() {
        return voteUp;
    }

    public void setVoteUp(Integer voteUp) {
        this.voteUp = voteUp;
    }

    public Integer getVoteDown() {
        return voteDown;
    }

    public void setVoteDown(Integer voteDown) {
        this.voteDown = voteDown;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ArticleComment> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleComment> children) {
        this.children = children;
    }

    @Override
    public List<ArticleFloorComment> getFloors() {
        return floors;
    }

    @Override
    public void setFloors(List<ArticleFloorComment> floors) {
        this.floors = floors;
    }

    @Override
    public List<ArticleFloorComment> getQuotes() {
        return quotes;
    }

    @Override
    public void setQuotes(List<ArticleFloorComment> quotes) {
        this.quotes = quotes;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Boolean isReport() {
        return isReport;
    }

    @Override
    public void setReport(Boolean report) {
        isReport = report;
    }

    @Override
    public String getReportIp() {
        return reportIp;
    }

    @Override
    public void setReportIp(String reportIp) {
        this.reportIp = reportIp;
    }

    @Override
    public Date getReportDate() {
        return reportDate;
    }

    @Override
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    public Long getReportByUserId() {
        return reportByUserId;
    }

    @Override
    public void setReportByUserId(Long reportByUserId) {
        this.reportByUserId = reportByUserId;
    }
}
