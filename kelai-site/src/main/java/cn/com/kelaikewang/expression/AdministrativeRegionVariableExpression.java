package cn.com.kelaikewang.expression;

import cn.com.kelaikewang.core.administrativeRegion.domain.City;
import cn.com.kelaikewang.core.administrativeRegion.domain.Region;
import cn.com.kelaikewang.core.administrativeRegion.domain.Street;
import cn.com.kelaikewang.core.administrativeRegion.service.CityService;
import cn.com.kelaikewang.core.administrativeRegion.service.RegionService;
import cn.com.kelaikewang.core.administrativeRegion.service.StreetService;
import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.broadleafcommerce.profile.core.domain.State;
import org.broadleafcommerce.profile.core.service.StateService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("zjcmsProvinceVariableExpression")
@ConditionalOnTemplating
public class AdministrativeRegionVariableExpression implements BroadleafVariableExpression {

    @Resource(name = "blStateService")
    private StateService stateService;

    @Resource(name = "zjcmsCityService")
    private CityService cityService;

    @Resource(name = "zjcmsRegionService")
    private RegionService regionService;

    @Resource(name = "zjcmsStreetService")
    private StreetService streetService;

    @Override
    public String getName() {
        return "zjcmsAdministrativeRegion";
    }

    public List<State> getProvinces(){
        List<State> states = stateService.findStates("CN");
        return states;
    }
    public List<City> getCities(String provinceCode){
        return cityService.listCityByProvinceCode(provinceCode);
    }
    public List<Region> getRegions(String cityCode){
        return regionService.listRegionByCityCode(cityCode);
    }
    public List<Street> getStreets(String regionCode){
        return streetService.listStreetByRegionCode(regionCode);
    }

}
