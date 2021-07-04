package cn.com.kelaikewang.controller;

import cn.com.kelaikewang.core.marketing.service.RecommendedProductService;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.service.MenuService;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("zjcmsHomeController")
public class HomeController {
    @Resource(name = "zjcmsRecommendedProductService")
    private RecommendedProductService recommendedProductService;
    @Resource(name = "zjcmsMenuService")
    private MenuService menuService;

    @RequestMapping("/")
    public String viewHome(HttpServletRequest request, HttpServletResponse response, Model model){
        return "layout/homepage";
        //return "layout/homepage";
    }
    @RequestMapping(value = "store",method = RequestMethod.GET)
    public String viewStore(HttpServletRequest request, HttpServletResponse response, Model model, SitePreference sitePreference){
        model.addAttribute("products",recommendedProductService.readAllRecommendedProducts());
        if (sitePreference.isMobile()){
            Menu menu = menuService.findMenuByName("MHomeMenu");
            //不要设置为categoryMenuItems及menuItems，会跟navMenu和categoryService冲突
            model.addAttribute("mHomeMenuItems",menu.getMenuItems());
        }
        return "layout/store";
    }
}
