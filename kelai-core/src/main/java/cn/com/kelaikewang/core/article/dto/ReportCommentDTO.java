package cn.com.kelaikewang.core.article.dto;

import java.io.Serializable;

public class ReportCommentDTO implements Serializable {
    private Long commentId;
    private String reportIp;
    private Long reportByUserId;
    private String type;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getReportIp() {
        return reportIp;
    }

    public void setReportIp(String reportIp) {
        this.reportIp = reportIp;
    }

    public Long getReportByUserId() {
        return reportByUserId;
    }

    public void setReportByUserId(Long reportByUserId) {
        this.reportByUserId = reportByUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
