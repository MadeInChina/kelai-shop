package cn.com.kelaikewang.commons.web.ui;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component("paginationUtils")
public class PaginationUtils {
    public String getPageUrl(int pageIndex){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?pageIndex=");
        stringBuilder.append(pageIndex);

        while (parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            if(!"pageIndex".equals(name)){
                stringBuilder.append("&");
                stringBuilder.append(name);
                stringBuilder.append("=");
                stringBuilder.append(request.getParameter(name));
            }

        }
        return stringBuilder.toString();
    }
}
