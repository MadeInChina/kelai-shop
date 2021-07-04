package cn.com.kelaikewang.core.administrativeRegion.domain;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_CITY")
public class CityImpl implements City{

    @Id
    @Column(name = "CITY_ID",unique = true,nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CITY_CODE")
    private String cityCode;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PROVINCE_CODE")
    private String provinceCode;
    @Column(name = "LNG")
    private String lng;
    @Column(name = "LAT")
    private String lat;
    @Column(name = "SORT")
    private Integer sort;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getProvinceCode() {
        return provinceCode;
    }

    @Override
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String getLng() {
        return lng;
    }

    @Override
    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String getLat() {
        return lat;
    }

    @Override
    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public Integer getSort() {
        return sort;
    }

    @Override
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
