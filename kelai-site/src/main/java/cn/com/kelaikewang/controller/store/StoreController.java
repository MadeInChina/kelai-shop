package cn.com.kelaikewang.controller.store;

import cn.com.kelaikewang.commons.geography.GeographyUtils;
import cn.com.kelaikewang.core.store.dto.CityStoresDTO;
import cn.com.kelaikewang.core.store.dto.ZaoJiCMSStoreDTO;
import cn.com.kelaikewang.core.store.service.ZaoJiCMSStoreService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/store")
public class StoreController {

    protected static final Log LOG = LogFactory.getLog(StoreController.class);

    @Resource(name = "zjcmsStoreService")
    protected ZaoJiCMSStoreService nextShopStoreService;

    /**
     * 根据ip获取附近门店
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "nearbyStoresByCurrentUserCity",method = RequestMethod.GET)
    public List<CityStoresDTO> allCityStories(HttpServletRequest request)  {
        //淘宝根据ip获取城市
        //http://ip.taobao.com/service/getIpInfo.php?ip=119.139.196.127
        //String ip = RequestUtils.getIpAddr(request);
        List<CityStoresDTO> allCityStores = nextShopStoreService.readAllCityStores();
        try {
            //String json = HttpUtils.get("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip, null);
            //JSONObject jsonObject = new JSONObject(json);
            //JSONObject data = jsonObject.getJSONObject("data");
            String city = request.getParameter("city");
            for (CityStoresDTO cityStoresDTO : allCityStores){
                if (cityStoresDTO.getCity().equals(city) || cityStoresDTO.getCity().contains(city)){
                    cityStoresDTO.setCurrentUserCity(true);
                    break;
                }
            }

        }catch (Exception e){
            LOG.error("根据ip获取城市失败",e);
        }
        return allCityStores;

    }

    /**
     * 根据经纬度获取附近门店
     * @param longitude
     * @param latitude
     * @param city
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "nearbyStoresByCurrentUserLngLat",method = RequestMethod.GET)
    public List<CityStoresDTO> allCityStories(@RequestParam("longitude")double longitude,
                                              @RequestParam("latitude")double latitude,
                                              @RequestParam("city")String city){
        List<CityStoresDTO> allCityStores = nextShopStoreService.readAllCityStores();

        for (CityStoresDTO cityStoresDTO : allCityStores){
            if (cityStoresDTO.getCity().equals(city) || cityStoresDTO.getCity().contains(city)){
                cityStoresDTO.setCurrentUserCity(true);

                List<ZaoJiCMSStoreDTO> storeDTOS = cityStoresDTO.getStores();
                for (ZaoJiCMSStoreDTO storeDTO : storeDTOS){
                    //WGS84:World Geodetic System 1984，是为GPS全球定位系统使用而建立的坐标系统
                    double distance = GeographyUtils.computeDistance(new GlobalCoordinates(latitude,longitude),
                            new GlobalCoordinates(storeDTO.getLatitude(),storeDTO.getLongitude()),
                            Ellipsoid.WGS84);
                    storeDTO.setDistance(distance);
                    //Math.round( x / 100d) / 10d ); //输出0.8
                    if (distance >= 1000){
                        storeDTO.setDistanceText( Math.round(distance / 100d / 10d) + "公里");
                    }else {
                        storeDTO.setDistanceText(Math.round(distance) + "米");
                    }
                }

                break;
            }
        }


        return allCityStores;
    }
}
