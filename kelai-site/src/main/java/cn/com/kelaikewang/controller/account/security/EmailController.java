package cn.com.kelaikewang.controller.account.security;

import cn.com.kelaikewang.controller.account.validator.BindEmailFormValidator;
import cn.com.kelaikewang.controller.account.validator.UpdateEmailFormValidator;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/account/security")
public class EmailController {

    @Resource(name = "blUpdateEmailFormValidator")
    private UpdateEmailFormValidator updateEmailFormValidator;

    @Resource(name = "blBindEmailFormValidator")
    private BindEmailFormValidator bindEmailFormValidator;

    @Resource(name = "blCustomerService")
    private CustomerService customerService;

    @RequestMapping(value = "bindEmail",method = RequestMethod.GET)
    public String bindEmail(@ModelAttribute("bindEmailForm")BindEmailForm bindEmailForm, Model model){
        return "account/security/bindEmail";
    }
    @RequestMapping(value = "bindEmail",method = RequestMethod.POST)
    public String processBindEmail(@ModelAttribute("bindEmailForm")BindEmailForm bindEmailForm, Model model, BindingResult errors, HttpServletRequest request){
        bindEmailFormValidator.validate(bindEmailForm,errors);
        if (errors.hasErrors()){
            return "account/security/bindEmail";
        }
        Customer customer = CustomerState.getCustomer();
        customer.setEmailAddress(bindEmailForm.getEmailAddress());
        customerService.saveCustomer(customer);
        //model.addAttribute("successMessage","绑定邮箱成功");
        request.getSession().setAttribute("successMessage","绑定邮箱成功");
        return "redirect:/account/actionMsg/success";
    }
    @RequestMapping(value = "updateEmail",method = RequestMethod.GET)
    public String updateEmail(@ModelAttribute("updateEmailForm")UpdateEmailForm updateEmailForm,Model model){
        Customer customer = CustomerState.getCustomer();
        if (StringUtils.isEmpty(customer.getEmailAddress())){
            return "redirect:/account/security/bindEmail";
        }
        model.addAttribute("currentEmail",customer.getEmailAddress());
        return "account/security/updateEmail";
    }
    @RequestMapping(value = "updateEmail",method = RequestMethod.POST)
    public String processUpdateEmail(@ModelAttribute("updateEmailForm")UpdateEmailForm updateEmailForm,Model model, BindingResult errors,HttpServletRequest request){
        Customer customer = CustomerState.getCustomer();
        if (StringUtils.isEmpty(customer.getEmailAddress())){
            return "redirect:/account/security/bindEmail";
        }
        updateEmailFormValidator.validate(updateEmailForm,errors);
        if (errors.hasErrors()){
            model.addAttribute("currentEmail",customer.getEmailAddress());
            return "account/security/updateEmail";
        }
        customer.setEmailAddress(updateEmailForm.getEmailAddress());
        customerService.saveCustomer(customer);
        //model.addAttribute("successMessage","修改邮箱成功");
        request.getSession().setAttribute("successMessage","修改邮箱成功");
        return "redirect:/account/actionMsg/success";
        //return "account/security/updateEmail";
    }
    @ModelAttribute("updateEmailForm")
    public UpdateEmailForm initUpdateEmailForm() {
        return new UpdateEmailForm();
    }
    @ModelAttribute("bindEmailForm")
    public BindEmailForm initBindEmailForm() {
        return new BindEmailForm();
    }
}
