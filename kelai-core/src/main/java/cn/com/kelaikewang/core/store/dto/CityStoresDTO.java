package cn.com.kelaikewang.core.store.dto;

import java.util.List;

public class CityStoresDTO {
    private String city;
    private String cityCode;
    private boolean isCurrentUserCity;
    private String quanpin;
    private String jianpin;
    private List<ZaoJiCMSStoreDTO> stores;

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

    public List<ZaoJiCMSStoreDTO> getStores() {
        return stores;
    }

    public void setStores(List<ZaoJiCMSStoreDTO> stores) {
        this.stores = stores;
    }

    public boolean isCurrentUserCity() {
        return isCurrentUserCity;
    }

    public void setCurrentUserCity(boolean currentUserCity) {
        isCurrentUserCity = currentUserCity;
    }

    public String getQuanpin() {
        return quanpin;
    }

    public void setQuanpin(String quanpin) {
        this.quanpin = quanpin;
    }

    public String getJianpin() {
        return jianpin;
    }

    public void setJianpin(String jianpin) {
        this.jianpin = jianpin;
    }
}
