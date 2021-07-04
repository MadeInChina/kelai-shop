package cn.com.kelaikewang.core.profile.service;

import org.broadleafcommerce.common.web.BroadleafWebRequestProcessor;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.service.login.LoginServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

public class ZaoJiCMSLoginServiceImpl extends LoginServiceImpl implements ZaoJiCMSLoginService{
    @Resource(name = "blUserDetailsService")
    private UserDetailsService userDetailsService;

    @Resource(name = "blAuthenticationManager")
    private AuthenticationManager authenticationManager;
    @Resource(name = "blCustomerStateRequestProcessor")
    private BroadleafWebRequestProcessor customerStateRequestProcessor;

    /**
     * 微信自动登录，不再验证密码
     * @param customer
     * @return
     */
    @Override
    public Authentication wxAutoLoginCustomer(Customer customer) {
        UserDetails principal = this.userDetailsService.loadUserByUsername(customer.getUsername());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, customer.getPassword(), principal.getAuthorities());
        //微信自动登录不再验证密码
        //Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);
        this.customerStateRequestProcessor.process(this.getWebRequest());
        this.cartStateRequestProcessor.process(this.getWebRequest());
        return token;
    }
}
