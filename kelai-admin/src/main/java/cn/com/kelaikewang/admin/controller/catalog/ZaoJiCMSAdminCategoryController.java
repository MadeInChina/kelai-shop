package cn.com.kelaikewang.admin.controller.catalog;

import org.broadleafcommerce.admin.web.controller.entity.AdminCategoryController;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ZaoJiCMSAdminCategoryController extends AdminCategoryController {
    @Override
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model,
                                 @PathVariable Map<String, String> pathVars,
                                 @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
         super.viewEntityList(request, response, model, pathVars, requestParams);
        model.addAttribute("viewType", "category/categoryTreeList");
        model.addAttribute("customScriptView","views/category/customScript/categoryTree");
         return "modules/categoryTreeListContainer";
    }
}
