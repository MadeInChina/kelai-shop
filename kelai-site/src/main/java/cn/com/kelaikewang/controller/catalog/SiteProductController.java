package cn.com.kelaikewang.controller.catalog;

import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SiteProductController extends ProductController {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.handleRequest(request, response);
        Product product = (Product)request.getAttribute("currentProduct");
        if(product != null) {
            Category category = product.getCategory();
            if (category != null) {
                List<Product> categoryProducts = catalogService.findActiveProductsByCategory(category);
                modelAndView.addObject("categoryProducts",categoryProducts);
            }
        }
        return modelAndView;
    }
}
