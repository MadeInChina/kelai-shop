package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("blWxPayConfigurationService")
public class WxPayConfigurationServiceImpl extends AbstractPaymentGatewayConfigurationService implements PaymentGatewayConfigurationService {

    @Resource(name = "blWxPayConfiguration")
    protected WxPayConfiguration configuration;

    @Resource(name = "blWxPayRollbackService")
    protected PaymentGatewayRollbackService rollbackService;

    @Resource(name = "blWxPayTransactionService")
    protected PaymentGatewayTransactionService transactionService;

    @Resource(name = "blWxPayWebResponseService")
    protected PaymentGatewayWebResponseService webResponseService;

    @Resource(name = "blWxPayWapHostedService")
    protected PaymentGatewayHostedService paymentGatewayWapHostedService;

    @Resource(name = "blWxPayPageHostedService")
    protected PaymentGatewayHostedService paymentGatewayPageHostedService;

    @Resource(name = "blWxPayReportingService")
    protected PaymentGatewayReportingService reportingService;

    @Resource(name = "blWxPayTransactionConfirmationService")
    protected PaymentGatewayTransactionConfirmationService transactionConfirmationService;

    @Override
    public WxPayConfiguration getConfiguration() {
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
