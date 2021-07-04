package cn.com.kelaikewang.core.payment.service;


import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import cn.com.kelaikewang.core.order.service.type.OrderWriteOffStatus;
import cn.com.kelaikewang.core.payment.domain.ZaoJiCMSOrderPayment;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerServiceImpl;

import cn.com.kelaikewang.infrastructure.id.factory.IdFactory;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.payment.PaymentAdditionalFieldType;
import org.broadleafcommerce.common.payment.PaymentGatewayType;
import org.broadleafcommerce.common.payment.dto.GatewayCustomerDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentOption;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.payment.domain.OrderPayment;
import org.broadleafcommerce.core.payment.domain.PaymentTransaction;
import org.broadleafcommerce.core.payment.service.DefaultPaymentGatewayCheckoutService;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZaoJiCMSPaymentGatewayCheckoutService extends DefaultPaymentGatewayCheckoutService {


    @Autowired
    protected IdFactory idFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZaoJiCMSCustomerServiceImpl.class);

    @Transactional("blTransactionManager")
    @Override
    public Long applyPaymentToOrder(PaymentResponseDTO responseDTO, PaymentGatewayConfiguration config) {

        //Payments can ONLY be parsed into Order Payments if they are 'valid'
        if (!responseDTO.isValid()) {
            throw new IllegalArgumentException("Invalid payment responses cannot be parsed into the order payment domain");
        }

        if (config == null) {
            throw new IllegalArgumentException("Config service cannot be null");
        }

        Long orderId = Long.parseLong(responseDTO.getOrderId());
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);

        if (!OrderStatus.SUBMITTED.equals(order.getStatus()) && !OrderStatus.IN_PROCESS.equals(order.getStatus()) && !OrderStatus.CSR_OWNED.equals(order.getStatus()) && !OrderStatus.QUOTE.equals(order.getStatus())) {
            throw new IllegalArgumentException("Cannot apply another payment to an Order that is not IN_PROCESS or CSR_OWNED");
        }

        Customer customer = order.getCustomer();
        if (customer.isAnonymous()) {
            GatewayCustomerDTO<PaymentResponseDTO> gatewayCustomer = responseDTO.getCustomer();
            if (StringUtils.isEmpty(customer.getFirstName()) && gatewayCustomer != null) {
                customer.setFirstName(gatewayCustomer.getFirstName());
            }
            if (StringUtils.isEmpty(customer.getLastName()) && gatewayCustomer != null) {
                customer.setLastName(gatewayCustomer.getLastName());
            }
            if (StringUtils.isEmpty(customer.getEmailAddress()) && gatewayCustomer != null) {
                customer.setEmailAddress(gatewayCustomer.getEmail());
            }
        }

        // If the gateway sends back an email address and the order does not contain one, set it.
        GatewayCustomerDTO<PaymentResponseDTO> gatewayCustomer = responseDTO.getCustomer();
        if (order.getEmailAddress() == null && gatewayCustomer != null) {
            order.setEmailAddress(gatewayCustomer.getEmail());
        }

        // If the gateway sends back Shipping Information, we will save that to the first shippable fulfillment group.
        dtoToEntityService.populateShippingInfo(responseDTO, order);

        // ALWAYS create a new order payment for the payment that comes in. Invalid payments should be cleaned up by
        // invoking {@link #markPaymentAsInvalid}.
        OrderPayment payment = orderPaymentService.create();
        payment.setType(responseDTO.getPaymentType());
        payment.setPaymentGatewayType(responseDTO.getPaymentGatewayType());
        payment.setAmount(responseDTO.getAmount());

        // If this gateway does not support multiple payments then mark all of the existing payments
        // as invalid before adding the new one
        List<OrderPayment> paymentsToInvalidate = new ArrayList<OrderPayment>();
        Address tempBillingAddress = null;
        if (!config.handlesMultiplePayments()) {
            PaymentGatewayType gateway = config.getGatewayType();
            for (OrderPayment p : order.getPayments()) {
                // A Payment on the order will be invalidated if:
                // - It's a temporary order payment: There may be a temporary Order Payment on the Order (e.g. to save the billing address)
                // - The payment being added is a Final Payment and there already exists a Final Payment
                // - The payment being added has the same gateway type of an existing one.
                if (PaymentGatewayType.TEMPORARY.equals(p.getGatewayType()) ||
                        (p.isFinalPayment() && payment.isFinalPayment()) ||
                        (p.getGatewayType() != null && p.getGatewayType().equals(gateway))) {

                    paymentsToInvalidate.add(p);

                    if (PaymentGatewayType.TEMPORARY.equals(p.getGatewayType()) ) {
                        tempBillingAddress = p.getBillingAddress();
                    }
                }
            }
        }

        for (OrderPayment invalid : paymentsToInvalidate) {
            // 2
            markPaymentAsInvalid(invalid.getId());
        }

        // The billing address that will be saved on the order will be parsed off the
        // Response DTO sent back from the Gateway as it may have Address Verification or Standardization.
        // If you do not wish to use the Billing Address coming back from the Gateway, you can override the
        // populateBillingInfo() method or set the useBillingAddressFromGateway property.
        dtoToEntityService.populateBillingInfo(responseDTO, payment, tempBillingAddress, isUseBillingAddressFromGateway());

        // Create the transaction for the payment
        PaymentTransaction transaction = orderPaymentService.createTransaction();
        transaction.setAmount(responseDTO.getAmount());
        transaction.setRawResponse(responseDTO.getRawResponse());
        transaction.setSuccess(responseDTO.isSuccessful());
        transaction.setType(responseDTO.getPaymentTransactionType());
        for (Map.Entry<String, String> entry : responseDTO.getResponseMap().entrySet()) {
            transaction.getAdditionalFields().put(entry.getKey(), entry.getValue());
        }

        //Set the Credit Card Info on the Additional Fields Map
        if (responseDTO.getCreditCard() != null && responseDTO.getCreditCard().creditCardPopulated()) {

            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.NAME_ON_CARD.getType(),
                    responseDTO.getCreditCard().getCreditCardHolderName());
            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.CARD_TYPE.getType(),
                    responseDTO.getCreditCard().getCreditCardType());
            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.EXP_DATE.getType(),
                    responseDTO.getCreditCard().getCreditCardExpDate());
            transaction.getAdditionalFields().put(PaymentAdditionalFieldType.LAST_FOUR.getType(),
                    responseDTO.getCreditCard().getCreditCardLastFour());
        }

        //TODO: validate that this particular type of transaction can be added to the payment (there might already
        // be an AUTHORIZE transaction, for instance)
        //Persist the order payment as well as its transaction
        payment.setOrder(order);
        transaction.setOrderPayment(payment);
        payment.addTransaction(transaction);

        payment = orderPaymentService.save(payment);

        if (transaction.getSuccess()) {
            orderService.addPaymentToOrder(order, payment, null);
        } else {
            // We will have to mark the entire payment as invalid and boot the user to re-enter their
            // billing info and payment information as there may be an error either with the billing address/or credit card
            handleUnsuccessfulTransaction(payment);
        }

        long paymentId =  payment.getId();

        ZaoJiCMSOrderPayment orderPayment = (ZaoJiCMSOrderPayment)orderPaymentService.readPaymentById(paymentId);
        orderPayment.setPaymentGatewayTradeNO(responseDTO.getResponseMap().get("PAYMENT_GATEWAY_TRADE_NO"));
        orderPayment.setUnifiedOrderId(Long.valueOf(responseDTO.getResponseMap().get("UNIFIED_ORDER_ID")));
        orderPaymentService.save(orderPayment);

        //Order order = orderPayment.getOrder();
        //生成提货码记录(虚拟产品没有配送)
        List<FulfillmentGroup> fulfillmentGroups = order.getFulfillmentGroups();
        if (fulfillmentGroups != null && fulfillmentGroups.size() > 0) {
            FulfillmentGroup fulfillmentGroup = fulfillmentGroups.get(0);
            if (fulfillmentGroup != null) {
                FulfillmentOption fulfillmentOption = fulfillmentGroup.getFulfillmentOption();
                if (fulfillmentOption != null) {
                    if (fulfillmentOption.getName().equals("门店自提")) {
                        order.setPickedUpInStore(true);
                        order.setWriteOffCode(idFactory.getNextId());
                        order.setWriteOffStatus(OrderWriteOffStatus.NORMAL.getType());
                        ZaoJiCMSOrderService cmsOrderService = (ZaoJiCMSOrderService)orderService;
                        cmsOrderService.save(order);
                    }
                }
            }
        }
        return paymentId;
    }
}
