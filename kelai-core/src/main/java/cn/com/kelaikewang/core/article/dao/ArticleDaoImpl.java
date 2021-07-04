package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.Article;
import cn.com.kelaikewang.core.article.dto.ArticlePaginationQueryDTO;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.broadleafcommerce.common.util.dao.TQJoin;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsArticleDao")
public class ArticleDaoImpl extends BaseDaoImpl<Article> implements ArticleDao {

    @Override
    public Class<Article> getModelClass() {
        return Article.class;
    }

    @Override
    public List<Article> articlePagination(ArticlePaginationQueryDTO paginationQueryDTO) {
        List<Article> articles = em.createNamedQuery("BC_ARTICLE_PAGINATION",Article.class)
                .setParameter("categoryId",paginationQueryDTO.getCategoryId())
                .setMaxResults(paginationQueryDTO.getPageSize())
                .setFirstResult(paginationQueryDTO.getOffset())
                .getResultList();
        return articles;
    }

    @Override
    public Long count(ArticlePaginationQueryDTO paginationQueryDTO) {
        Long count = new TypedQueryBuilder<>(Article.class, "a")
                .addJoin(new TQJoin("a.articleCategories","c"))
                .addRestriction("c.id","=",paginationQueryDTO.getCategoryId()).toCountQuery(em).getSingleResult();
        return count;
    }
}
