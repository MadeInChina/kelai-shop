package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.ArticleComment;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsArticleCommentDao")
public class ArticleCommentDaoImpl extends BaseDaoImpl<ArticleComment> implements ArticleCommentDao {

    @Override
    public Class<ArticleComment> getModelClass() {
        return ArticleComment.class;
    }

    @Override
    public List<ArticleComment> articleCommentPagination(long articleId, int pageSize, int pageIndex) {
        List<ArticleComment> articleComments = em.createNamedQuery("BC_ARTICLE_COMMENT_PAGINATION", ArticleComment.class)
                .setParameter("articleId",articleId)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex -1) * pageSize)
                .getResultList();
        return articleComments;
    }

}
