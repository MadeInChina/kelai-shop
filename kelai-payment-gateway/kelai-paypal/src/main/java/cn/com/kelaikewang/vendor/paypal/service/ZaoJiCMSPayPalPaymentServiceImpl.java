package cn.com.kelaikewang.vendor.paypal.service;

import com.paypal.api.payments.Payment;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrderImpl;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.service.PaymentUnifiedOrderService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.paypal.service.PayPalPaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

public class ZaoJiCMSPayPalPaymentServiceImpl extends PayPalPaymentServiceImpl {

    @Autowired
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Override
    public Payment createPayPalPaymentForCurrentOrder(boolean performCheckoutOnReturn) throws PaymentException {

        return super.createPayPalPaymentForCurrentOrder(performCheckoutOnReturn);
    }

    @Override
    protected Payment createPayment(Payment payment, PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentUnifiedOrder unifiedOrder = new PaymentUnifiedOrderImpl();
        unifiedOrder.setAmount(new BigDecimal(paymentRequestDTO.getTransactionTotal()));
        unifiedOrder.setOrderId(Long.valueOf(paymentRequestDTO.getOrderId()));
        unifiedOrder.setPaymentType("PayPal");
        //unifiedOrder.setResponse();
        Date now = new Date();
        unifiedOrder.setSubmitedDate(now);
        //unifiedOrder.setTimeExpire(calendar.getTime());
        PaymentUnifiedOrder response = paymentUnifiedOrderService.create(unifiedOrder);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        request.getSession().setAttribute("unifiedOrderId",response.getId());

        return super.createPayment(payment, paymentRequestDTO);
    }
}
