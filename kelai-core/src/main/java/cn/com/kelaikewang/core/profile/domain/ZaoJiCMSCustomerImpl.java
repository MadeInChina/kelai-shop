package cn.com.kelaikewang.core.profile.domain;

import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.Index;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_CUSTOMER")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
public class ZaoJiCMSCustomerImpl extends CustomerImpl implements ZaoJiCMSCustomer {

    @Column(name = "WE_CHAT_OPEN_ID",unique = true)
    @Index(name="WE_CHAT_OPEN_ID_INDEX", columnNames={"WE_CHAT_OPEN_ID"})
    private String weChatOpenId;

    @Column(name = "SUPERIOR_ID")
    private Long superiorId;

    @Column(name = "PROFILE_PHOTO")
    private String profilePhoto;

    @Column(name = "NICKNAME")
    private String nickname;


    @Override
    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    @Override
    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }


    @Override
    public Long getSuperiorId() {
        return superiorId;
    }

    @Override
    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    @Override
    public String getProfilePhoto() {
        return profilePhoto;
    }

    @Override
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
