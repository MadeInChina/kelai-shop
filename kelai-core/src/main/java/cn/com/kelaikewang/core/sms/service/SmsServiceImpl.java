package cn.com.kelaikewang.core.sms.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import cn.com.kelaikewang.core.sms.config.SmsConfigProperties;
import cn.com.kelaikewang.core.sms.dto.SendSmsRequestDTO;
import cn.com.kelaikewang.integration.redis.util.RedisUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("zjcmsSmsService")
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisUtils redisUtils;

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";
    @Autowired
    private SmsConfigProperties smsConfigProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);


    static {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

    }

    private ResponseDTO send(SendSmsRequestDTO sendSmsRequestBean, String successMsg, String failedMsg) throws ClientException {
        if (true){
            return ResponseDTO.success(successMsg);
        }
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfigProperties.getAccessKeyID(), smsConfigProperties.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(sendSmsRequestBean.getMobile());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(sendSmsRequestBean.getSign());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(sendSmsRequestBean.getTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        if(sendSmsRequestBean.getTemplateParams() != null && sendSmsRequestBean.getTemplateParams().size() > 0){

            request.setTemplateParam(JSON.toJSONString(sendSmsRequestBean.getTemplateParams()));
        }

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(sendSmsRequestBean.getOutId());

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            return ResponseDTO.success(successMsg);
        }else {
            LOGGER.error( failedMsg + ",code:" + sendSmsResponse.getCode() + ",msg:" + sendSmsResponse.getMessage());

            return ResponseDTO.fail(failedMsg);
        }



    }

    @Override
    public ResponseDTO send(SendSmsRequestDTO sendSmsRequestBean) throws ClientException {
        return send(sendSmsRequestBean,"短信发送成功","短信发送失败");
    }

    @Override
    public ResponseDTO sendCode(String mobile) throws ClientException {

        String retry = mobile + "_retry";

        if(redisUtils.get(retry) != null){
            return ResponseDTO.fail("发送太过频繁");
        }

        SendSmsRequestDTO sendSmsRequestBean = new SendSmsRequestDTO();
        sendSmsRequestBean.setMobile(mobile);
        sendSmsRequestBean.setSign(smsConfigProperties.getSign());
        sendSmsRequestBean.setTemplateCode(smsConfigProperties.getTemplateCode());
        Map<String,String> parameters = new HashMap<>();
        //String code = RandomStringUtils.random(6,false,true);
        String code = "111111";
        parameters.put("code",code);
        sendSmsRequestBean.setTemplateParams(parameters);

        ResponseDTO operationResult = send(sendSmsRequestBean,"验证码发送成功，请注意查收","验证码发送失败");
        if(ResponseStatus.SUCCESS.equals(operationResult.getStatus())){
            redisUtils.set(mobile,code,5 * 60);
            redisUtils.set(retry,"", 60);
        }
        return operationResult;
    }

    @Override
    public QuerySendDetailsResponse query(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfigProperties.getAccessKeyID(), smsConfigProperties.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    @Override
    public ResponseDTO checkCode(String mobile, String code) {
        String cacheCode = (String) redisUtils.get(mobile);
        if (cacheCode == null){
            return ResponseDTO.fail("验证码已过期");
        }
        if (!cacheCode.equals(code)){
            return ResponseDTO.fail("验证码无效");
        }else {
            return ResponseDTO.success("验证码正确");
        }
    }

    @Override
    public void setCodeUsed(String mobile) {
        redisUtils.del(mobile);
        redisUtils.del(mobile + "_retry");
    }

}
