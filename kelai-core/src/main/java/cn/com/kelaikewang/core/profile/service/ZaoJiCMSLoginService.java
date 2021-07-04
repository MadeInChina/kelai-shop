package cn.com.kelaikewang.core.profile.service;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.service.login.LoginService;
import org.springframework.security.core.Authentication;

public interface ZaoJiCMSLoginService extends LoginService {
    Authentication wxAutoLoginCustomer(Customer customer);
}
