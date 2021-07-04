package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.commons.web.RequestUtils;
import cn.com.kelaikewang.controller.profile.validator.ZaoJiCMSRegisterCustomerValidator;
import cn.com.kelaikewang.core.sms.service.SmsService;
import cn.com.kelaikewang.core.profile.form.ZaoJiCMSRegisterCustomerForm;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import cn.com.kelaikewang.integration.wechat.constant.WeChatConstants;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.order.domain.NullOrderImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.core.web.controller.account.BroadleafRegisterController;
import org.broadleafcommerce.core.web.order.CartState;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.form.RegisterCustomerForm;
import org.broadleafcommerce.profile.web.core.service.register.RegistrationService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ZaoJiCMSRegisterController extends BroadleafRegisterController {

    @Resource(name = "zjcmsRegistrationService")
    private RegistrationService registrationService;

    @Resource(name = "zjcmsRegisterCustomerValidator")
    private ZaoJiCMSRegisterCustomerValidator youZanPuZiRegisterCustomerValidator;

    @Resource(name = "blCustomerService")
    private ZaoJiCMSCustomerService youZanPuZiCustomerService;

    @Resource(name = "zjcmsSmsService")
    private SmsService smsService;

    @RequestMapping(method= RequestMethod.GET)
    public String register(HttpServletRequest request, HttpServletResponse response, Model model,
                           @ModelAttribute("registrationForm") ZaoJiCMSRegisterCustomerForm registerCustomerForm) {
        return super.register(registerCustomerForm, request, response, model);
    }

    @RequestMapping(method=RequestMethod.POST)
    public String processRegister(HttpServletRequest request, HttpServletResponse response, Model model,
                                  @ModelAttribute("registrationForm") ZaoJiCMSRegisterCustomerForm registerCustomerForm, BindingResult errors) throws ServiceException, PricingException {
        return this.processRegister(registerCustomerForm, errors, request, response, model);
    }

    @Override
    public String processRegister(RegisterCustomerForm registerCustomerForm, BindingResult errors, HttpServletRequest request, HttpServletResponse response, Model model) throws ServiceException, PricingException {
        //return super.processRegister(registerCustomerForm, errors, request, response, model);
        Customer newCustomer;
        if (this.useEmailForLogin) {
            newCustomer = registerCustomerForm.getCustomer();
            newCustomer.setUsername(newCustomer.getEmailAddress());
        }

        this.youZanPuZiRegisterCustomerValidator.validate(registerCustomerForm, errors, this.useEmailForLogin);
        if (!errors.hasErrors()) {
            newCustomer = this.youZanPuZiCustomerService.registerCustomer(registerCustomerForm.getCustomer(), registerCustomerForm.getPassword(), registerCustomerForm.getPasswordConfirm());

            assert newCustomer != null;

            this.loginService.loginCustomer(registerCustomerForm.getCustomer());

            ZaoJiCMSRegisterCustomerForm youZanPuZiRegisterCustomerForm = (ZaoJiCMSRegisterCustomerForm)registerCustomerForm;
            smsService.setCodeUsed(registerCustomerForm.getCustomer().getUsername());

            if (RequestUtils.isWeChatEnv()) {
                WxMpUser wxMpUser = (WxMpUser)request.getSession().getAttribute(WeChatConstants.WX_MP_USER);
                if (wxMpUser != null) {
                    youZanPuZiCustomerService.saveCustomerWeChatInfo(wxMpUser.getOpenId(), registerCustomerForm.getCustomer().getUsername());
                }
            }

            Order cart = CartState.getCart();
            if (cart != null && !(cart instanceof NullOrderImpl) && cart.getEmailAddress() == null) {
                cart.setEmailAddress(newCustomer.getEmailAddress());
                this.orderService.save(cart, false);
            }

            String redirectUrl = registerCustomerForm.getRedirectUrl();
            if (StringUtils.isNotBlank(redirectUrl) && redirectUrl.contains(":")) {
                redirectUrl = null;
            }

            return StringUtils.isBlank(redirectUrl) ? this.getRegisterSuccessView() : "redirect:" + redirectUrl;
        } else {
            return this.getRegisterView();
        }
    }


    @ModelAttribute("registrationForm")
    public ZaoJiCMSRegisterCustomerForm initYouZanPuZiCustomerRegistrationForm() {
        return (ZaoJiCMSRegisterCustomerForm)registrationService.initCustomerRegistrationForm();
    }

    @Override
    public String getRegisterView() {
        return "authentication/register";
    }
}
