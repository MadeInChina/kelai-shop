package cn.com.kelaikewang.controller.account.validator;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseStatus;
import cn.com.kelaikewang.core.sms.service.SmsService;
import cn.com.kelaikewang.commons.lang.StringUtils;
import cn.com.kelaikewang.core.profile.form.UpdateMobileForm;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component("blUpdateMobileFormValidator")
public class UpdateMobileFormValidator implements Validator {
    @Resource(name = "blCustomerService")
    private CustomerService customerService;

    @Autowired
    private SmsService smsService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UpdateMobileForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdateMobileForm form = (UpdateMobileForm)o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newMobile", "username.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verificationCode", "verificationCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");

        if (!errors.hasErrors()){
            Customer customer = CustomerState.getCustomer();

            if (!StringUtils.isMobile(form.getNewMobile())){
                errors.rejectValue("newMobile","username.invalid",null,null);
            }else {
                if (form.getNewMobile().equals(customer.getUsername())){
                    errors.rejectValue("newMobile","username.same.as.current",null,null);
                }else {
                    Customer dbCustomer = customerService.readCustomerByUsername(form.getNewMobile());
                    if (dbCustomer != null && dbCustomer.getId().longValue() != customer.getId().longValue()){
                        errors.rejectValue("newMobile", "username.used", null, null);
                    }
                }
            }

            if (!customer.getPassword().equals(form.getPassword())){
                errors.rejectValue("password","password.invalid",null,null);
            }

            if (!StringUtils.isVerificationCode(form.getVerificationCode())){
                errors.rejectValue("verificationCode","verificationCode.invalid",null,null);
            }
            if(!errors.hasErrors()){
                ResponseDTO responseDTO = smsService.checkCode(form.getNewMobile(),form.getVerificationCode());
                if (!ResponseStatus.SUCCESS.equals(responseDTO.getStatus())){
                    errors.rejectValue("verificationCode","verificationCode.invalid",null,null);
                }
            }
        }
    }
}
