package cn.com.kelaikewang.controller.checkout.stage;

import org.broadleafcommerce.core.web.checkout.stage.CheckoutStageType;
import org.springframework.stereotype.Component;

@Component
public class ZaoJiCMSCheckoutStageType extends CheckoutStageType {
    public static CheckoutStageType SUBMIT_ORDER = new CheckoutStageType("SUBMIT_ORDER", "提交订单", 1500);
    public ZaoJiCMSCheckoutStageType(){

    }
}
