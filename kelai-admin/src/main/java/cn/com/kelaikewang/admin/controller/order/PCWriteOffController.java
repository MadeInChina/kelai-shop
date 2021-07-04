package cn.com.kelaikewang.admin.controller.order;

import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.openadmin.server.security.domain.AdminUser;
import org.broadleafcommerce.openadmin.server.security.remote.SecurityVerifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("order/pcWriteOff")
public class PCWriteOffController {
    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService orderService;

    @Resource(name = "blAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;

    @RequestMapping(value = "scanCode",method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        model.addAttribute("count",orderService.getCountOfOrderWriteOff(adminUser.getId()));
        model.addAttribute("url", URLEncoder.encode(request.getRequestURL().toString(),"UTF-8"));
        model.addAttribute("templateName", "views/order/writeOff/scanCode");
        model.addAttribute("customScriptView","views/order/writeOff/customScript/scanCode");
        return "layout/fullPageLayout";
    }
    @RequestMapping(value = "orderList",method = RequestMethod.GET)
    public String list(HttpServletRequest request, Model model){
        int pageIndex = 1;
        String pageIndexParam = request.getParameter("pageIndex");
        if (!StringUtils.isEmpty(pageIndexParam)){
            pageIndex = Integer.parseInt(pageIndexParam);
        }
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        model.addAttribute("page",orderService.getPageOfOrderWriteOff(adminUser.getId(),pageIndex,10));
        model.addAttribute("templateName", "views/order/writeOff/orderList");
        model.addAttribute("customHeadView","views/order/writeOff/customHead/detail");
        return "layout/fullPageLayout";
    }
    @RequestMapping(value = "orderDetail",method = RequestMethod.GET)
    public String orderDetail(@RequestParam("orderId")long orderId, Model model){
        model.addAttribute("order",orderService.findOrderById(orderId));
        model.addAttribute("templateName", "views/order/writeOff/orderDetail");
        model.addAttribute("customHeadView","views/order/writeOff/customHead/detail");
        return "layout/fullPageLayout";
    }
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public String detail(@RequestParam("writeOffCode")long writeOffCode,Model model) {
        model.addAttribute("order",orderService.getByWriteOffCode(writeOffCode));
        model.addAttribute("templateName", "views/order/writeOff/detail");
        model.addAttribute("customHeadView","views/order/writeOff/customHead/detail");
        model.addAttribute("customScriptView","views/order/writeOff/customScript/detail");
        return "layout/fullPageLayout";
    }
    @RequestMapping(value = "process",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO process(@RequestParam("writeOffCode")long writeOffCode){
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        return orderService.orderWriteOff(writeOffCode,adminUser.getId());
    }
}
