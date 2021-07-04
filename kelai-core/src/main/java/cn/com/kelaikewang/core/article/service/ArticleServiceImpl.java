package cn.com.kelaikewang.core.article.service;

import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.article.dao.ArticleDao;
import cn.com.kelaikewang.core.article.domain.Article;
import cn.com.kelaikewang.core.article.dto.ArticlePaginationQueryDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsArticleService")
public class ArticleServiceImpl implements ArticleService {
    @Resource(name = "zjcmsArticleDao")
    private ArticleDao articleDao;

    @Override
    public PageOfItems<Article> articlePagination(ArticlePaginationQueryDTO paginationQueryDTO) {
        List<Article> articles = articleDao.articlePagination(paginationQueryDTO);
        long recordCount = articleDao.count(paginationQueryDTO);
        PageOfItems<Article> pageOfItems = new PageOfItems<>();
        pageOfItems.setItems(articles);
        pageOfItems.setPageIndex(paginationQueryDTO.getPageIndex());
        pageOfItems.setPageSize(paginationQueryDTO.getPageSize());
        pageOfItems.setRecordCount(recordCount);
        return pageOfItems;
    }

    @Override
    public Article getArticleById(long id) {
        return articleDao.getById(id);
    }
    @Transactional("blTransactionManager")
    @Override
    public void updateViewCount(long articleId) {
        Article article = getArticleById(articleId);
        if (article != null){
            if(article.getViewCount() == null){
                article.setViewCount(1);
            }else {
                article.setViewCount(article.getViewCount() + 1);
            }
            articleDao.update(article);
        }
    }
}
