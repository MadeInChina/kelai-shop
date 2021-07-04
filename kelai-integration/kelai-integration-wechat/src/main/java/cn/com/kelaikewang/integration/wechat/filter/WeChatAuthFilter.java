package cn.com.kelaikewang.integration.wechat.filter;


import cn.com.kelaikewang.integration.wechat.constant.WeChatConstants;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class WeChatAuthFilter extends OncePerRequestFilter {

    private WxMpService wxMpService;

    public WxMpService getWxMpService() {
        return wxMpService;
    }

    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (BroadleafRequestContext.getBroadleafRequestContext().getWebRequest() == null ){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String userAgent = httpServletRequest.getHeader("user-agent");
        if ((!StringUtils.isEmpty(userAgent)) && (!userAgent.toLowerCase().contains("micromessenger"))) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        WxMpUser wxMpUser = (WxMpUser)httpServletRequest.getSession().getAttribute(WeChatConstants.WX_MP_USER);
        if (wxMpUser != null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String code = httpServletRequest.getParameter("code");

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(httpServletRequest.getRequestURL().toString());
        //String original_url = httpServletRequest.getRequestURL().toString();


        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        List<String> names = null;
        if ((parameterNames != null) && (parameterNames.hasMoreElements())) {
            names = new ArrayList();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                if ((!"code".equals(name)) && (!names.contains(name))) {
                    names.add(name);
                }
            }
        }

        if ((names != null) && (names.size() > 0)) {
            for (int i = 0; i < names.size(); i++) {
                if (i == 0) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append(names.get(i));
                stringBuilder.append("=");
                stringBuilder.append(httpServletRequest.getParameter(names.get(i)));
            }
        }

        //url = url + URLEncoder.encode(new StringBuilder().append(original_url).append(stringBuilder.toString()).toString());
        if (StringUtils.isEmpty(code)) {
            if (wxMpUser == null) {
                String url = wxMpService.oauth2buildAuthorizationUrl(stringBuilder.toString(), "snsapi_userinfo", "1");
                httpServletResponse.sendRedirect(url);
                //httpServletResponse.sendRedirect("https://wx-auth-proxy.hhmpay.com/proxy.do?original_url=" + URLEncoder.encode(stringBuilder.toString()));
                return;
            }
        } else if (wxMpUser == null) {
            try {
                WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);
                wxMpUser = wxMpService.oauth2getUserInfo(accessToken, "zh_CN");
                //openId = wxMpUser.getOpenId();
                httpServletRequest.getSession().setAttribute(WeChatConstants.WX_MP_USER,wxMpUser);
                //httpServletRequest.getSession().setAttribute(WeChatConstants.WX_MP_USER_OPEN_ID, openId);

            }
            catch (Exception e)
            {
                httpServletResponse.sendRedirect(stringBuilder.toString());
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
