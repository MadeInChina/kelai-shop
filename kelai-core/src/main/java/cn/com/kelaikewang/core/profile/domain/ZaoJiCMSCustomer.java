package cn.com.kelaikewang.core.profile.domain;

import org.broadleafcommerce.profile.core.domain.Customer;

public interface ZaoJiCMSCustomer extends Customer{
    String getWeChatOpenId();
    void setWeChatOpenId(String weChatOpenId);

    Long getSuperiorId();
    void setSuperiorId(Long superiorId);

    String getProfilePhoto();
    void setProfilePhoto(String profilePhoto);

    String getNickname();
    void setNickname(String nickname);
}
