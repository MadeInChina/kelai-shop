package cn.com.kelaikewang.controller.account.security;

import cn.com.kelaikewang.controller.account.validator.UpdateMobileFormValidator;
import cn.com.kelaikewang.core.sms.service.SmsService;
import cn.com.kelaikewang.core.profile.form.UpdateMobileForm;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.broadleafcommerce.profile.web.core.service.login.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/account/security/mobile")
public class UpdateMobileController {

    @Resource(name = "blUpdateMobileFormValidator")
    private UpdateMobileFormValidator updateMobileFormValidator;

    @Resource(name = "blCustomerService")
    private ZaoJiCMSCustomerService youZanPuZiCustomerService;

    @Resource(name = "blLoginService")
    protected LoginService loginService;

    @Resource(name = "zjcmsSmsService")
    private SmsService smsService;

    @RequestMapping(method = RequestMethod.GET)
    public String viewUpdateMobile(HttpServletRequest request, @ModelAttribute("updateMobileForm") UpdateMobileForm updateMobileForm, Model model){
        model.addAttribute("currentMobile", CustomerState.getCustomer().getUsername());
        return "account/security/updateMobile";
    }
    @RequestMapping(method = RequestMethod.POST)
    public String processUpdateMobile(HttpServletRequest request, @ModelAttribute("updateMobileForm")UpdateMobileForm updateMobileForm, BindingResult errors,Model model){
        updateMobileFormValidator.validate(updateMobileForm,errors);
        if (errors.hasErrors()){
            model.addAttribute("currentMobile", CustomerState.getCustomer().getUsername());
            return "account/security/updateMobile";
        }else {
            youZanPuZiCustomerService.updateMobile(updateMobileForm);
            smsService.setCodeUsed(updateMobileForm.getNewMobile());
            loginService.loginCustomer(updateMobileForm.getNewMobile(),updateMobileForm.getPassword());
            model.addAttribute("successMessage","手机号修改成功");
            return "account/security/updateMobile";
        }
    }
    @ModelAttribute("updateMobileForm")
    public UpdateMobileForm initUpdateMobileForm() {
        return new UpdateMobileForm();
    }
}
