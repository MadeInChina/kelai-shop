package cn.com.kelaikewang.core.profile.domain;

import org.broadleafcommerce.profile.core.domain.Address;

/**
 * 增加省份、城市、区县、街道代码
 */
public interface ZaoJiCMSAddress extends Address {
    String getProvinceCode();
    void setProvinceCode(String provinceCode);
    String getCityCode();
    void setCityCode(String cityCode);
    String getRegion();
    void setRegion(String region);
    String getRegionCode();
    void setRegionCode(String regionCode);
    String getStreetCommunity();
    void setStreetCommunity(String street);
    String getStreetCode();
    void setStreetCode(String streetCode);
}
