package cn.com.kelaikewang.vendor.paypal.service.gateway;

import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.payment.service.gateway.PayPalCheckoutWebResponseServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class ZaoJiCMSPayPalCheckoutWebResponseServiceImpl extends PayPalCheckoutWebResponseServiceImpl {
    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {
        PaymentResponseDTO paymentResponseDTO = super.translateWebResponse(request);
        paymentResponseDTO.responseMap("PAYMENT_GATEWAY_TRADE_NO",paymentResponseDTO.getResponseMap().get("PAYMENTID"));
        Long unifiedOrderId = (Long) request.getSession().getAttribute("unifiedOrderId");
        paymentResponseDTO.responseMap("UNIFIED_ORDER_ID",unifiedOrderId == null ? null : unifiedOrderId.toString());
        return paymentResponseDTO;
    }
}
