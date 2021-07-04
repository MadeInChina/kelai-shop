package cn.com.kelaikewang.core.order.service;

import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.payment.service.OrderToPaymentRequestDTOServiceImpl;
import org.broadleafcommerce.profile.core.domain.Address;

public class ZaoJiCMSOrderToPaymentRequestDTOServiceImpl extends OrderToPaymentRequestDTOServiceImpl {
    @Override
    public void populateShipTo(Order order, PaymentRequestDTO requestDTO) {
        super.populateShipTo(order, requestDTO);

        FulfillmentGroup defaultFg = this.fgService.getFirstShippableFulfillmentGroup(order);
        if (defaultFg != null && defaultFg.getAddress() != null) {
            Address fgAddress = defaultFg.getAddress();
            requestDTO.additionalField("ADDRESS_ID",fgAddress.getId());
        }

    }
}
