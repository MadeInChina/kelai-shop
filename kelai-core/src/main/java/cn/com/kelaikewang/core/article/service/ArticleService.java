package cn.com.kelaikewang.core.article.service;

import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.article.domain.Article;
import cn.com.kelaikewang.core.article.dto.ArticlePaginationQueryDTO;

public interface ArticleService {
    PageOfItems<Article> articlePagination(ArticlePaginationQueryDTO paginationQueryDTO);
    Article getArticleById(long id);
    void updateViewCount(long articleId);
}
