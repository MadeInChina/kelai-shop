package cn.com.kelaikewang.core.logistics.dto;

import java.io.Serializable;
import java.util.List;

public class BigRegionDTO implements Serializable {
    private String bigRegionName;
    private String bigRegionCode;
    //private String checkState;
    private List<ProvinceDTO> provinces;

    public String getBigRegionName() {
        return bigRegionName;
    }

    public void setBigRegionName(String bigRegionName) {
        this.bigRegionName = bigRegionName;
    }

    public String getBigRegionCode() {
        return bigRegionCode;
    }

    public void setBigRegionCode(String bigRegionCode) {
        this.bigRegionCode = bigRegionCode;
    }

    /*public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }*/

    public List<ProvinceDTO> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceDTO> provinces) {
        this.provinces = provinces;
    }
}
