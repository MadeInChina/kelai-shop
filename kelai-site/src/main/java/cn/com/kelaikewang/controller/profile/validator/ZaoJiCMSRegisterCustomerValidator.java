package cn.com.kelaikewang.controller.profile.validator;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseStatus;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import cn.com.kelaikewang.core.sms.service.SmsService;
import cn.com.kelaikewang.core.profile.form.ZaoJiCMSRegisterCustomerForm;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.controller.validator.RegisterCustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.annotation.Resource;

@Component("zjcmsRegisterCustomerValidator")
public class ZaoJiCMSRegisterCustomerValidator extends RegisterCustomerValidator {
    @Resource(name = "blCustomerService")
    private ZaoJiCMSCustomerService customerService;

    @Autowired
    private SmsService smsService;

    protected static final String MOBILE_EXPRESSION = "^1\\d{10}$";
    protected static final String CODE_EXPRESSION = "^\\d{6}$";

    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(ZaoJiCMSRegisterCustomerForm.class);
    }

    @Override
    public void validate(Object obj, Errors errors, boolean useEmailForUsername) {
        /*super.validate(obj, errors, useEmailForUsername);*/
        ZaoJiCMSRegisterCustomerForm form = (ZaoJiCMSRegisterCustomerForm)obj;
        Customer customerFromDb = this.customerService.readCustomerByUsername(form.getCustomer().getUsername());
        if (customerFromDb != null && customerFromDb.isRegistered()) {
            if (useEmailForUsername) {
                errors.rejectValue("customer.emailAddress", "emailAddress.used", (Object[])null, (String)null);
            } else {
                errors.rejectValue("customer.username", "username.used", (Object[])null, (String)null);
            }
        }
        if (!StringUtils.isEmpty(form.getNickname())) {
            Customer customerByNickname = this.customerService.readCustomerByNickname(form.getNickname());
            if (customerByNickname != null) {
                errors.rejectValue("customer.nickname", "nickname.used", (Object[]) null, (String) null);
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verificationCode", "verificationCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "passwordConfirm.required");

        errors.pushNestedPath("customer");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required");
        /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");*/
        /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");*/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"nickname","nickname.required");

        errors.popNestedPath();
        if (!errors.hasErrors()) {
            if (!form.getCustomer().getUsername().matches(MOBILE_EXPRESSION)){
                errors.rejectValue("customer.username","username.invalid",null,null);
            }
            if (!form.getVerificationCode().matches(CODE_EXPRESSION)){
                errors.rejectValue("verificationCode","verificationCode.invalid",null,null);
            }else {
                ResponseDTO responseDTO = smsService.checkCode(form.getCustomer().getUsername(),form.getVerificationCode());
                if (!ResponseStatus.SUCCESS.equals(responseDTO.getStatus())){
                    errors.rejectValue("verificationCode","verificationCode.invalid",null,null);
                }
            }
            if (!form.getPassword().matches(this.getValidatePasswordExpression())) {
                errors.rejectValue("password", "password.invalid", (Object[])null, (String)null);
            }

            if (!form.getPassword().equals(form.getPasswordConfirm())) {
                errors.rejectValue("password", "passwordConfirm.invalid", (Object[])null, (String)null);
            }

            /*if (!GenericValidator.isEmail(form.getCustomer().getEmailAddress())) {
                errors.rejectValue("customer.emailAddress", "emailAddress.invalid", (Object[])null, (String)null);
            }*/

        }
    }
}
