package cn.com.kelaikewang.core.logistics.service;

import cn.com.kelaikewang.core.logistics.service.type.ShippingPricingType;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderItemService;
import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProduct;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleItem;
import cn.com.kelaikewang.core.logistics.domain.ShippingTemplate;
import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddress;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.vendor.service.exception.FulfillmentPriceException;
import org.broadleafcommerce.core.catalog.domain.Dimension;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.order.domain.*;
import org.broadleafcommerce.core.pricing.service.fulfillment.provider.BandedFulfillmentPricingProvider;
import org.broadleafcommerce.core.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public class ZaoJiCMSBandedFulfillmentPricingProvider extends BandedFulfillmentPricingProvider {
    @Resource(name = "blOrderItemService")
    private ZaoJiCMSOrderItemService nextShopOrderItemService;

    @Override
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {
        if(fulfillmentGroup.getFulfillmentOption().getName().equals("官方配送")) {
            //Order order = fulfillmentGroup.getOrder();
            ZaoJiCMSAddress address = (ZaoJiCMSAddress) fulfillmentGroup.getAddress();
            List<FulfillmentGroupItem> fulfillmentGroupItems = fulfillmentGroup.getFulfillmentGroupItems();
            BigDecimal amount = BigDecimal.ZERO;
            for (FulfillmentGroupItem groupItem: fulfillmentGroupItems){
                OrderItem orderItem = groupItem.getOrderItem();
                if (orderItem instanceof DiscreteOrderItem){
                    DiscreteOrderItem discreteOrderItem = (DiscreteOrderItem)orderItem;
                    Product product = discreteOrderItem.getProduct();
                    if (product instanceof ZaoJiCMSProduct){
                        ShippingTemplate shippingTemplate =((ZaoJiCMSProduct) product).getShippingTemplate();
                        amount = amount.add(calculateCostForOrderItem(discreteOrderItem,shippingTemplate,address,product));
                    }
                }else if(orderItem instanceof BundleOrderItem){
                    BundleOrderItem bundleOrderItem = (BundleOrderItem)orderItem;
                    List<DiscreteOrderItem> discreteOrderItems = bundleOrderItem.getDiscreteOrderItems();
                    for (DiscreteOrderItem item : discreteOrderItems){
                        Product product = item.getProduct();
                        if (product instanceof ZaoJiCMSProduct){
                            ShippingTemplate shippingTemplate =((ZaoJiCMSProduct) product).getShippingTemplate();
                            amount = amount.add(calculateCostForOrderItem(item,shippingTemplate,address,product));
                        }
                    }
                }else {
                    throw new RuntimeException("无法处理计算未知订单项的运费");
                }
            }
            fulfillmentGroup.setRetailFulfillmentPrice(new Money(amount));
            fulfillmentGroup.setSaleFulfillmentPrice(new Money(amount));
            fulfillmentGroup.setFulfillmentPrice(new Money(amount));
            return fulfillmentGroup;
        }else {
            return super.calculateCostForFulfillmentGroup(fulfillmentGroup);
        }
    }
    private BigDecimal calculateCostForOrderItem(OrderItem orderItem,ShippingTemplate shippingTemplate,ZaoJiCMSAddress address,Product product){
        if (shippingTemplate.getFreeShipping()){
            return BigDecimal.ZERO;
        }else {
            List<ShippingPricingRule> shippingPricingRules = shippingTemplate.getShippingPricingRules();
            if (shippingPricingRules.size() == 0){
                throw new RuntimeException("运费模板缺少运送方式的配置,shippingTemplateId=" + shippingTemplate.getId());
            }
            ShippingPricingRule shippingPricingRule = nextShopOrderItemService.getOrderItemShippingPricingRule(orderItem.getId());
            if (shippingPricingRule == null){
                //return BigDecimal.ZERO;
                throw new RuntimeException("订单项缺少对应的ShippingPricingRule,orderItemId=" + orderItem.getId());
            }
            String toCityCode = address.getCityCode();
            //默认选择第一种运输方式，后面还要升级购物车让客户可以自由选择物流
            //ShippingPricingRule shippingPricingRule = shippingPricingRules.get(0);
            int orderItemQuantity = orderItem.getQuantity();
            if (shippingTemplate.getPricingType().equals(ShippingPricingType.NUMBER.getType())){
                return calculateCostForRule(shippingPricingRule,orderItemQuantity,toCityCode);

            }else if (shippingTemplate.getPricingType().equals(ShippingPricingType.VOLUME.getType())){
                Dimension dimension = product.getDimension();
                //总体积
                BigDecimal volume = dimension.getWidth().multiply(dimension.getHeight()).multiply(dimension.getDepth()).multiply(new BigDecimal(orderItemQuantity));
                return calculateCostForRule(shippingPricingRule,volume.doubleValue(),toCityCode);

            }else if(shippingTemplate.getPricingType().equals(ShippingPricingType.WEIGHT.getType())){
                //总重量
                BigDecimal weight = product.getWeight().getWeight().multiply(new BigDecimal(orderItemQuantity));
                return calculateCostForRule(shippingPricingRule,weight.doubleValue(),toCityCode);
            }else {
                throw new RuntimeException("未知的运费模板计价方式");
            }
        }
    }
    private BigDecimal calculateCostForRule(ShippingPricingRule shippingPricingRule,double orderItemQuantityOrVolumeOrWeight,String toCityCode) {
        //如果没有地区配置，则使用默认配置
        if (shippingPricingRule.getShippingPricingRuleItems() == null || shippingPricingRule.getShippingPricingRuleItems().size() == 0){

                    /*if (orderItemQuantity <= shippingPricingRule.getDefaultQuantity()){
                        return shippingPricingRule.getDefaultPrice();
                    }else {
                        //计算首费加增加部分的费用总和
                        BigDecimal defaultPrice = shippingPricingRule.getDefaultPrice();
                        //增加的倍数
                        Double m = Math.ceil((orderItemQuantity - shippingPricingRule.getDefaultQuantity()) / shippingPricingRule.getIncrement());
                        BigDecimal increment = shippingPricingRule.getIncrementPrice().multiply(new BigDecimal(m));
                        return defaultPrice.add(increment);
                    }*/
            return calculateCost(orderItemQuantityOrVolumeOrWeight,
                    shippingPricingRule.getDefaultQuantity(),
                    shippingPricingRule.getDefaultPrice(),
                    shippingPricingRule.getIncrement(),
                    shippingPricingRule.getIncrementPrice());
        }else {
            //如果有地区配置，看看能否匹配上
            ShippingPricingRuleItem matchingShippingPricingRuleItem = null;
            for (ShippingPricingRuleItem ruleItem : shippingPricingRule.getShippingPricingRuleItems()){
                String[] cityCodes = ruleItem.getSelectedRegionIds().split(",");
                for (String cityCode : cityCodes){
                    if (cityCode.equals(toCityCode)){
                        matchingShippingPricingRuleItem = ruleItem;
                        break;
                    }
                }
                if (matchingShippingPricingRuleItem != null){
                    break;
                }
            }
            //如果没有找到匹配的地区配置则使用默认的配置，否则使用匹配的地区运费规则
            if (matchingShippingPricingRuleItem == null){
                return calculateCost(orderItemQuantityOrVolumeOrWeight,
                        shippingPricingRule.getDefaultQuantity(),
                        shippingPricingRule.getDefaultPrice(),
                        shippingPricingRule.getIncrement(),
                        shippingPricingRule.getIncrementPrice());
            }else {
                return calculateCost(orderItemQuantityOrVolumeOrWeight,
                        matchingShippingPricingRuleItem.getBottomQuantity(),
                        matchingShippingPricingRuleItem.getBottomPrice(),
                        matchingShippingPricingRuleItem.getIncrement(),
                        matchingShippingPricingRuleItem.getIncrementPrice());
            }
        }
    }
    private BigDecimal calculateCost(double orderItemQuantityOrVolumeOrWeight,double defaultQuantity,BigDecimal defaultPrice,double increment,BigDecimal incrementPrice){
        //如果在收费范围内，返回首费
        if (orderItemQuantityOrVolumeOrWeight <= defaultQuantity){
            return defaultPrice;
        }else {
            //计算首费加增加部分的费用总和
            //BigDecimal defaultPrice = shippingPricingRule.getDefaultPrice();
            //增加的倍数
            double m = Math.ceil((orderItemQuantityOrVolumeOrWeight - defaultQuantity) / increment);
            BigDecimal incrementCost = incrementPrice.multiply(new BigDecimal(m));
            return defaultPrice.add(incrementCost);
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
