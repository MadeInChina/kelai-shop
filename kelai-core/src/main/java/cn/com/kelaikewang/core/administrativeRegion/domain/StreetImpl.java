package cn.com.kelaikewang.core.administrativeRegion.domain;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_STREET")
public class StreetImpl implements Street{
    @Id
    @Column(name = "STREET_ID",unique = true,nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "STREET_CODE")
    private String streetCode;
    @Column(name = "NAME")
    private String name;
    @Column(name = "REGION_CODE")
    private String regionCode;
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

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
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
    public String getRegionCode() {
        return regionCode;
    }

    @Override
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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
