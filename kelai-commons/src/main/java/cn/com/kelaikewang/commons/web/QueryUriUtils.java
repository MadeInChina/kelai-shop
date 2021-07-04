package cn.com.kelaikewang.commons.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component("queryUriUtils")
public class QueryUriUtils {
    public String getQueryUri(String sort){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String q = request.getParameter("q");
        return "/search?q=" + (StringUtils.isEmpty(q) ? "" : q) + (StringUtils.isEmpty(sort) ? "" : "&sort=" + sort);
    }
}
