package cn.com.kelaikewang.core.administrativeRegion.domain;

public interface Region {
    Long getId();
    void setId(Long id);
    String getRegionCode();
    void setRegionCode(String code);
    String getName();
    void setName(String name);
    String getCityCode();
    void setCityCode(String cityCode);
    String getLng();
    void setLng(String lng);
    String getLat();
    void setLat(String lat);
    Integer getSort();
    void setSort(Integer sort);
}
