package cn.com.kelaikewang.configuration;

import cn.com.kelaikewang.core.profile.service.ZaoJiCMSCustomerService;
import cn.com.kelaikewang.core.profile.service.ZaoJiCMSLoginService;
import cn.com.kelaikewang.filter.InviteFilter;
import cn.com.kelaikewang.filter.WeChatAutoLoginFilter;
import cn.com.kelaikewang.filter.ZaoJiCMSCartStateFilter;
//import io.zaojicms.filter.RedirectToMSiteFilter;
import org.broadleafcommerce.cms.web.PageHandlerMapping;
import org.broadleafcommerce.common.config.service.SystemPropertiesService;
import org.broadleafcommerce.common.extensibility.context.merge.Merge;
import org.broadleafcommerce.common.web.controller.annotation.EnableAllFrameworkControllers;
import org.broadleafcommerce.common.web.filter.FilterOrdered;
import org.broadleafcommerce.common.web.filter.IgnorableOpenEntityManagerInViewFilter;
import org.broadleafcommerce.core.order.domain.NullOrderFactory;
import org.broadleafcommerce.core.web.catalog.CategoryHandlerMapping;
import org.broadleafcommerce.core.web.catalog.ProductHandlerMapping;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleafViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Configuration
@EnableAllFrameworkControllers
@ComponentScan({"cn.com.kelaikewang"})
public class SiteServletConfig extends WebMvcConfigurerAdapter {

    @Autowired
    protected SystemPropertiesService propertiesService;

    @Resource(name = "blNullOrderFactory")
    protected NullOrderFactory nullOrderFactory;

    @Autowired
    private ZaoJiCMSCustomerService nsCustomerService;
    @Resource(name = "blLoginService")
    private ZaoJiCMSLoginService loginService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/favicon.ico");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DeviceResolverHandlerInterceptor());
        registry.addInterceptor(new SitePreferenceHandlerInterceptor());
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SitePreferenceHandlerMethodArgumentResolver());
    }
    @Bean
    @Primary
    public LiteDeviceDelegatingViewResolver liteDeviceDelegatingViewResolver(@Qualifier("blThymeleafViewResolver") BroadleafThymeleafViewResolver viewResolver) {
        LiteDeviceDelegatingViewResolver delegate = new LiteDeviceDelegatingViewResolver(viewResolver);
        delegate.setMobilePrefix("mobile/");
        delegate.setTabletPrefix("tablet/");
        delegate.setEnableFallback(true);
        return delegate;
    }
    @Bean
    @DependsOn("thymeleafViewResolver")
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver(@Qualifier("liteDeviceDelegatingViewResolver") LiteDeviceDelegatingViewResolver liteDeviceDelegatingViewResolver){
        ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
        List<ViewResolver> viewResolvers = new ArrayList<>();
        viewResolvers.add(liteDeviceDelegatingViewResolver);
        contentNegotiatingViewResolver.setViewResolvers(viewResolvers);
        return contentNegotiatingViewResolver;
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

/*    @Bean
    public FilterRegistrationBean redirectToMSiteFilterFilterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        RedirectToMSiteFilter redirectToMSiteFilter = new RedirectToMSiteFilter(propertiesService.resolveSystemProperty("mSiteUrl"));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(redirectToMSiteFilter);
        return registrationBean;
    }*/
    @Bean
    public FilterRegistrationBean weChatAutoLoginFilterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        WeChatAutoLoginFilter weChatAutoLoginFilter = new WeChatAutoLoginFilter();
        weChatAutoLoginFilter.setLoginService(loginService);
        weChatAutoLoginFilter.setNsCustomerService(nsCustomerService);
        bean.setFilter(weChatAutoLoginFilter);
        bean.setName("weChatAutoLoginFilter");
        bean.setOrder(Integer.MAX_VALUE);
        bean.addUrlPatterns("/*");
        return bean;
    }
    @Bean
    public FilterRegistrationBean inviteFilterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        InviteFilter inviteFilter = new InviteFilter();
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(inviteFilter);
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean nextShopCartStateFilterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        ZaoJiCMSCartStateFilter cartStateFilter = new ZaoJiCMSCartStateFilter();
        cartStateFilter.setNullOrderFactory(nullOrderFactory);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setFilter(cartStateFilter);
        registrationBean.setOrder(Integer.MAX_VALUE);
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
        mappings.put("/img/**", "blImageResources");
        mappings.put("/fonts/**", "blFontResources");
        resourceMapping.setMappings(mappings);
        return resourceMapping;
    }
    
    @Merge("blJsLocations")
    public List<String> jsLocations() {
        return Collections.singletonList("classpath:/js/");
    }

    @Merge("blCssLocations")
    public List<String> cssLocations() {
        return Collections.singletonList("classpath:/css/");
    }

    @Merge("blImageLocations")
    public List<String> imageLocations() {
        return Collections.singletonList("classpath:/img/");
    }

    @Merge("blFontLocations")
    public List<String> fontLocations() {
        return Collections.singletonList("classpath:/fonts/");
    }
    
    @Bean
    public HandlerMapping productHandlerMapping() {
        ProductHandlerMapping mapping = new ProductHandlerMapping();
        mapping.setOrder(3);
        return mapping;
    }
    
    @Bean
    public HandlerMapping pageHandlerMapping() {
        PageHandlerMapping mapping = new PageHandlerMapping();
        mapping.setOrder(4);
        return mapping;
    }
    
    @Bean
    public HandlerMapping categoryHandlerMapping() {
        CategoryHandlerMapping mapping = new CategoryHandlerMapping();
        mapping.setOrder(5);
        return mapping;
    }
    
}
