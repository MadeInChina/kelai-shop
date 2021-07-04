package cn.com.kelaikewang.admin.controller.order;

import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import cn.com.kelaikewang.core.logistics.service.ExpressCompanyService;
import cn.com.kelaikewang.core.logistics.service.ShippingService;
import cn.com.kelaikewang.core.order.domain.AfterSales;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.service.AfterSalesService;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderItemService;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.admin.web.controller.entity.AdminOrderController;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.web.form.component.ListGrid;
import org.broadleafcommerce.openadmin.web.form.entity.EntityForm;
import org.broadleafcommerce.openadmin.web.form.entity.EntityFormAction;
import org.broadleafcommerce.openadmin.web.form.entity.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ZaoJiCMSAdminOrderController extends AdminOrderController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZaoJiCMSAdminOrderController.class);

    @Resource(name = "zjcmsExpressCompanyService")
    protected ExpressCompanyService expressCompanyService;
    @Resource(name = "zjcmsShippingService")
    protected ShippingService shippingService;
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "blOrderItemService")
    protected ZaoJiCMSOrderItemService orderItemService;
    @Resource(name = "zjcmsAfterSalesService")
    protected AfterSalesService afterSalesService;

    @Override
    public String viewEntityList(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        String view = super.viewEntityList(request, response, model, pathVars, requestParams);
        Map<String, Object> modelMap = model.asMap();
        ListGrid listGrid = (ListGrid)modelMap.get("listGrid");
        listGrid.setIsReadOnly(true);
        //mainActions
        List mainActions = (List) modelMap.get("mainActions");
        if (mainActions != null){
            mainActions.clear();
        }
        return view;
    }

    @Override
    public String viewEntityForm(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id) throws Exception {
        String view = super.viewEntityForm(request, response, model, pathVars, id);
        Map<String,Object> modelMap = model.asMap();
        EntityForm entityForm = (EntityForm)modelMap.get("entityForm");
        if (entityForm != null){
            List<EntityFormAction> actions = entityForm.getActions().stream().filter(item->!item.getId().equals("DELETE")).collect(Collectors.toList());
            entityForm.setActions(actions);
        }
        Optional<Tab> tab = entityForm.getTabs().stream().filter(item->item.getKey().equals("OrderImpl_Fulfillment_Groups_Tab")).findFirst();
        if (tab.isPresent()){
            tab.get().setCustomTemplate("/views/order/partials/logistics");
        }
        /*tab = entityForm.getTabs().stream().filter(item->item.getKey().equals("OrderImpl_AfterSales")).findFirst();
        if (tab.isPresent()){
            //tab.get().setCustomTemplate("components/afterSales");
        }*/
        long orderId = Long.parseLong(id);
        model.addAttribute("expressCompanies",expressCompanyService.listAll());
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);

        model.addAttribute("customer",order.getCustomer());
        AfterSales afterSales = null;
        List<AfterSales> afterSalesList = order.getAfterSales();
        if (afterSalesList == null || afterSalesList.size() == 0){
            afterSales = null;
        }else {
            afterSales = afterSalesList.get(0);
        }
        model.addAttribute("afterSales",afterSales);
        model.addAttribute("order",order);

        if (!StringUtils.isEmpty(order.getLogisticsTrackingNumber())) {
            model.addAttribute("logisticTrackingInfo", shippingService.getLogisticTrackingInfo(orderId));
        }

        model.addAttribute("customScriptView","views/order/customScript/orderActions");
        return view;
    }
    @RequestMapping("send-out")
    @ResponseBody
    public ResponseDTO sendOut(@RequestParam("orderId") long orderId,
                               @RequestParam("expressCompanyId")long expressCompanyId,
                               @RequestParam("trackingNumber")String trackingNumber){
        shippingService.sendOut(orderId,expressCompanyId,trackingNumber);
        return ResponseDTO.success("发货成功");
    }

    @Override
    public String viewEntityTab(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @PathVariable("tabName") String tabName, @ModelAttribute("entityForm") EntityForm entityForm, @ModelAttribute("entity") Entity entity) throws Exception {
        String view = super.viewEntityTab(request, response, model, pathVars, id, tabName, entityForm, entity);

        return view;
    }

    @Override
    public String showViewCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable Map<String, String> pathVars, @PathVariable("id") String id, @PathVariable("collectionField") String collectionField, @PathVariable("collectionItemId") String collectionItemId) throws Exception {
        String view = super.showViewCollectionItem(request, response, model, pathVars, id, collectionField, collectionItemId);
        if ("afterSales".equals(collectionField)){
            model.addAttribute("viewType","order/modal/afterSales");
            AfterSales afterSales = afterSalesService.getById(Long.parseLong(collectionItemId));
            model.addAttribute("afterSales",afterSalesService.getById(Long.parseLong(collectionItemId)));
            model.addAttribute("customer",afterSales.getCustomer());
        }
        return view;
    }
    @RequestMapping(value = "agreeRefund",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO agreeRefund(@RequestParam("id")long id,@RequestParam("reply")String reply) throws AlipayApiException, WxPayException {
        try {
            return afterSalesService.agree(id, reply);
        }catch (Exception e){
            LOGGER.error("退款异常",e);
            return ResponseDTO.fail("退款失败：" + e.getMessage());
        }
    }
    @RequestMapping(value = "disagreeRefund",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO disagreeRefund(@RequestParam("id")long id,@RequestParam("reply")String reply) throws AlipayApiException, WxPayException {
        afterSalesService.disagree(id,reply);
        return ResponseDTO.success("操作成功");
    }
}
