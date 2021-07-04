package cn.com.kelaikewang.admin.controller.dashboard;

import cn.com.kelaikewang.core.catalog.service.ZaoJiCMSProductService;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DashboardController {
   
    @Resource(name = "blCustomerService")
    private ZaoJiCMSCustomerService nextShopCustomerService;
    @Resource(name = "blOrderService")
    private ZaoJiCMSOrderService nextShopOrderService;
    @Resource(name = "zjcmsProductService")
    private ZaoJiCMSProductService nextShopProductService;

    @RequestMapping("/dashboard")
    public String viewDashboard(HttpServletRequest request, HttpServletResponse response, Model model){

        model.addAttribute("orderLineChartData",nextShopOrderService.getLast12MonthsOrderTransactionAmount());
        model.addAttribute("topSaleProducts",nextShopProductService.getTopSaleProducts(10));
        model.addAttribute("orderTransactionAmount",nextShopOrderService.getOrderTransactionAmountByDateRange());

        model.addAttribute("customerLineChartDate",nextShopCustomerService.getCountOfLast12MonthsUserRegistration());
        model.addAttribute("countOfCustomerReg",nextShopCustomerService.getCountOfUserRegistrationByDateRange());

        model.addAttribute("countOfUnprocessedOrder",nextShopOrderService.readCountOfOrderByStatus(ZaoJiCMSOrderStatus.PAID));

        model.addAttribute("templateName", "views/dashboard/dashboard");
        model.addAttribute("customScriptView","views/dashboard/customScript/dashboard");
        model.addAttribute("customHeadView","views/dashboard/customHead/dashboard");
        return "layout/fullPageLayout";
    }
}
