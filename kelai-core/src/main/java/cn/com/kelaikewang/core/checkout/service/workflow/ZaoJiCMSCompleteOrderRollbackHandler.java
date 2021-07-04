package cn.com.kelaikewang.core.checkout.service.workflow;

import org.broadleafcommerce.core.checkout.service.workflow.CheckoutSeed;
import org.broadleafcommerce.core.checkout.service.workflow.CompleteOrderRollbackHandler;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.workflow.Activity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.core.workflow.state.RollbackFailureException;
import org.broadleafcommerce.core.workflow.state.RollbackHandler;

import java.util.Date;
import java.util.Map;

public class ZaoJiCMSCompleteOrderRollbackHandler extends CompleteOrderRollbackHandler {
    @Override
    public void rollbackState(Activity<ProcessContext<CheckoutSeed>> activity, ProcessContext<CheckoutSeed> checkoutSeedProcessContext, Map<String, Object> map) throws RollbackFailureException {
        CheckoutSeed seed = checkoutSeedProcessContext.getSeedData();
        seed.getOrder().setStatus(OrderStatus.SUBMITTED);
    }
}
