package cn.com.kelaikewang.admin.controller.logistics;

import com.alibaba.fastjson.JSON;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleItem;
import cn.com.kelaikewang.core.logistics.dto.BigRegionDTO;
import cn.com.kelaikewang.core.logistics.service.ShippingTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LogisticsController {

    @Resource(name = "zjcmsShippingTemplateService")
    private ShippingTemplateService shippingTemplateService;

    @GetMapping("shippingPricingRuleRegions")
    @ResponseBody
    public List<List<BigRegionDTO>> regions(HttpServletRequest request, HttpServletResponse response){
        String ruleIdParameter = request.getParameter("ruleId");
        long ruleId = Long.valueOf(ruleIdParameter);
        String actionForParameter = request.getParameter("actionFor");
        if ("add".equals(actionForParameter) || "edit".equals(actionForParameter)){
            List<ShippingPricingRuleItem> shippingPricingRuleItems = null;
            if("add".equals(actionForParameter)){
                shippingPricingRuleItems = shippingTemplateService.listRuleItemsByRuleId(ruleId);
            }else {
                String ruleItemIdParameter = request.getParameter("ruleItemId");
                long ruleItemId = Long.valueOf(ruleItemIdParameter);
                shippingPricingRuleItems = shippingTemplateService.listRuleItemsByRuleIdAndRuleItemIdNotEq(ruleId,ruleItemId);
            }
            List<List<BigRegionDTO>> result = new ArrayList<>();
            for (ShippingPricingRuleItem item : shippingPricingRuleItems){
                result.add(JSON.parseArray(item.getRegions(),BigRegionDTO.class));
            }
            return result;

        } else {
            return null;
        }
    }
}
