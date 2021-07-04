package cn.com.kelaikewang.controller.wx;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("wx")
public class WxController {
    @Autowired
    private WxMpService wxMpService;

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    @ResponseBody
    public WxJsapiSignature wxJsSdkConfig(HttpServletRequest request, String url) {
        try {
            return wxMpService.createJsapiSignature(url);
        } catch (WxErrorException e) {
            return null;
        }
    }
}
