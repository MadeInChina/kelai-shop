package cn.com.kelaikewang.security;

import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import com.alibaba.fastjson.JSON;
import cn.com.kelaikewang.commons.web.RequestUtils;
import org.broadleafcommerce.common.security.BroadleafAuthenticationFailureHandler;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ZaoJiCMSAuthenticationFailureHandler extends BroadleafAuthenticationFailureHandler {
    public ZaoJiCMSAuthenticationFailureHandler(String defaultFailureUrl){
        super(defaultFailureUrl);
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (RequestUtils.isAjaxRequest()){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSON.toJSONString(ResponseDTO.fail("用户名或者密码错误")));
            printWriter.close();

        }else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
