package cn.com.kelaikewang.core.article.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "ZJCMS_ARTICLE_FLOOR_COMMENT")
@Entity
public class ArticleFloorCommentImpl implements ArticleFloorComment {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ArticleCommentFloorId")
    @GenericGenerator(
            name="ArticleCommentFloorId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ArticleCommentFloorImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ArticleCommentFloorImpl")
            }
    )
    private Long id;
    /**
     * 楼层评论内容
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ArticleCommentImpl.class)
    @JoinColumn(name = "FLOOR_COMMENT_ID")
    private ArticleComment floorComment;
    /**
     * 楼层
     */
    @Column(name = "FLOOR_NUMBER")
    private Integer floorNumber;
    /**
     *当前用户发表的评论id（主干评论）
     */
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ArticleCommentImpl.class)
    @JoinColumn(name = "TRUNK_COMMENT_ID")
    private ArticleComment trunkComment;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public ArticleComment getFloorComment() {
        return floorComment;
    }

    @Override
    public void setFloorComment(ArticleComment floorComment) {
        this.floorComment = floorComment;
    }

    @Override
    public Integer getFloorNumber() {
        return floorNumber;
    }

    @Override
    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public ArticleComment getTrunkComment() {
        return trunkComment;
    }

    @Override
    public void setTrunkComment(ArticleComment trunkComment) {
        this.trunkComment = trunkComment;
    }
}
