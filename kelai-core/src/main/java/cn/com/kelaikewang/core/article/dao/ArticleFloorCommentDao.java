package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.ArticleFloorComment;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface ArticleFloorCommentDao extends BaseDao<ArticleFloorComment> {
    List<Map> listCommentFloorsByCommentIds(List<Long> commentIds);
}
