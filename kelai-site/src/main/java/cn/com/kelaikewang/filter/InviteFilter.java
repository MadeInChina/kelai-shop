package cn.com.kelaikewang.filter;


import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.util.BLCSystemProperty;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InviteFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //如果启用了分销功能
        /*if (BLCSystemProperty.resolveBooleanSystemProperty("enableDistribution")) {
            String sid = httpServletRequest.getParameter(ParameterNameConstants.SID);
            if (!StringUtils.isEmpty(sid)) {
                sid = sid.replace(" ","@");
                Cookie cookie = new Cookie(CookieConstants.SID, sid);
                //Sets the maximum age of the cookie in seconds.
                cookie.setMaxAge(CookieConstants.SID_MAX_AGE); //一年的有效期
                httpServletResponse.addCookie(cookie);
            }
        }*/
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
