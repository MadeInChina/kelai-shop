package cn.com.kelaikewang.controller.account;

import org.broadleafcommerce.core.web.controller.account.UpdateAccountForm;

import java.io.Serializable;

public class ZaoJiCMSUpdateAccountForm extends UpdateAccountForm implements Serializable {

    private String nickname;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
