package cn.com.kelaikewang.core.profile.service;

import cn.com.kelaikewang.core.profile.form.ZaoJiCMSRegisterCustomerForm;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.broadleafcommerce.profile.web.core.form.RegisterCustomerForm;
import org.broadleafcommerce.profile.web.core.service.register.RegistrationServiceImpl;
import org.springframework.stereotype.Service;

@Service("zjcmsRegistrationService")
public class ZaoJiCMSRegistrationServiceImpl extends RegistrationServiceImpl {
    @Override
    public RegisterCustomerForm initCustomerRegistrationForm() {
        Customer customer = CustomerState.getCustomer();
        if (customer == null || !customer.isAnonymous()) {
            customer = this.customerService.createCustomerFromId((Long)null);
        }

        ZaoJiCMSRegisterCustomerForm customerRegistrationForm = new ZaoJiCMSRegisterCustomerForm();
        customerRegistrationForm.setCustomer(customer);
        return customerRegistrationForm;
    }
}
