package cn.com.kelaikewang.controller.article;

import cn.com.kelaikewang.core.article.dto.ArticlePaginationQueryDTO;
import cn.com.kelaikewang.core.article.service.ArticleCategoryService;
import cn.com.kelaikewang.core.article.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller("zjcmsArticleCategoryController")
public class ArticleCategoryController {
    @Resource(name = "zjcmsArticleService")
    private ArticleService articleService;
    @Resource(name = "zjcmsArticleCategoryService")
    private ArticleCategoryService articleCategoryService;

    @RequestMapping(value = "article/category/{categoryId}")
    public String articleList(ArticlePaginationQueryDTO articlePaginationQueryDTO, Model model){
        model.addAttribute("page",articleService.articlePagination(articlePaginationQueryDTO));
        model.addAttribute("articleCategory",articleCategoryService.getById(articlePaginationQueryDTO.getCategoryId()));
        return "article/articleList";
    }
}
