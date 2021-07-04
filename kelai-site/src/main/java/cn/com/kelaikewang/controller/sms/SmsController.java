package cn.com.kelaikewang.controller.sms;

import cn.com.kelaikewang.commons.lang.StringUtils;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import com.aliyuncs.exceptions.ClientException;
import cn.com.kelaikewang.core.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("sms")
public class SmsController {
    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "send-code",method = RequestMethod.POST)
    public @ResponseBody
    ResponseDTO requestCode(@RequestParam("mobile")String mobile) throws ClientException {
        if(!StringUtils.isMobile(mobile)){
            return ResponseDTO.fail("无效的手机号");
        }

        return smsService.sendCode(mobile);
    }
    @RequestMapping(value = "check-code",method = RequestMethod.POST)
    public @ResponseBody
    ResponseDTO checkCode(@RequestParam("mobile")String mobile, @RequestParam("code")String code){
        if(!StringUtils.isMobile(mobile)){
            return ResponseDTO.fail("无效的手机号");
        }
        if(!StringUtils.isVerificationCode(code)){
            return ResponseDTO.fail("无效的验证码");
        }
        return smsService.checkCode(mobile,code);
    }
}
