package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.ArticleComment;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface ArticleCommentDao extends BaseDao<ArticleComment> {
    List<ArticleComment> articleCommentPagination(long articleId, int pageSize, int pageIndex);
}
