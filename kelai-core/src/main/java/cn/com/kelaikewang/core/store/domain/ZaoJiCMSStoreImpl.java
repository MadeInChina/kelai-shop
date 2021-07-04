package cn.com.kelaikewang.core.store.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_STORE")
@AdminPresentationClass(friendlyName = "ZaoJiCMSStoreImpl_Name")
public class ZaoJiCMSStoreImpl implements ZaoJiCMSStore {

    @Id
    @GeneratedValue(generator = "ZaoJiCMSStoreId")
    @GenericGenerator(
            name = "ZaoJiCMSStoreId",
            strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {@org.hibernate.annotations.Parameter(
                    name = "segment_value",
                    value = "ZaoJiCMSStoreImpl"
            ), @org.hibernate.annotations.Parameter(
                    name = "entity_name",
                    value = "cn.com.kelaikewang.core.store.domain.ZaoJiCMSStoreImpl"
            )}
    )
    @Column(name = "STORE_ID", nullable = false)
    @AdminPresentation(
            friendlyName = "StoreImpl_Store_ID",
            visibility = VisibilityEnum.HIDDEN_ALL
    )
    protected Long id;
    @Column(name = "NAME",nullable = false)
    @AdminPresentation(prominent = true,order = 1,gridOrder = 1,friendlyName = "ZaoJiCMSStoreImpl_Name")
    protected String name;

    @AdminPresentation(prominent = true,gridOrder = 2,order = 2,friendlyName = "ZaoJiCMSStoreImpl_Tel")
    @Column(name = "TEL")
    private String tel;

    @Column(name = "OPEN",nullable = false)
    @AdminPresentation(prominent = true,order = 3,gridOrder = 3,friendlyName = "ZaoJiCMSStoreImpl_Open")
    protected Boolean open;

    @Column(name = "STORE_HOURS",nullable = false)
    @AdminPresentation(prominent = true,order = 4,gridOrder = 4,friendlyName = "ZaoJiCMSStoreImpl_Store_Hours")
    protected String storeHours;

    @Column(name = "PROVINCE",nullable = false)
    @AdminPresentation(order = 5,fieldComponentRendererTemplate = "select.html",friendlyName = "ZaoJiCMSStoreImpl_Province")
    protected String province;

    @Column(name = "PROVINCE_CODE",nullable = false)
    @AdminPresentation(order = 6,fieldComponentRendererTemplate = "hidden.html")
    protected String provinceCode;

    @Column(name = "CITY",nullable = false)
    @AdminPresentation(order = 7,fieldComponentRendererTemplate = "select.html",friendlyName = "ZaoJiCMSStoreImpl_City")
    protected String city;

    @Column(name = "CITY_CODE",nullable = false)
    @AdminPresentation(order = 8,fieldComponentRendererTemplate = "hidden.html")
    protected String cityCode;

    @Column(name = "REGION",nullable = false)
    @AdminPresentation(order = 9,fieldComponentRendererTemplate = "select.html",friendlyName = "ZaoJiCMSStoreImpl_Region")
    protected String region;

    @Column(name = "REGION_CODE",nullable = false)
    @AdminPresentation(order = 10,fieldComponentRendererTemplate = "hidden.html")
    protected String regionCode;

    @Column(name = "DETAILED_ADDRESS",nullable = false)
    @AdminPresentation(order = 11,friendlyName = "ZaoJiCMSStoreImpl_DetailedAddress",fieldComponentRendererTemplate = "address-to-latitude-longitude.html")
    protected String detailedAddress;


    @Column(name = "LATITUDE",nullable = false)
    @AdminPresentation(order = 13,friendlyName = "ZaoJiCMSStoreImpl_Latitude",fieldComponentRendererTemplate = "text-readonly.html")
    protected Double latitude;

    @Column(name = "LONGITUDE",nullable = false)
    @AdminPresentation(order = 12,friendlyName = "ZaoJiCMSStoreImpl_Longitude",fieldComponentRendererTemplate = "text-readonly.html")
    protected Double longitude;



    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public String getStoreHours() {
        return storeHours;
    }

    public void setStoreHours(String storeHours) {
        this.storeHours = storeHours;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
