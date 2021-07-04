package cn.com.kelaikewang.integration.wechat.controller;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("wx")
public class WxMpApiController {
    @Resource(name = "wxMpService")
    protected WxMpService wxMpService;

    @RequestMapping("jsapiSignature")
    @ResponseBody
    public WxJsapiSignature createJsapiSignature(@RequestParam("url") String url) throws WxErrorException {
        return wxMpService.createJsapiSignature(url);
    }
}
