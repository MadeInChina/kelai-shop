package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.presentation.AdminPresentationClass;

@AdminPresentationClass(friendlyName = "ShippingPricingRuleImpl_Name")
public interface ShippingRuleAdminPresentation {
    public static class GroupName {
        public static final String DefaultShippingPrice = "默认运费(除指定地区外，其余地区的运费采用默认运费)";
    }
}
