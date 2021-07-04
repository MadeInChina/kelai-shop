package cn.com.kelaikewang.controller.account;

import java.io.Serializable;

public class ForgotPasswordForm implements Serializable {
    private String mobile;
    private String verificationCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
