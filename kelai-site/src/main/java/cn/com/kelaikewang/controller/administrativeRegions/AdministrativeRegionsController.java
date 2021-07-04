package cn.com.kelaikewang.controller.administrativeRegions;

import cn.com.kelaikewang.core.administrativeRegion.domain.City;
import cn.com.kelaikewang.core.administrativeRegion.domain.Region;
import cn.com.kelaikewang.core.administrativeRegion.domain.Street;
import cn.com.kelaikewang.core.administrativeRegion.dto.CityDTO;
import cn.com.kelaikewang.core.administrativeRegion.dto.ProvinceDTO;
import cn.com.kelaikewang.core.administrativeRegion.dto.RegionDTO;
import cn.com.kelaikewang.core.administrativeRegion.dto.StreetDTO;
import cn.com.kelaikewang.core.administrativeRegion.service.CityService;
import cn.com.kelaikewang.core.administrativeRegion.service.RegionService;
import cn.com.kelaikewang.core.administrativeRegion.service.StreetService;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.profile.core.domain.State;
import org.broadleafcommerce.profile.core.service.StateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/administrativeRegion")
public class AdministrativeRegionsController {

    @Resource(name = "blStateService")
    private StateService stateService;

    @Resource(name = "zjcmsCityService")
    private CityService cityService;
    @Resource(name = "zjcmsRegionService")
    private RegionService regionService;
    @Resource(name = "zjcmsStreetService")
    private StreetService streetService;

    private static List<ProvinceDTO> provinceDTOS = null;
    private static final Object PROVINCE_LOCK = new Object();

    private static List<CityDTO> cityDTOS = null;
    private static final Object CITY_LOCK = new Object();

    private static List<RegionDTO> regionDTOS = null;
    private static final Object REGION_LOCK = new Object();

    private static List<StreetDTO> streetDTOS = null;
    private static final Object STREET_LOCK = new Object();

    @RequestMapping(value = "/provinces",method = RequestMethod.GET)
    @ResponseBody
    public List<ProvinceDTO> provinces(){
        if (provinceDTOS == null) {
            synchronized (PROVINCE_LOCK) {
                if (provinceDTOS == null) {
                    provinceDTOS = new ArrayList<>();
                    List<State> states = stateService.findStates("CN");

                    for (State state : states) {
                        ProvinceDTO provinceDTO = new ProvinceDTO();
                        provinceDTO.setCode(state.getAbbreviation());
                        provinceDTO.setName(state.getName());
                        provinceDTOS.add(provinceDTO);
                    }
                }
            }
        }
        return provinceDTOS;
    }
    @RequestMapping(value = "/cities",method = RequestMethod.GET)
    @ResponseBody
    public List<CityDTO> cities(HttpServletRequest request){
        final String provinceCode = request.getParameter("provinceCode");
        //List<City> cities = null;
        if (cityDTOS == null) {
            synchronized (CITY_LOCK) {
                if (cityDTOS == null) {
                    cityDTOS = new ArrayList<>();
                    List<City> cities = cityService.listAllCity();

                    cityDTOS = new ArrayList<>();
                    for (City city : cities) {

                        CityDTO cityDTO = new CityDTO();
                        cityDTO.setCode(city.getCityCode());
                        cityDTO.setName(city.getName());
                        cityDTO.setProvinceCode(city.getProvinceCode());
                        cityDTO.setSort(city.getSort());
                        cityDTOS.add(cityDTO);
                    }
                }
            }
        }
        if (StringUtils.isEmpty(provinceCode)) {
            return cityDTOS;
        }else {
            return cityDTOS.stream()
                    .filter(cityDTO -> cityDTO.getProvinceCode().equals(provinceCode))
                    .collect(Collectors.toList());
        }
    }
    @RequestMapping(value = "/regions",method = RequestMethod.GET)
    @ResponseBody
    public List<RegionDTO> regions(HttpServletRequest request){
        String cityCode = request.getParameter("cityCode");
        if (regionDTOS == null) {
            synchronized (REGION_LOCK) {
                if (regionDTOS == null) {
                    List<Region> regions = regionService.listAllRegion();

                    regionDTOS = new ArrayList<>();
                    for (Region region : regions) {
                        RegionDTO regionDTO = new RegionDTO();
                        regionDTO.setCityCode(region.getCityCode());
                        regionDTO.setCode(region.getRegionCode());
                        regionDTO.setName(region.getName());
                        regionDTO.setSort(region.getSort());
                        regionDTOS.add(regionDTO);
                    }
                }
            }
        }
        if (StringUtils.isEmpty(cityCode)){
            return regionDTOS;
        }else {
            return regionDTOS.stream()
                    .filter(region->region.getCityCode().equals(cityCode))
                    .collect(Collectors.toList());
        }

    }
    @RequestMapping(value = "/streets",method = RequestMethod.GET)
    @ResponseBody
    public List<StreetDTO> streets(HttpServletRequest request){
        String regionCode = request.getParameter("regionCode");
        if (streetDTOS == null) {
            synchronized (STREET_LOCK) {
                if (streetDTOS == null) {
                    List<Street> streets = streetService.listAllStreet();

                    streetDTOS = new ArrayList<>();
                    for (Street street : streets) {
                        StreetDTO streetDTO = new StreetDTO();
                        streetDTO.setCode(street.getStreetCode());
                        streetDTO.setName(street.getName());
                        streetDTO.setRegionCode(street.getRegionCode());
                        streetDTO.setSort(street.getSort());
                        streetDTOS.add(streetDTO);
                    }
                }
            }
        }
        if (StringUtils.isEmpty(regionCode)){
            return streetDTOS;
        }else {
            return streetDTOS.stream()
                    .filter(street->street.getRegionCode().equals(regionCode))
                    .collect(Collectors.toList());
        }

    }
}
