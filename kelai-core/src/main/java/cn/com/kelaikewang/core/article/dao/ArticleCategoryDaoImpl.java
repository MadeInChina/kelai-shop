package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.ArticleCategory;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("zjcmsArticleCategoryDao")
public class ArticleCategoryDaoImpl extends BaseDaoImpl<ArticleCategory> implements ArticleCategoryDao {

    @Override
    public Class<ArticleCategory> getModelClass() {
        return ArticleCategory.class;
    }
}
