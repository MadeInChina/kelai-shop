package cn.com.kelaikewang.core.article.service;

import cn.com.kelaikewang.core.article.dto.*;
import cn.com.kelaikewang.core.article.service.type.CommentItemStatusType;
import cn.com.kelaikewang.core.profile.dao.ZaoJiCMSCustomerDao;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomer;
import cn.com.kelaikewang.core.article.dao.ArticleCommentDao;
import cn.com.kelaikewang.core.article.dao.ArticleFloorCommentDao;
import cn.com.kelaikewang.core.article.dao.ArticleDao;
import cn.com.kelaikewang.core.article.domain.ArticleFloorCommentImpl;
import cn.com.kelaikewang.core.article.domain.ArticleComment;
import cn.com.kelaikewang.core.article.domain.ArticleCommentImpl;
import cn.com.kelaikewang.core.article.dto.*;
import cn.com.kelaikewang.infrastructure.dao.QueryConditions;
import org.broadleafcommerce.common.util.dao.TQOrder;
import org.broadleafcommerce.common.util.dao.TQRestriction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("zjcmsArticleCommentService")
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Resource(name = "zjcmsArticleCommentDao")
    private ArticleCommentDao articleCommentDao;
    @Resource(name = "zjcmsArticleCommentFloorDao")
    private ArticleFloorCommentDao floorCommentDao;
    @Resource(name = "zjcmsArticleDao")
    private ArticleDao articleDao;
    @Resource(name = "blCustomerDao")
    private ZaoJiCMSCustomerDao customerDao;

    @Transactional("blTransactionManager")
    @Override
    public void addComment(AddCommentDTO addCommentDTO) {
        ArticleComment articleComment = new ArticleCommentImpl();
        articleComment.setArticle(articleDao.getById(addCommentDTO.getArticleId()));
        articleComment.setContent(addCommentDTO.getComment());
        articleComment.setCreated(new Date());

        if (addCommentDTO.getParentCommentId() != null){
            ArticleComment parent = articleCommentDao.getById(addCommentDTO.getParentCommentId());
            articleComment.setParent(parent);

            parent.setReplyCount(parent.getReplyCount()+1);
            articleCommentDao.update(parent);
        }
        articleComment.setStatus(CommentItemStatusType.NORMAL.getType());
        articleComment.setUserId(addCommentDTO.getUserId());
        articleComment.setVoteDown(0);
        articleComment.setVoteUp(0);
        articleComment.setReplyCount(0);
        articleComment.setIp(addCommentDTO.getIp());

        //articleComment.setFloors(new ArrayList<>());

        articleCommentDao.create(articleComment);

        if (addCommentDTO.getFloorCommentIds() != null && addCommentDTO.getFloorCommentIds().size() > 0){
            int i = 1;
            for (Long commentId : addCommentDTO.getFloorCommentIds()){
                ArticleFloorCommentImpl floor = new ArticleFloorCommentImpl();
                floor.setTrunkComment(articleComment);
                floor.setFloorComment(articleCommentDao.getById(commentId));
                floor.setFloorNumber(i);
                floorCommentDao.create(floor);
                i++;
            }

        }

    }

    @Override
    public CommentPaginationDTO getCommentPagination(CommentPaginationQueryDTO queryDTO) {
        QueryConditions commentQueryConditions = new QueryConditions();
        commentQueryConditions.setRootAlias("c");
        commentQueryConditions.addRestriction(new TQRestriction("c.article.id","=",queryDTO.getArticleId()));
        commentQueryConditions.addOrder(new TQOrder("c.created",false));
        //主干评论
        List<ArticleComment> trunkComments = articleCommentDao.pagination(commentQueryConditions,
                queryDTO.getPageIndex(),
                queryDTO.getPageSize());

        List<Long> commentIds = trunkComments.stream()
                .map(ArticleComment::getId)
                .collect(Collectors.toList());
        //楼层记录
        List<Map> floorRecordMap = floorCommentDao.listCommentFloorsByCommentIds(commentIds);
        List<Long> userIds = floorRecordMap.stream().map(item->((BigInteger)item.get("USER_ID")).longValue()).collect(Collectors.toList());
        userIds.addAll(trunkComments.stream().map(ArticleComment::getUserId).collect(Collectors.toList()));

        List<ZaoJiCMSCustomer> customers = customerDao.listCustomerByIds(userIds);

        List<FloorCommentDTO> floorComments = new ArrayList<>();
        for (Map record : floorRecordMap){
            FloorCommentDTO commentDTO = new FloorCommentDTO();
            commentDTO.setComment((String) record.get("CONTENT"));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            commentDTO.setCommentDate(simpleDateFormat.format((Date)record.get("CREATED")));
            commentDTO.setCommentId(((BigInteger)record.get("ID")).longValue());
            commentDTO.setFloorId(((BigInteger)record.get("FLOOR_ID")).longValue());
            commentDTO.setFloorNumber((Integer)record.get("FLOOR_NUMBER"));
            commentDTO.setVoteDown((Integer)record.get("VOTE_DOWN"));
            commentDTO.setVoteUp((Integer)record.get("VOTE_UP"));
            commentDTO.setTrunkCommentId(((BigInteger)record.get("TRUNK_COMMENT_ID")).longValue());

            Optional<ZaoJiCMSCustomer> optCmsCustomer = customers.stream()
                    .filter(item->item.getId().equals(((BigInteger)record.get("USER_ID")).longValue()))
                    .findFirst();
            if (!optCmsCustomer.isPresent()){
                commentDTO.setNickname("");
                commentDTO.setProfilePhoto("");
                commentDTO.setUserId(null);
            }else {
                ZaoJiCMSCustomer cmsCustomer = optCmsCustomer.get();
                commentDTO.setNickname(cmsCustomer.getNickname());
                commentDTO.setProfilePhoto(cmsCustomer.getProfilePhoto());
                commentDTO.setUserId(((BigInteger)record.get("USER_ID")).longValue());
            }

            floorComments.add(commentDTO);
        }

        long recordCount = articleCommentDao.countByQueryConditions(commentQueryConditions);

        CommentPaginationDTO paginationDTO = new CommentPaginationDTO();
        paginationDTO.setPageIndex(queryDTO.getPageIndex());
        paginationDTO.setRecordCount(recordCount);
        paginationDTO.setPageCount((int)Math.ceil((float)recordCount/(float) queryDTO.getPageSize()));

        List<TrunkCommentDTO> list = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (ArticleComment comment : trunkComments){

            TrunkCommentDTO trunkCommentDTO = new TrunkCommentDTO();
            trunkCommentDTO.setUserId(comment.getUserId());
            trunkCommentDTO.setComment(comment.getContent());

            trunkCommentDTO.setCommentDate(simpleDateFormat.format(comment.getCreated()));
            trunkCommentDTO.setCommentId(comment.getId());
            trunkCommentDTO.setVoteDown(comment.getVoteDown());
            trunkCommentDTO.setVoteUp(comment.getVoteUp());

            Optional<ZaoJiCMSCustomer> optCmsCustomer = customers.stream()
                    .filter(item->item.getId().equals(comment.getUserId()))
                    .findFirst();

            if (!optCmsCustomer.isPresent()){
                trunkCommentDTO.setNickname("");
                trunkCommentDTO.setProfilePhoto("");
                trunkCommentDTO.setUserId(null);
            }else {
                ZaoJiCMSCustomer cmsCustomer = optCmsCustomer.get();
                trunkCommentDTO.setNickname(cmsCustomer.getNickname());
                trunkCommentDTO.setProfilePhoto(cmsCustomer.getProfilePhoto());
                trunkCommentDTO.setUserId(cmsCustomer.getId());
            }

            List<FloorCommentDTO> floors = floorComments.stream()
                    .filter(item->item.getTrunkCommentId().equals(comment.getId()))
                    .collect(Collectors.toList());

            if (floors.size() > 0) {
                Collections.sort(floors, (o1, o2) -> {
                    if (o1.getFloorNumber().intValue() == o2.getFloorNumber().intValue()) {
                        return 0;
                    }
                    if (o1.getFloorNumber() > o2.getFloorNumber()) {
                        return -1;
                    }
                    return 1;
                });
                int i =0;
                FloorCommentDTO current = null;
                while (i<floors.size()){
                    if (i == 0){
                        trunkCommentDTO.setFloorComment(floors.get(i));
                    }else {
                        current.setNextFloor(floors.get(i));
                    }
                    current = floors.get(i);
                    i++;
                }
            }
            list.add(trunkCommentDTO);
        }

        paginationDTO.setComments(list);

        return paginationDTO;
    }
    @Transactional("blTransactionManager")
    @Override
    public void report(ReportCommentDTO reportCommentDTO) {
        ArticleComment articleComment = articleCommentDao.getById(reportCommentDTO.getCommentId());
        articleComment.setReport(true);
        articleComment.setReportByUserId(reportCommentDTO.getReportByUserId());
        articleComment.setReportDate(new Date());
        articleComment.setReportIp(reportCommentDTO.getReportIp());
        articleCommentDao.update(articleComment);
    }
    @Transactional("blTransactionManager")
    @Override
    public void voteUp(long commentId) {
        ArticleComment articleComment = articleCommentDao.getById(commentId);
        articleComment.setVoteUp(articleComment.getVoteUp()+1);
        articleCommentDao.update(articleComment);
    }
    @Transactional("blTransactionManager")
    @Override
    public void voteDown(long commentId) {
        ArticleComment articleComment = articleCommentDao.getById(commentId);
        articleComment.setVoteDown(articleComment.getVoteDown()+1);
        articleCommentDao.update(articleComment);
    }
}
