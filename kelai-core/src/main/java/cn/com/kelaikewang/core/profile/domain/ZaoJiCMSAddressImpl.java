package cn.com.kelaikewang.core.profile.domain;

import org.broadleafcommerce.profile.core.domain.AddressImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ZJCMS_ADDRESS")
@PrimaryKeyJoinColumn(name = "ADDRESS_ID")
public class ZaoJiCMSAddressImpl extends AddressImpl implements ZaoJiCMSAddress {

    @Column(name = "PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "CITY_CODE")
    private String cityCode;
    @Column(name = "REGION")
    private String region;
    @Column(name = "REGION_CODE")
    private String regionCode;
    @Column(name = "STREET_COMMUNITY")
    private String streetCommunity;
    @Column(name = "STREET_CODE")
    private String streetCode;

    @Override
    public String getProvinceCode() {
        return provinceCode;
    }

    @Override
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
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
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getRegionCode() {
        return regionCode;
    }

    @Override
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getStreetCommunity() {
        return streetCommunity;
    }

    public void setStreetCommunity(String streetCommunity) {
        this.streetCommunity = streetCommunity;
    }

    @Override
    public String getStreetCode() {
        return streetCode;
    }

    @Override
    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }


}
