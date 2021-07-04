package cn.com.kelaikewang.core.order.service;

import org.broadleafcommerce.common.config.service.SystemPropertiesService;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.order.service.FulfillmentOptionServiceImpl;

import javax.annotation.Resource;
import java.util.List;

public class ZaoJiCMSFulfillmentOptionServiceImpl extends FulfillmentOptionServiceImpl {

    @Resource(name = "blSystemPropertiesService")
    protected SystemPropertiesService systemPropertiesService;

    /**
     * 如果不允许自提，则从列表里把自提删除
     * @return
     */
    @Override
    public List<FulfillmentOption> readAllFulfillmentOptions() {
        List<FulfillmentOption> fulfillmentOptions = super.readAllFulfillmentOptions();
        boolean enableInStorePickup = systemPropertiesService.resolveBooleanSystemProperty("enableInStorePickup");
        if (!enableInStorePickup){
            for (FulfillmentOption fulfillmentOption : fulfillmentOptions){
                if (fulfillmentOption.getName().equals("门店自提")){
                    fulfillmentOptions.remove(fulfillmentOption);
                    break;
                }
            }
        }
        return fulfillmentOptions;
    }
}
