package org.broadleafcommerce.vendor.alipay.config;

import org.broadleafcommerce.common.config.DefaultOrderFrameworkCommonClasspathPropertySource;

public class AlipayProperties  extends DefaultOrderFrameworkCommonClasspathPropertySource {

    @Override
    public String getClasspathFolder() {
        return "config/bc/Alipay/";
    }

}
