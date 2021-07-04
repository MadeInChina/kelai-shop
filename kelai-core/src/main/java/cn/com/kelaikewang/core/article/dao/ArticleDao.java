package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.Article;
import cn.com.kelaikewang.core.article.dto.ArticlePaginationQueryDTO;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface ArticleDao extends BaseDao<Article> {
    List<Article> articlePagination(ArticlePaginationQueryDTO paginationQueryDTO);
    Long count(ArticlePaginationQueryDTO paginationQueryDTO);
}
