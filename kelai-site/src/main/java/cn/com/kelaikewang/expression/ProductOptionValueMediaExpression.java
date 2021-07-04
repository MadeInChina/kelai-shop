package cn.com.kelaikewang.expression;

import cn.com.kelaikewang.core.catalog.domain.ZaoJiCMSProduct;
import cn.com.kelaikewang.core.catalog.domain.ProductColorOptionValueMedia;
import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.core.catalog.domain.ProductOptionValue;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("zjcmsProductOptionValueMediaExpression")
@ConditionalOnTemplating
public class ProductOptionValueMediaExpression implements BroadleafVariableExpression {
    @Override
    public String getName() {
        return "zjcmsProductOptionValueMedia";
    }
    public String get(ZaoJiCMSProduct product, ProductOptionValue optionValue){
        List<ProductColorOptionValueMedia> optionValueMedia = product.getProductColorOptionValueMedia();
        for (ProductColorOptionValueMedia colorOptionValueMedia : optionValueMedia){
            if (colorOptionValueMedia.getProductOptionValue().getId().equals(optionValue.getId())){
                return colorOptionValueMedia.getImageUrl();
            }
        }
        return null;
    }
    public boolean isHasColor(ZaoJiCMSProduct product,ProductOptionValue optionValue){
        List<ProductColorOptionValueMedia> optionValueMedia = product.getProductColorOptionValueMedia();
        for (ProductColorOptionValueMedia colorOptionValueMedia : optionValueMedia){
            if (colorOptionValueMedia.getProductOptionValue().getId().equals(optionValue.getId())){
                return true;
            }
        }
        return false;
    }
}
