package cn.com.kelaikewang.filter;

import cn.com.kelaikewang.integration.wechat.constant.WeChatConstants;
import cn.com.kelaikewang.commons.web.RequestUtils;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSLoginService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.broadleafcommerce.common.util.BLCRequestUtils;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class WeChatAutoLoginFilter extends OncePerRequestFilter {

    private ZaoJiCMSCustomerService nsCustomerService;

    private ZaoJiCMSLoginService loginService;

    public void setNsCustomerService(ZaoJiCMSCustomerService nsCustomerService) {
        this.nsCustomerService = nsCustomerService;
    }

    public void setLoginService(ZaoJiCMSLoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (RequestUtils.isWeChatEnv()){
            //只有动态页面才进行自动登录
            if (BroadleafRequestContext.getBroadleafRequestContext().getWebRequest() != null && BLCRequestUtils.isOKtoUseSession(BroadleafRequestContext.getBroadleafRequestContext().getWebRequest())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if ("anonymousUser".equals(authentication.getName()) || !authentication.isAuthenticated()) {
                    WxMpUser wxMpUser = (WxMpUser) httpServletRequest.getSession().getAttribute(WeChatConstants.WX_MP_USER);
                    if (wxMpUser != null) {
                        Customer customer = nsCustomerService.readCustomerByWeChatOpenId(wxMpUser.getOpenId());
                        if (customer != null) {
                            //loginService.loginCustomer(customer.getUsername(), customer.getPassword());
                            loginService.wxAutoLoginCustomer(customer);
                        }
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
