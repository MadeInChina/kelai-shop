package cn.com.kelaikewang.controller.account.security;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class SecurityController {
    @RequestMapping("security")
    public String security(Model model){
        Customer customer = CustomerState.getCustomer();
        model.addAttribute("mobile",customer.getUsername());
        model.addAttribute("email",customer.getEmailAddress());
        return "account/security/security";
    }
}
