package cn.com.kelaikewang.admin.controller.catalog;

import cn.com.kelaikewang.admin.service.ProductFilterService;
import cn.com.kelaikewang.core.catalog.dto.CategoryDTO;
import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.core.catalog.dto.ProductDTO;
import cn.com.kelaikewang.core.catalog.dto.ProductFilterDTO;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminProductBasicOperationsController {

   @Resource(name = "zjcmsProductFilterService")
   private ProductFilterService productFilterService;

   @Resource(name = "blCatalogService")
   private CatalogService catalogService;

    @RequestMapping(value = "productFilter",method = RequestMethod.GET)
    @ResponseBody
    public PageOfItems<ProductDTO> productFilter(ProductFilterDTO productFilterDTO){
        productFilterDTO.setPageSize(5);
       return productFilterService.filter(productFilterDTO);
    }
    @RequestMapping(value = "productCategory",method = RequestMethod.GET)
    @ResponseBody
    public List<CategoryDTO> productCategory(){
        List<Category> categories = catalogService.findAllCategories();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categories){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            Category parent = category.getParentCategory();
            categoryDTO.setpId(parent == null ? 0 : parent.getId());
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }
}
