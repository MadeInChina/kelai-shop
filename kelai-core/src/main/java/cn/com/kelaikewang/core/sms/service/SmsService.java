package cn.com.kelaikewang.core.sms.service;


import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import cn.com.kelaikewang.core.sms.dto.SendSmsRequestDTO;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;

public interface SmsService {
    ResponseDTO send(SendSmsRequestDTO sendSmsRequestBean) throws ClientException;
    ResponseDTO sendCode(String mobile) throws ClientException;
    QuerySendDetailsResponse query(String bizId) throws ClientException;
    ResponseDTO checkCode(String mobile, String code);
    void setCodeUsed(String mobile);
}
