package cn.com.kelaikewang.core.article.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface ArticleComment extends Serializable {
     Long getId();

     void setId(Long id);

     ArticleComment getParent();

     void setParent(ArticleComment parent);

     Article getArticle();

     void setArticle(Article article);

     Long getUserId();

     void setUserId(Long userId);

     String getContent();

     void setContent(String content);

     Integer getReplyCount();

     void setReplyCount(Integer replyCount);

     Integer getVoteUp();

     String getIp();
     void setIp(String ip);

     void setVoteUp(Integer voteUp);

     Integer getVoteDown();

     void setVoteDown(Integer voteDown);

     String getStatus();

     void setStatus(String status);

     Boolean isReport();
     void setReport(Boolean report);

     Date getReportDate();
     void setReportDate(Date reportDate);

     String getReportIp();
     void setReportIp(String reportIp);

     Long getReportByUserId();
     void setReportByUserId(Long reportByUserId);

     Date getCreated();

     void setCreated(Date created);
    List<ArticleComment> getChildren();

    void setChildren(List<ArticleComment> children);

     /**
      * 评论的楼层
      * @return
      */
    List<ArticleFloorComment> getFloors();
    void setFloors(List<ArticleFloorComment> floors);

     /**
      * 引用该评论的楼层
      * @return
      */
    List<ArticleFloorComment> getQuotes();
    void setQuotes(List<ArticleFloorComment> quotes);

}
