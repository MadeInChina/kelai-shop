package cn.com.kelaikewang.core.article.service;

import cn.com.kelaikewang.core.article.dto.AddCommentDTO;
import cn.com.kelaikewang.core.article.dto.CommentPaginationDTO;
import cn.com.kelaikewang.core.article.dto.CommentPaginationQueryDTO;
import cn.com.kelaikewang.core.article.dto.ReportCommentDTO;

public interface ArticleCommentService {
    void addComment(AddCommentDTO addCommentDTO);
    CommentPaginationDTO getCommentPagination(CommentPaginationQueryDTO commentPaginationQueryDTO);
    void report(ReportCommentDTO reportCommentDTO);
    void voteUp(long commentId);
    void voteDown(long commentId);
}
