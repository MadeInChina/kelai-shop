package cn.com.kelaikewang.core.administrativeRegion.domain;

public interface Street {
    Long getId();
    void setId(Long id);
    String getStreetCode();
    void setStreetCode(String code);
    String getName();
    void setName(String name);
    String getRegionCode();
    void setRegionCode(String regionCode);
    String getLng();
    void setLng(String lng);
    String getLat();
    void setLat(String lat);
    Integer getSort();
    void setSort(Integer sort);
}
