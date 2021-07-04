package cn.com.kelaikewang.core.order.domain;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleImpl;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.core.order.domain.OrderItemImpl;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_ORDER_ITEM")
@PrimaryKeyJoinColumn(name = "ORDER_ITEM_ID")
public class ZaoJiCMSOrderItemImpl extends OrderItemImpl implements ZaoJiCMSOrderItem {
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ShippingPricingRuleImpl.class)
    @JoinColumn(name = "SHIPPING_PRICING_RULE_ID")
    @AdminPresentation(friendlyName = "OrderItemImpl_ShippingPricingRule",prominent = true)
    @AdminPresentationToOneLookup()
    protected ShippingPricingRule shippingPricingRule;

   /* @OneToOne(fetch = FetchType.LAZY,targetEntity = ReviewDetailImpl.class)
    @JoinColumn(name = "REVIEW_DETAIL_ID")
    protected ReviewDetail reviewDetail;*/
    @Override
    public ShippingPricingRule getShippingPricingRule() {
        return shippingPricingRule;
    }

    @Override
    public void setShippingPricingRule(ShippingPricingRule shippingPricingRule) {
        this.shippingPricingRule = shippingPricingRule;
    }

  /*  @Override
    public ReviewDetail getReviewDetail() {
        return reviewDetail;
    }

    @Override
    public void setReviewDetail(ReviewDetail reviewDetail) {
        this.reviewDetail = reviewDetail;
    }*/
}
