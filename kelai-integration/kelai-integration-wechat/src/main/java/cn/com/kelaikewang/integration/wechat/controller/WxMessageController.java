package cn.com.kelaikewang.integration.wechat.controller;

import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("wxMessage")
public class WxMessageController {

    @Autowired
    private  WxMpService wxService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WxMessageController.class);

    @RequestMapping(value = "echo",method = RequestMethod.GET)
    public void echo(@RequestParam(value = "signature",required = false) String signature,
                     @RequestParam(value = "timestamp",required = false) String timestamp,
                     @RequestParam(value = "nonce",required = false) String nonce,
                     @RequestParam(value = "echostr",required = false) String echostr,
                     HttpServletResponse response) throws IOException {

        LOGGER.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            printWriter.write(echostr);
            printWriter.close();
            return;
        }

        printWriter.write("非法请求");
        printWriter.close();
    }
}
