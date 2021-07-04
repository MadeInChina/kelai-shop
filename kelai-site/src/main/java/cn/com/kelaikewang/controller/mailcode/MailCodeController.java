package cn.com.kelaikewang.controller.mailcode;

import cn.com.kelaikewang.core.email.service.MailCodeService;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("mailcode")
public class MailCodeController {
    @Autowired
    private MailCodeService mailCodeService;

    @RequestMapping(value = "send",method = RequestMethod.POST)
    public @ResponseBody
    ResponseDTO requestCode(@RequestParam("email")String email)  {
        if(!StringUtils.isEmail(email)){
            ResponseDTO responseDTO = ResponseDTO.fail("无效的邮箱地址");
            return responseDTO;
        }

        return mailCodeService.sendCode(email);
    }
    @RequestMapping(value = "check",method = RequestMethod.POST)
    public @ResponseBody
    ResponseDTO checkCode(@RequestParam("email")String email, @RequestParam("code")String code){
        if(!StringUtils.isEmail(email)){
            ResponseDTO responseDTO = ResponseDTO.fail("无效的邮箱地址");
            return responseDTO;
        }
        if(!StringUtils.isVerificationCode(code)){
            ResponseDTO responseDTO = ResponseDTO.fail("无效的验证码");
            return responseDTO;
        }
        return mailCodeService.checkCode(email,code);
    }
}
