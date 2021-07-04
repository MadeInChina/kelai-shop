package cn.com.kelaikewang.core.email.service;


import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.integration.redis.util.RedisUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.broadleafcommerce.common.email.service.EmailService;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("zjcmsMailCodeService")
public class MailCodeServiceImpl implements MailCodeService {

    @Resource(name = "blEmailService")
    private EmailService emailService;

    @Resource(name = "blCodeEmailInfo")
    private EmailInfo emailInfo;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ResponseDTO sendCode(String email) {
        String retryKey = email + "_retry";
        Object retry =  redisUtils.get(retryKey);
        if (retry != null){
            return ResponseDTO.fail("发送请求太过频繁");
        }

        String code = RandomStringUtils.random(6,false,true);
        Map<String,Object> data = new HashMap<>();
        data.put("code",code);
        if(emailService.sendTemplateEmail(email,emailInfo,data)){
            redisUtils.set(email,code,5 * 60);
            redisUtils.set(retryKey,"",60);
            return ResponseDTO.success("验证码发送成功");
        }else {
            return ResponseDTO.fail("验证码发送失败");
        }

    }

    @Override
    public ResponseDTO checkCode(String email, String code) {
        String cacheCode = (String) redisUtils.get(email);
        if (cacheCode == null){
            return ResponseDTO.fail("验证码已过期");
        }
        if (cacheCode.equals(code)){
            return ResponseDTO.success("验证码正确");
        }else {
            return ResponseDTO.success("验证码错误");
        }
    }

    @Override
    public void setCodeUsed(String email) {
        redisUtils.del(email);
        redisUtils.del(email + "_retry");
    }
}
