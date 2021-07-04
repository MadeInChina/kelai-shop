package cn.com.kelaikewang.core.order.domain;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import org.broadleafcommerce.core.order.domain.OrderItem;

public interface ZaoJiCMSOrderItem extends OrderItem {
    ShippingPricingRule getShippingPricingRule();
    void setShippingPricingRule(ShippingPricingRule shippingPricingRule);
    /*ReviewDetail getReviewDetail();
    void setReviewDetail(ReviewDetail reviewDetail);*/
}
