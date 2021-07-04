package cn.com.kelaikewang.infrastructure.encryption;

import org.broadleafcommerce.common.encryption.EncryptionModule;
import org.springframework.beans.factory.annotation.Value;

public class ZaoJiCMSPassthroughEncryptionModule implements EncryptionModule {

    @Value("${encryption.key}")
    private String encryptionKey;

    @Override
    public String encrypt(String s) {
        try {
            return AesEncryptUtils.encrypt(s,encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String decrypt(String s) {
        try {
            return AesEncryptUtils.decrypt(s,encryptionKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //Boolean matches(String raw, String encrypted)
    @Override
    public Boolean matches(String s, String s1) {
        String decrypt = this.decrypt(s1);
        return s.equals(decrypt);
    }


}
