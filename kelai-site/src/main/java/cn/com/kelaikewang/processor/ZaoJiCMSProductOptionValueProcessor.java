package cn.com.kelaikewang.processor;

import org.broadleafcommerce.core.web.processor.ProductOptionValueProcessor;
import org.broadleafcommerce.presentation.model.BroadleafAttributeModifier;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ZaoJiCMSProductOptionValueProcessor extends ProductOptionValueProcessor {
    @Override
    public BroadleafAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, BroadleafTemplateContext context) {
        return super.getModifiedAttributes(tagName, tagAttributes, attributeName, attributeValue, context);
    }
}
