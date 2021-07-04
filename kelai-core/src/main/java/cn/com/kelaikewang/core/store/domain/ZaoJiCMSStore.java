package cn.com.kelaikewang.core.store.domain;

import java.io.Serializable;

public interface ZaoJiCMSStore extends Serializable {
    String getProvince();

    void setProvince(String province);

    String getProvinceCode();

    void setProvinceCode(String provinceCode);

    String getCity();

    void setCity(String city);

    String getCityCode();

    void setCityCode(String cityCode);

    String getRegion();

    void setRegion(String region);

    String getRegionCode();

    void setRegionCode(String regionCode);

    String getDetailedAddress();

    void setDetailedAddress(String detailedAddress);
    String getTel();

    void setTel(String tel);

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Boolean getOpen();

    void setOpen(Boolean open);

    String getStoreHours();

    void setStoreHours(String storeHours);

    Double getLatitude();

    void setLatitude(Double latitude);

    Double getLongitude();

    void setLongitude(Double longitude);
}
