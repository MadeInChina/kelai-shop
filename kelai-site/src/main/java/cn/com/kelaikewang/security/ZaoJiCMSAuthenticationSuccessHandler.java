package cn.com.kelaikewang.security;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.integration.wechat.constant.WeChatConstants;
import com.alibaba.fastjson.JSON;
import cn.com.kelaikewang.commons.web.RequestUtils;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.broadleafcommerce.core.web.order.security.BroadleafAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ZaoJiCMSAuthenticationSuccessHandler extends BroadleafAuthenticationSuccessHandler {
    private ZaoJiCMSCustomerService nsCustomerService;

    public void setNsCustomerService(ZaoJiCMSCustomerService nsCustomerService) {
        this.nsCustomerService = nsCustomerService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        if (RequestUtils.isWeChatEnv()) {
            WxMpUser wxMpUser = (WxMpUser)request.getSession().getAttribute(WeChatConstants.WX_MP_USER);
            if (wxMpUser != null) {
                nsCustomerService.saveCustomerWeChatInfo(wxMpUser.getOpenId(), authentication.getName());
            }
        }
        if (RequestUtils.isAjaxRequest()){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSON.toJSONString(ResponseDTO.success("登录成功")));
            printWriter.close();
        }else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
