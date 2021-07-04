package cn.com.kelaikewang.core.logistics.service;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.vendor.service.exception.FulfillmentPriceException;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.pricing.service.fulfillment.provider.FixedPriceFulfillmentPricingProvider;
import org.broadleafcommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;

import java.util.Set;


public class ZaoJiCMSFixedPriceFulfillmentPricingProvider extends FixedPriceFulfillmentPricingProvider {
    @Override
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {
        if (fulfillmentGroup.getFulfillmentOption().getName().equals("到店自提")) {
            fulfillmentGroup.setRetailFulfillmentPrice(new Money(0));
            fulfillmentGroup.setSaleFulfillmentPrice(new Money(0));
            fulfillmentGroup.setFulfillmentPrice(new Money(0));

            //FixedPriceFulfillmentPricingProvider
            return fulfillmentGroup;
        }else {
            return super.calculateCostForFulfillmentGroup(fulfillmentGroup);
        }
    }

    @Override
    public boolean canCalculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, FulfillmentOption fulfillmentOption) {
        return super.canCalculateCostForFulfillmentGroup(fulfillmentGroup,fulfillmentOption);
    }

    @Override
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> set) throws FulfillmentPriceException {
       return super.estimateCostForFulfillmentGroup(fulfillmentGroup,set);
    }
}
