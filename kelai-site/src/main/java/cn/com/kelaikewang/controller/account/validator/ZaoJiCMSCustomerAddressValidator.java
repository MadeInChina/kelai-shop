package cn.com.kelaikewang.controller.account.validator;

import org.broadleafcommerce.core.web.controller.account.validator.CustomerAddressValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class ZaoJiCMSCustomerAddressValidator extends CustomerAddressValidator {
    @Override
    public void validate(Object obj, Errors errors) {
        super.validate(obj, errors);
        if (!this.isCustomValidationEnabled()){
            /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.streetCommunity", "streetCode.required");*/
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.region", "regionCode.required");

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.provinceCode", "provinceCode.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.cityCode", "cityCode.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.regionCode", "regionCode.required");
            /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.streetCode", "streetCode.required");*/

        }

    }
}
