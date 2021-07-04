package cn.com.kelaikewang.controller.account.validator;

import cn.com.kelaikewang.commons.lang.StringUtils;
import cn.com.kelaikewang.controller.account.security.UpdateEmailForm;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseStatus;
import cn.com.kelaikewang.core.email.service.MailCodeService;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component("blUpdateEmailFormValidator")
public class UpdateEmailFormValidator  implements Validator {

    @Resource(name = "blCustomerService")
    private CustomerService customerService;

    @Resource(name = "zjcmsMailCodeService")
    private MailCodeService mailCodeService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UpdateEmailFormValidator.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdateEmailForm updateEmailForm = (UpdateEmailForm)o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "verificationCode", "verificationCode.required");

        if (!errors.hasErrors()){
            if (!StringUtils.isEmail(updateEmailForm.getEmailAddress())){
                errors.rejectValue("emailAddress", "emailAddress.invalid");
            }
            if (!StringUtils.isVerificationCode(updateEmailForm.getVerificationCode())){
                errors.rejectValue("verificationCode","verificationCode.invalid");
            }
            Customer current = CustomerState.getCustomer();
            if (updateEmailForm.getEmailAddress().equals(current.getEmailAddress())){
                errors.rejectValue("emailAddress","emailAddress.same.as.current");
            }
            if (!errors.hasErrors()){

                Customer customer = customerService.readCustomerByEmail(updateEmailForm.getEmailAddress());
                if (customer != null &&  !current.getId().equals(customer.getId())){
                    errors.rejectValue("emailAddress", "emailAddress.used");
                }
                if (!errors.hasErrors()){
                    ResponseDTO responseDTO = mailCodeService.checkCode(updateEmailForm.getEmailAddress(),updateEmailForm.getVerificationCode());
                    if (!ResponseStatus.SUCCESS.equals(responseDTO.getStatus())){
                        errors.rejectValue("verificationCode","verificationCode.invalid");
                    }
                }

            }
        }
    }
}
