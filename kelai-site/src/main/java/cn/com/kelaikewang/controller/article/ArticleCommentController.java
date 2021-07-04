package cn.com.kelaikewang.controller.article;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.commons.web.RequestUtils;
import cn.com.kelaikewang.core.article.dto.AddCommentDTO;
import cn.com.kelaikewang.core.article.dto.CommentPaginationDTO;
import cn.com.kelaikewang.core.article.dto.CommentPaginationQueryDTO;
import cn.com.kelaikewang.core.article.dto.ReportCommentDTO;
import cn.com.kelaikewang.core.article.service.ArticleCommentService;
import cn.com.kelaikewang.core.article.service.type.ReportCommentType;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.profile.core.service.CustomerUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("article")
public class ArticleCommentController {
    @Resource(name = "zjcmsArticleCommentService")
    private ArticleCommentService articleCommentService;

    @RequestMapping(value = "{articleId}/comment/page",method = RequestMethod.GET)
    @ResponseBody
    public CommentPaginationDTO commentPagination(CommentPaginationQueryDTO queryDTO){
        return articleCommentService.getCommentPagination(queryDTO);
    }
    @RequestMapping(value = "{articleId}/comment",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO addComment(@RequestBody AddCommentDTO addCommentDTO, HttpServletRequest request){
        if (addCommentDTO.getArticleId() == null){
            return ResponseDTO.fail("参数错误，提交评论失败");
        }
        if (StringUtils.isEmpty(addCommentDTO.getComment())){
            return ResponseDTO.fail("请填写评论内容");
        }
        addCommentDTO.setIp(RequestUtils.getIpAddr(request));
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof CustomerUserDetails){
            CustomerUserDetails customerUserDetails = (CustomerUserDetails)user;
            addCommentDTO.setUserId(customerUserDetails.getId());
        }
        if (addCommentDTO.getUserId() == null){
            return ResponseDTO.fail("请先登录");
        }
        articleCommentService.addComment(addCommentDTO);
        return ResponseDTO.success("提交评论成功");
    }
    @RequestMapping(value = "{articleId}/comment/{commentId}/voteUp",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO voteUp(@PathVariable("articleId")long articleId,@PathVariable("commentId")long commentId){
        articleCommentService.voteUp(commentId);
        return ResponseDTO.success("操作成功");
    }
    @RequestMapping(value = "{articleId}/comment/{commentId}/voteDown",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO voteDown(@PathVariable("articleId")long articleId,@PathVariable("commentId")long commentId){
        articleCommentService.voteDown(commentId);
        return ResponseDTO.success("操作成功");
    }
    @RequestMapping(value = "{articleId}/comment/{commentId}/report",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO report(@PathVariable("articleId")long articleId,
                              @RequestBody ReportCommentDTO reportCommentDTO,
                              HttpServletRequest request){
        if (reportCommentDTO.getCommentId() == null ||
                StringUtils.isEmpty(reportCommentDTO.getType()) ||
                ReportCommentType.getInstance(reportCommentDTO.getType()) == null){
            return ResponseDTO.fail("参数错误，提交失败");
        }
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof CustomerUserDetails){
            CustomerUserDetails customerUserDetails = (CustomerUserDetails)user;
            reportCommentDTO.setReportByUserId(customerUserDetails.getId());
        }
        reportCommentDTO.setReportIp(RequestUtils.getIpAddr(request));
        articleCommentService.report(reportCommentDTO);
        return ResponseDTO.success("操作成功");
    }
}
