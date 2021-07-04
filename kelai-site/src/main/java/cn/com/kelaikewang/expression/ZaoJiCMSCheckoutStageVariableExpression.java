package cn.com.kelaikewang.expression;

import cn.com.kelaikewang.controller.checkout.stage.ZaoJiCMSCheckoutStageType;
import org.broadleafcommerce.core.web.checkout.stage.CheckoutStageType;
import org.broadleafcommerce.core.web.expression.checkout.CheckoutStageVariableExpression;

public class ZaoJiCMSCheckoutStageVariableExpression extends CheckoutStageVariableExpression {
    public boolean isPreviousStage(String currentStage, String activeStage) {
        CheckoutStageType currentStageType = ZaoJiCMSCheckoutStageType.getInstance(currentStage);
        CheckoutStageType activeStageType = ZaoJiCMSCheckoutStageType.getInstance(activeStage);

        return currentStageType.compareTo(activeStageType) < 0;
    }

    public boolean isActiveStage(String currentStage, String activeStage) {
        CheckoutStageType currentStageType = ZaoJiCMSCheckoutStageType.getInstance(currentStage);
        CheckoutStageType activeStageType = ZaoJiCMSCheckoutStageType.getInstance(activeStage);

        return currentStageType.compareTo(activeStageType) == 0;
    }

    public boolean isLaterStage(String currentStage, String activeStage) {
        CheckoutStageType currentStageType = ZaoJiCMSCheckoutStageType.getInstance(currentStage);
        CheckoutStageType activeStageType = ZaoJiCMSCheckoutStageType.getInstance(activeStage);

        return currentStageType.compareTo(activeStageType) > 0;
    }
}
