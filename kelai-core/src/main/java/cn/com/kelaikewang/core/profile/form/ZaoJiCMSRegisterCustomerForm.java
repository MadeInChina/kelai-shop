package cn.com.kelaikewang.core.profile.form;

import org.broadleafcommerce.profile.web.core.form.RegisterCustomerForm;

public class ZaoJiCMSRegisterCustomerForm extends RegisterCustomerForm {
    private String verificationCode;
    private String nickname;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
