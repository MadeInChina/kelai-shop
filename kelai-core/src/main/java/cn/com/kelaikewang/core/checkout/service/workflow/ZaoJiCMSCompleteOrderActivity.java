package cn.com.kelaikewang.core.checkout.service.workflow;

import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import org.broadleafcommerce.core.checkout.service.workflow.CheckoutSeed;
import org.broadleafcommerce.core.checkout.service.workflow.CompleteOrderActivity;
import org.broadleafcommerce.core.checkout.service.workflow.CompleteOrderRollbackHandler;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ZaoJiCMSCompleteOrderActivity extends CompleteOrderActivity {
    @Autowired
    public ZaoJiCMSCompleteOrderActivity(@Qualifier("blCompleteOrderRollbackHandler")CompleteOrderRollbackHandler rollbackHandler) {
        super(rollbackHandler);
    }

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        //return super.execute(context);
        CheckoutSeed seed = (CheckoutSeed)context.getSeedData();
        seed.getOrder().setStatus(this.getCompletedStatus());
        //seed.getOrder().setOrderNumber(this.determineOrderNumber(seed.getOrder()));
        //seed.getOrder().setSubmitDate(this.determineSubmitDate(seed.getOrder()));
        return context;
    }

    @Override
    protected OrderStatus getCompletedStatus() {
        return ZaoJiCMSOrderStatus.PAID;
    }
}
