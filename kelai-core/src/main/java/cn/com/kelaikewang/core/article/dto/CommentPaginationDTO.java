package cn.com.kelaikewang.core.article.dto;

import java.io.Serializable;
import java.util.List;

public class CommentPaginationDTO implements Serializable {
    private Integer pageIndex;
    private Integer pageCount;
    private Long recordCount;

    private List<TrunkCommentDTO> comments;

    public List<TrunkCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TrunkCommentDTO> comments) {
        this.comments = comments;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }
}
