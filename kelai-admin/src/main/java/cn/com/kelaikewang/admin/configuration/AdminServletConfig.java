package cn.com.kelaikewang.admin.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import cn.com.kelaikewang.admin.web.filter.SetCookieFilter;
import org.broadleafcommerce.common.web.controller.FrameworkControllerHandlerMapping;
import org.broadleafcommerce.common.web.controller.annotation.EnableAllFrameworkControllers;
import org.broadleafcommerce.common.web.filter.FilterOrdered;
import org.broadleafcommerce.common.web.filter.IgnorableOpenEntityManagerInViewFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Configuration
@EnableAllFrameworkControllers
public class AdminServletConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(FrameworkControllerHandlerMapping.REQUEST_MAPPING_ORDER-1);
        registry.addResourceHandler("/img/**")
            .addResourceLocations("classpath:/open_admin_style/img/", "classpath:/common_style/img/", "classpath:/community-demo-style/img/", "classpath:/static/img/","/img/");
        registry.addResourceHandler("/fonts/**")
            .addResourceLocations("classpath:/open_admin_style/fonts/", "classpath:/community-demo-style/fonts/", "classpath:/common_style/fonts/");
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/favicon.ico");
        registry.addResourceHandler("/robots.txt")
            .addResourceLocations("classpath:/robots.txt");

        registry.addResourceHandler("/nio-contentbuilder/**")
                .addResourceLocations("classpath:/static/nio-contentbuilder/");

    }
    
    /**
     * Setup the "blPU" entity manager on the request thread using the entity-manager-in-view pattern
     */
    @Bean
    public FilterRegistrationBean openEntityManagerInViewFilterFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new IgnorableOpenEntityManagerInViewFilter();
        registrationBean.setFilter(openEntityManagerInViewFilter);
        registrationBean.setName("openEntityManagerInViewFilter");
        registrationBean.setOrder(FilterOrdered.PRE_SECURITY_HIGH);
        return registrationBean;
    }
    
    @Bean
    public HttpSessionEventPublisher sessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
    @Bean
    public HandlerMapping staticResourcesHandlerMapping() {
        SimpleUrlHandlerMapping resourceMapping = new SimpleUrlHandlerMapping();
        resourceMapping.setOrder(-10);
        Properties mappings = new Properties();
        mappings.put("/js/**", "blJsResources");
        mappings.put("/css/**", "blCssResources");
        resourceMapping.setMappings(mappings);
        return resourceMapping;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new SetCookieFilter());
        bean.setOrder(1);
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        /* 1.???????????????convert?????????????????????
          * 2.??????fastjson??????????????????????????????????????????????????????json??????
          * 3.???convertzhong ??????????????????
          * 4.???convert?????????converters??????
        */
        //1.???????????????convert?????????????????????
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2.??????fastjson??????????????????????????????????????????????????????json??????
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

        //????????????????????????(????????????????????????)
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        //3.???convertzhong ??????????????????
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //4.???convert?????????converters??????
        converters.add(fastConverter);

    }
}
