package cn.com.kelaikewang.core.email.service;


import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;

public interface MailCodeService{
    ResponseDTO sendCode(String email);
    ResponseDTO checkCode(String email, String code);
    void setCodeUsed(String email);
}