package cn.com.kelaikewang.core.article.dto;

import java.io.Serializable;
import java.util.List;

public class AddCommentDTO implements Serializable {
    private Long articleId;
    private Long parentCommentId;
    private String comment;
    private String ip;
    private List<Long> floorCommentIds;
    private Long userId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public List<Long> getFloorCommentIds() {
        return floorCommentIds;
    }

    public void setFloorCommentIds(List<Long> floorCommentIds) {
        this.floorCommentIds = floorCommentIds;
    }
}
