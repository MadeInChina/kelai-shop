package cn.com.kelaikewang.controller.account.validator;

import cn.com.kelaikewang.commons.lang.StringUtils;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseStatus;
import cn.com.kelaikewang.core.sms.service.SmsService;
import cn.com.kelaikewang.controller.account.ForgotPasswordForm;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component("blForgotPasswordFormValidator")
public class ForgotPasswordFormValidator implements Validator {
    @Autowired
    private SmsService smsService;
    @Resource(name = "blCustomerService")
    private CustomerService customerService;
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ForgotPasswordForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ForgotPasswordForm form = (ForgotPasswordForm)o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile", "username.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verificationCode", "verificationCode.required");

        if (!errors.hasErrors()){

            if (!StringUtils.isMobile(form.getMobile())){
                errors.rejectValue("mobile","username.invalid",null,null);
            }else {
                Customer customer = customerService.readCustomerByUsername(form.getMobile());
                if (customer == null){
                    errors.rejectValue("mobile","username.not.exist",null,null);
                }else {
                    if (!StringUtils.isVerificationCode(form.getVerificationCode())){
                        errors.rejectValue("verificationCode","verificationCode.invalid",null,null);
                    }else {
                        ResponseDTO responseDTO = smsService.checkCode(form.getMobile(),form.getVerificationCode());
                        if (!ResponseStatus.SUCCESS.equals(responseDTO.getStatus())){
                            errors.rejectValue("verificationCode","verificationCode.invalid",null,null);
                        }
                    }
                }
            }


        }
    }
}
