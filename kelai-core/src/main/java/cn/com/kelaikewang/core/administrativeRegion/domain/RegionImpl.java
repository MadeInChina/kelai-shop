package cn.com.kelaikewang.core.administrativeRegion.domain;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_REGION")
public class RegionImpl implements Region{
    @Id
    @Column(name = "REGION_ID",unique = true,nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "REGION_CODE")
    private String regionCode;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CITY_CODE")
    private String cityCode;
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

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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
    public String getCityCode() {
        return cityCode;
    }

    @Override
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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
