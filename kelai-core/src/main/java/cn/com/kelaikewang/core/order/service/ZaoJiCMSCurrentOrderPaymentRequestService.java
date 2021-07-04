package cn.com.kelaikewang.core.order.service;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.web.payment.service.DefaultCurrentOrderPaymentRequestService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ZaoJiCMSCurrentOrderPaymentRequestService extends DefaultCurrentOrderPaymentRequestService {
    @Override
    public PaymentRequestDTO getPaymentRequestFromCurrentOrder() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String orderIdParameter = request.getParameter("orderId");
        if (StringUtils.isEmpty(orderIdParameter)){
            return null;
        }
        long orderId = Long.parseLong(orderIdParameter);
        Order order = orderService.findOrderById(orderId);
        request.setAttribute("order",order);
        return this.paymentRequestDTOService.translateOrder(order);
    }
}
