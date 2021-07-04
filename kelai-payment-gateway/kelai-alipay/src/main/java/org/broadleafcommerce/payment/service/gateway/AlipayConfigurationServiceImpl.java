package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("blAlipayConfigurationService")
public class AlipayConfigurationServiceImpl  extends AbstractPaymentGatewayConfigurationService implements PaymentGatewayConfigurationService {

    @Resource(name = "blAlipayConfiguration")
    protected AlipayConfiguration configuration;

    @Resource(name = "blAlipayRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "blAlipayTransactionService")
    protected PaymentGatewayTransactionService transactionService;

    @Resource(name = "blAlipayWebResponseService")
    protected PaymentGatewayWebResponseService webResponseService;

    @Resource(name = "blAlipayWapHostedService")
    protected PaymentGatewayHostedService paymentGatewayWapHostedService;

    @Resource(name = "blAlipayPageHostedService")
    protected PaymentGatewayHostedService paymentGatewayPageHostedService;

    @Resource(name = "blAlipayReportingService")
    protected PaymentGatewayReportingService reportingService;

    @Resource(name = "blAlipayTransactionConfirmationService")
    protected PaymentGatewayTransactionConfirmationService transactionConfirmationService;

    @Override
    public AlipayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayRollbackService getRollbackService() {
        return rollbackService;
    }

    @Override
    public PaymentGatewayTransactionService getTransactionService() {
        return transactionService;
    }

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return webResponseService;
    }

    public PaymentGatewayHostedService getPaymentGatewayWapHostedService() {
        return paymentGatewayWapHostedService;
    }

    public PaymentGatewayHostedService getPaymentGatewayPageHostedService() {
        return paymentGatewayPageHostedService;
    }

    @Override
    public PaymentGatewayReportingService getReportingService() {
        return reportingService;
    }

    @Override
    public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
        return transactionConfirmationService;
    }

    @Override
    public PaymentGatewayHostedService getHostedService() {
        return paymentGatewayWapHostedService;
    }
}
