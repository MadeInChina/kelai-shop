package org.broadleafcommerce.payment.web.expression;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.payment.service.gateway.AlipayConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("blAlipayVariableExpression")
public class AlipayVariableExpression implements BroadleafVariableExpression {
    @Resource(name = "blAlipayConfiguration")
    private AlipayConfiguration alipayConfiguration;
    @Override
    public String getName() {
        return "alipay";
    }
    public boolean hasConfigProperties() {
        return StringUtils.isNotBlank(alipayConfiguration.getAlipayPublicKey())
                && StringUtils.isNotBlank(alipayConfiguration.getNotifyUrl())
                && StringUtils.isNotBlank(alipayConfiguration.getReturnUrl())
                && StringUtils.isNotBlank(alipayConfiguration.getAppId())
                && StringUtils.isNotBlank(alipayConfiguration.getGateway())
                && StringUtils.isNotBlank(alipayConfiguration.getMerchantPrivateKey());
    }
}
