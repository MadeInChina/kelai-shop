package cn.com.kelaikewang.core.article.service;

import cn.com.kelaikewang.core.article.dao.ArticleCategoryDao;
import cn.com.kelaikewang.core.article.domain.ArticleCategory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("zjcmsArticleCategoryService")
public class ArticleCategoryServiceImpl implements ArticleCategoryService{
    @Resource(name = "zjcmsArticleCategoryDao")
    private ArticleCategoryDao articleCategoryDao;
    @Override
    public ArticleCategory getById(long id) {
        return articleCategoryDao.getById(id);
    }
}
