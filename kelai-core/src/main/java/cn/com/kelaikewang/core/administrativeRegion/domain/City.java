package cn.com.kelaikewang.core.administrativeRegion.domain;


public interface City {
    Long getId();
    void setId(Long id);
    String getCityCode();
    void setCityCode(String code);
    String getName();
    void setName(String name);
    String getProvinceCode();
    void setProvinceCode(String provinceCode);
    String getLng();
    void setLng(String lng);
    String getLat();
    void setLat(String lat);
    Integer getSort();
    void setSort(Integer sort);
}
