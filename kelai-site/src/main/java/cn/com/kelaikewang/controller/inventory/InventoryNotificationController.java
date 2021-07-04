package cn.com.kelaikewang.controller.inventory;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseStatus;
import cn.com.kelaikewang.core.email.service.MailCodeService;
import cn.com.kelaikewang.core.inventory.service.InventoryNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("inventory")
public class InventoryNotificationController {
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "zjcmsMailCodeService")
    private MailCodeService mailCodeService;

    @Resource(name = "blCustomerService")
    private CustomerService customerService;

    @Resource(name = "zjcmsInventoryNotificationService")
    private InventoryNotificationService inventoryNotificationService;

    @RequestMapping(value = "notification/{productId}",method = RequestMethod.GET)
    public String notification(@PathVariable("productId")long productId, Model model){
        if (CustomerState.getCustomer().isAnonymous()){
            return "redirect:/login";
        }
        Product product = catalogService.findProductById(productId);
        model.addAttribute("product",product);
        model.addAttribute("email",CustomerState.getCustomer().getEmailAddress());
        return "inventory/notification";
    }
    @RequestMapping(value = "notification/{productId}",method = RequestMethod.POST)
    public String processNotification(@PathVariable("productId")long productId, Model model, HttpServletRequest request){
        if (CustomerState.getCustomer().isAnonymous()){
            return "redirect:/login";
        }
        Customer customer = CustomerState.getCustomer();
        model.addAttribute("customer",customer);
        String email = request.getParameter("emailAddress");
        String code = request.getParameter("verificationCode");
        if (!cn.com.kelaikewang.commons.lang.StringUtils.isEmail(email)){
            Product product = catalogService.findProductById(productId);
            model.addAttribute("product",product);
            model.addAttribute("errorMsg","无效的邮箱地址");
            return "inventory/notification";
        }
        if(StringUtils.isEmpty(customer.getEmailAddress())){
            Product product = catalogService.findProductById(productId);
            if (!cn.com.kelaikewang.commons.lang.StringUtils.isVerificationCode(code)){

                model.addAttribute("product",product);
                model.addAttribute("errorMsg","无效的验证码");
                model.addAttribute("email",email);
                return "inventory/notification";
            }
            ResponseDTO responseDTO = mailCodeService.checkCode(email,code);
            if (!ResponseStatus.SUCCESS.equals(responseDTO.getStatus())){
                model.addAttribute("product",product);
                model.addAttribute("errorMsg","无效的验证码");
                model.addAttribute("email",email);
                return "inventory/notification";
            }else {
                customer.setEmailAddress(email);
                customerService.saveCustomer(customer);
                mailCodeService.setCodeUsed(email);
            }
        }
        /*InventoryNotification inventoryNotification = new InventoryNotificationImpl();
        inventoryNotification.setNotificationTimes(0);
        inventoryNotification.setNotificationStatus(InventoryNotificationStatus.UNPROCESSED);
        inventoryNotification.setCreated(new Date());
        inventoryNotification.setCustomerId(customer.getId());
        inventoryNotification.setProductId(productId);*/
        inventoryNotificationService.create(productId,customer.getId());
        //model.addAttribute("email",CustomerState.getCustomer().getEmailAddress());
        return "redirect:/inventory/notificationSuccess";
    }
    @RequestMapping("notificationSuccess")
    public String notificationSuccess(){
        return "/inventory/notificationSuccess";
    }
}
