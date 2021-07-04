package cn.com.kelaikewang.admin.controller.catalog;

import cn.com.kelaikewang.core.catalog.service.ZaoJiCMSProductService;
import org.broadleafcommerce.admin.web.controller.action.AdminCatalogActionsController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ZaoJiCMSAdminCatalogActionsController extends AdminCatalogActionsController {

    @Resource(name = "zjcmsProductService")
    protected ZaoJiCMSProductService nextShopProductService;

    public @ResponseBody
    Map<String, Object> generateSkus(HttpServletRequest request, HttpServletResponse response, Model model,
                                     @PathVariable(value = "productId") Long productId,
                                     @PathVariable(value = "skusFieldName") String skusFieldName) {
        Integer generatedColorOptionValueMediaCount = nextShopProductService.generateColorOptionValueMedia(productId);
        Map<String, Object> result = super.generateSkus(request,response,model,productId,skusFieldName);
        Integer generatedSkus = (Integer)result.get("skusGenerated");
        if (generatedColorOptionValueMediaCount != 0 || generatedSkus != 0) {
            result.put("skusGenerated", generatedColorOptionValueMediaCount > generatedSkus ? generatedColorOptionValueMediaCount : generatedSkus);
        }
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.indexOf("/additionalSkus")) + "/colorOptionValueMedia";
        result.put("colorOptionValueMediaUrl",url);
        return result;
    }
}
