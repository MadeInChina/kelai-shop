package org.broadleafcommerce.common.web.payment.controller;

import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.core.order.domain.Order;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public abstract class ZaoJiCMSPaymentGatewayAbstractController extends PaymentGatewayAbstractController {

    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService nextShopOrderService;

    @Override
    public String process(Model model, HttpServletRequest request,
                          final RedirectAttributes redirectAttributes) throws PaymentException {
        try {
            PaymentResponseDTO responseDTO = getWebResponseService().translateWebResponse(request);
            return processApplyPaymentToOrder(model,request,redirectAttributes,responseDTO);
        }catch (Exception e){
            return getErrorViewRedirect();
        }
    }

    protected void saveOrderShippingAddress(long orderId,long addressId){
        nextShopOrderService.saveOrderShippingAddress(addressId,orderId);
    }

    protected String processApplyPaymentToOrder(Model model, HttpServletRequest request,
                                              final RedirectAttributes redirectAttributes,
                                              PaymentResponseDTO responseDTO) throws PaymentException {
        Long orderPaymentId = null;

        try {
            //PaymentResponseDTO responseDTO = getWebResponseService().translateWebResponse(request);
            String orderId = responseDTO.getOrderId();
            if (orderId == null) {
                throw new RuntimeException("Order ID must be set on the Payment Response DTO");
            }
            Order order = nextShopOrderService.findOrderById(Long.parseLong(orderId));
            if (order == null){
                LOG.info("找不到订单,orderId=" + orderId);
                return getErrorViewRedirect();
            }
            if (ZaoJiCMSOrderStatus.PAID.getType().equals(order.getStatus().getType())){
                LOG.info("订单已处理,无需重复处理，orderId=" + orderId);
                return getConfirmationViewRedirect(order.getOrderNumber());
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("HTTPRequest translated to Raw Response: " +  responseDTO.getRawResponse());
            }

            orderPaymentId = applyPaymentToOrder(responseDTO);

            if (!responseDTO.isSuccessful()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("The Response DTO is marked as unsuccessful. Delegating to the " +
                            "payment module to handle an unsuccessful transaction");
                }

                handleUnsuccessfulTransaction(model, redirectAttributes, responseDTO);
                return getErrorViewRedirect();
            }

            if (!responseDTO.isValid()) {
                throw new PaymentException("The validity of the response cannot be confirmed." +
                        "Check the Tamper Proof Seal for more details.");
            }

            //String orderId = responseDTO.getOrderId();


            if (responseDTO.isCompleteCheckoutOnCallback()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("The Response DTO for this Gateway is configured to complete checkout on callback. " +
                            "Initiating Checkout with Order ID: " + orderId);
                }

                String orderNumber = initiateCheckout(Long.parseLong(orderId));
                return getConfirmationViewRedirect(orderNumber);
            } else {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("The Gateway is configured to not complete checkout. " +
                            "Redirecting to the Order Review Page for Order ID: " + orderId);
                }

                return getOrderReviewRedirect();
            }

        } catch (Exception e) {

            if (LOG.isTraceEnabled()) {
                LOG.trace("HTTPRequest - " + webResponsePrintService.printRequest(request));
            }

            if (LOG.isErrorEnabled()) {
                LOG.error("An exception was caught either from processing the response and applying the payment to " +
                        "the order, or an activity in the checkout workflow threw an exception. Attempting to " +
                        "mark the payment as invalid and delegating to the payment module to handle any other " +
                        "exception processing", e);
            }

            if (paymentGatewayCheckoutService != null && orderPaymentId != null) {
                paymentGatewayCheckoutService.markPaymentAsInvalid(orderPaymentId);
            }

            handleProcessingException(e, redirectAttributes);
        }

        return getErrorViewRedirect();
    }

}
