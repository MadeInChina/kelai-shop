package cn.com.kelaikewang.core.article.dao;

import cn.com.kelaikewang.core.article.domain.ArticleFloorComment;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("zjcmsArticleCommentFloorDao")
public class ArticleFloorCommentDaoImpl extends BaseDaoImpl<ArticleFloorComment> implements ArticleFloorCommentDao {

    @Override
    public List<Map> listCommentFloorsByCommentIds(List<Long> commentIds) {
        if (commentIds == null || commentIds.size() == 0){
            return new ArrayList<>();
        }
        List<Map> articleCommentFloors = em.createNativeQuery("SELECT\n" +
                "\tZJCMS_ARTICLE_COMMENT.ID,\n" +
                "\tZJCMS_ARTICLE_COMMENT.CONTENT,\n" +
                "\tZJCMS_ARTICLE_COMMENT.CREATED,\n" +
                "\tZJCMS_ARTICLE_COMMENT.REPLY_COUNT,\n" +
                "\tZJCMS_ARTICLE_COMMENT.`STATUS`,\n" +
                "\tZJCMS_ARTICLE_COMMENT.USER_ID,\n" +
                "\tZJCMS_ARTICLE_COMMENT.VOTE_DOWN,\n" +
                "\tZJCMS_ARTICLE_COMMENT.VOTE_UP,\n" +
                "\tZJCMS_ARTICLE_COMMENT.ARTICLE_ID,\n" +
                "\tZJCMS_ARTICLE_COMMENT.PARENT_ID,\n" +
                "\tZJCMS_ARTICLE_COMMENT.IP,\n" +
                "\tZJCMS_ARTICLE_FLOOR_COMMENT.FLOOR_NUMBER,\n" +
                "\tZJCMS_ARTICLE_FLOOR_COMMENT.ID AS FLOOR_ID,\n" +
                "\tZJCMS_ARTICLE_FLOOR_COMMENT.TRUNK_COMMENT_ID\n" +
                "FROM\n" +
                "\tZJCMS_ARTICLE_COMMENT\n" +
                "\tINNER JOIN ZJCMS_ARTICLE_FLOOR_COMMENT ON ZJCMS_ARTICLE_FLOOR_COMMENT.FLOOR_COMMENT_ID = ZJCMS_ARTICLE_COMMENT.ID \n" +
                "WHERE\n" +
                "\tZJCMS_ARTICLE_FLOOR_COMMENT.TRUNK_COMMENT_ID IN (:commentIds) ")
                .unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setParameter("commentIds",commentIds)
                .list();
        return articleCommentFloors;
    }

    @Override
    public Class<ArticleFloorComment> getModelClass() {
        return ArticleFloorComment.class;
    }
}
