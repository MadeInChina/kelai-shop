package cn.com.kelaikewang.core.logistics.dto;

import java.io.Serializable;

public class CityDTO implements Serializable {
    private String cityName;
    private String cityCode;
    private boolean selected;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
