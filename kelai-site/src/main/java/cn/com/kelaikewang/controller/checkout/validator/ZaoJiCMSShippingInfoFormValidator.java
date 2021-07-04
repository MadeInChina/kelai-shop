package cn.com.kelaikewang.controller.checkout.validator;

import org.broadleafcommerce.common.util.BLCSystemProperty;
import org.broadleafcommerce.core.web.checkout.validator.ShippingInfoFormValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


public class ZaoJiCMSShippingInfoFormValidator extends ShippingInfoFormValidator {
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
