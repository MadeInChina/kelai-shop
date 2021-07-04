package cn.com.kelaikewang.controller.account.validator;

import cn.com.kelaikewang.controller.account.ZaoJiCMSUpdateAccountForm;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSCustomer;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import org.broadleafcommerce.core.web.controller.account.UpdateAccountForm;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.annotation.Resource;

public class UpdateAccountValidator extends org.broadleafcommerce.core.web.controller.account.validator.UpdateAccountValidator {
    @Resource(name = "blCustomerService")
    private ZaoJiCMSCustomerService customerService;

    @Override
    public void validate(UpdateAccountForm form, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"nickname","nickname.required");
        ZaoJiCMSUpdateAccountForm accountForm = (ZaoJiCMSUpdateAccountForm)form;
        ZaoJiCMSCustomer customerByNickname = (ZaoJiCMSCustomer)customerService.readCustomerByNickname(accountForm.getNickname());
        Customer currentCustomer = CustomerState.getCustomer();
        if (customerByNickname != null && !customerByNickname.getId().equals(currentCustomer.getId())){
            errors.rejectValue("nickname", "nickname.used", (Object[])null, (String)null);
        }
    }
}
