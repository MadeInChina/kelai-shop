package cn.com.kelaikewang.expression;

import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("zjcmsStaticFileExpression")
@ConditionalOnTemplating
public class StaticFileExpression implements BroadleafVariableExpression {

    @Value("${staticFileUrlPrefix}")
    private String staticFileUrlPrefix;

    @Override
    public String getName() {
        return "sf";
    }
    public String url(String staticFile){
        return  staticFileUrlPrefix + staticFile;
    }
}
