package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.commons.lang.StringUtils;
import cn.com.kelaikewang.controller.account.validator.ForgotPasswordFormValidator;
import cn.com.kelaikewang.core.sms.service.SmsService;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.web.controller.account.BroadleafLoginController;
import org.broadleafcommerce.core.web.controller.account.ResetPasswordForm;
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

/**
 * The controller responsible for all actions involving logging a customer in
 */

public class ZaoJiCMSLoginController extends BroadleafLoginController {

    private static final String FORGOT_PASSWORD_AUTH_FLAG = "FORGOT_PASSWORD_AUTH_FLAG";
    private static final String FORGOT_PASSWORD_MOBILE = "change_password_request_username";
    private static final String RESET_PASSWORD_TOKEN = "RESET_PASSWORD_TOKEN";

    @Resource(name = "blRegistrationService")
    RegistrationService registrationService;

    @Resource(name = "blForgotPasswordFormValidator")
    private ForgotPasswordFormValidator forgotPasswordFormValidator;

    @Resource(name = "blCustomerService")
    private ZaoJiCMSCustomerService youZanPuZiCustomerService;

    @Resource(name = "zjcmsSmsService")
    private SmsService smsService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String loginView = super.login(request, response, model);

        RegisterCustomerForm registrationForm = buildRegistrationForm();
        model.addAttribute("registrationForm", registrationForm);

        return loginView;
    }

    protected RegisterCustomerForm buildRegistrationForm() {
        RegisterCustomerForm registrationForm = registrationService.initCustomerRegistrationForm();
        registrationService.addRedirectUrlToForm(registrationForm);

        return registrationForm;
    }

    @RequestMapping(value="/login/forgotPassword", method= RequestMethod.GET)
    public String forgotPassword(@ModelAttribute("forgotPasswordForm")ForgotPasswordForm forgotPasswordForm,HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.forgotPassword(request, response, model);
    }

    /*@RequestMapping(value="/login/forgotPassword", method=RequestMethod.POST)
    public String processForgotPassword(@RequestParam("emailAddress") String emailAddress, HttpServletRequest request, Model model) {
        return super.processForgotPassword(emailAddress, request, model);
    }*/

    @RequestMapping(value="/login/forgotPassword", method=RequestMethod.POST)
    public String processForgotPassword(HttpServletRequest request,@ModelAttribute("forgotPasswordForm")ForgotPasswordForm forgotPasswordForm,BindingResult errors, Model model) {
        forgotPasswordFormValidator.validate(forgotPasswordForm,errors);
        if (errors.hasErrors()){
            return getForgotPasswordView();
        }else {
            request.getSession().setAttribute(FORGOT_PASSWORD_AUTH_FLAG,true);
            request.getSession().setAttribute(FORGOT_PASSWORD_MOBILE,forgotPasswordForm.getMobile());
            String token = youZanPuZiCustomerService.generateForgotPasswordToken(forgotPasswordForm.getMobile());
            smsService.setCodeUsed(forgotPasswordForm.getMobile());
            if (org.apache.commons.lang3.StringUtils.isEmpty(token)){
                return "redirect:/login/forgotPassword";
            }
            request.getSession().setAttribute(RESET_PASSWORD_TOKEN,token);
            return "redirect:/login/resetPassword";
        }
    }
    @ModelAttribute("forgotPasswordForm")
    public ForgotPasswordForm initForgotPasswordForm(){
        return new ForgotPasswordForm();
    }

    @RequestMapping(value="/login/resetPassword", method=RequestMethod.GET)
    public String resetPassword(HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
        //resetPasswordForm.setToken((String) request.getSession().getAttribute(RESET_PASSWORD_TOKEN));
        Boolean authFlag = (Boolean) request.getSession().getAttribute(FORGOT_PASSWORD_AUTH_FLAG);
        String mobile = (String)request.getSession().getAttribute(FORGOT_PASSWORD_MOBILE);
        if (authFlag == null || !authFlag || !StringUtils.isMobile(mobile)){
            return "redirect:/login/forgotPassword";
        }
        model.addAttribute("mobile",mobile);
        //model.addAttribute("resetPasswordForm",resetPasswordForm);
        String view = super.resetPassword(request, response, model);
        ResetPasswordForm resetPasswordForm = (ResetPasswordForm)model.asMap().get("resetPasswordForm");
        resetPasswordForm.setToken((String) request.getSession().getAttribute(RESET_PASSWORD_TOKEN));
        return view;
    }

    @RequestMapping(value="/login/resetPassword", method=RequestMethod.POST)
    public String processResetPassword(@ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, HttpServletRequest request, HttpServletResponse response, Model model, BindingResult errors) throws ServiceException {
        Boolean authFlag = (Boolean) request.getSession().getAttribute(FORGOT_PASSWORD_AUTH_FLAG);
        String mobile = (String)request.getSession().getAttribute(FORGOT_PASSWORD_MOBILE);
        resetPasswordForm.setToken((String) request.getSession().getAttribute(RESET_PASSWORD_TOKEN));
        if (authFlag == null || !authFlag || !StringUtils.isMobile(mobile) || !mobile.equals(resetPasswordForm.getUsername())){
            return "redirect:/login/forgotPassword";
        }
        resetPasswordForm.setUsername(mobile);
        if (org.apache.commons.lang3.StringUtils.isEmpty(resetPasswordForm.getPassword())) {
            errors.rejectValue("password", "password.required",null,null);
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(resetPasswordForm.getUsername())) {
            errors.rejectValue("username", "username.required",null,null);
        }
        return super.processResetPassword(resetPasswordForm, request, response, model, errors);
    }

    @Override
    public String getResetPasswordUrl(HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + getResetPasswordPort(request, request.getScheme());

        if (request.getContextPath() != null && ! "".equals(request.getContextPath())) {
            url = url + request.getContextPath() + "/login/resetPassword";
        } else {
            url = url + "/login/resetPassword";
        }
        return url;
    }
}
