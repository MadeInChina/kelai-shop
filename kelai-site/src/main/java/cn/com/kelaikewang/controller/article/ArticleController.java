package cn.com.kelaikewang.controller.article;

import cn.com.kelaikewang.core.article.domain.Article;
import cn.com.kelaikewang.core.article.service.ArticleService;
import cn.com.kelaikewang.core.article.service.type.ArticleStatusType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller("zjcmsArticleController")
public class ArticleController {
    @Resource(name = "zjcmsArticleService")
    private ArticleService articleService;


    @RequestMapping(value = "article/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id")long id, Model model, HttpServletResponse response){
        Article article = articleService.getArticleById(id);
        if (article == null || !ArticleStatusType.NORMAL.getType().equals(article.getStatus())){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            //model.addAttribute("unhandled_exception_message","请求的资源未找到");
            model.addAttribute("exceptionUUID", UUID.randomUUID().toString());
            return "utility/error";
        }
        articleService.updateViewCount(id);
        model.addAttribute("article",article);
        model.addAttribute("articleCategories",article.getArticleCategories());
        model.addAttribute("relatedProducts",article.getRelatedProducts());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ("anonymousUser".equals(authentication.getPrincipal())){
            model.addAttribute("isLoggedIn",false);
        }else {
            model.addAttribute("isLoggedIn",true);
        }
        return "article/article";
    }
}
