package cn.com.kelaikewang.infrastructure.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext; // Spring应用上下文环境
    private static Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
        environment = applicationContext.getEnvironment();
    }
    public static Environment getEnvironment(){
        return environment;
    }
    public static ApplicationContext getContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }
    public static <T> T getBean(String name,Class<T> tClass) throws BeansException {
        return (T) applicationContext.getBean(name,tClass);
    }
    public static <T> T getBean(Class<T> tClass){
        return (T) applicationContext.getBean(tClass);
    }
    public static <T> Map<String, T> getBeanOfType(Class<T> cls){
        return applicationContext.getBeansOfType(cls);
    }
    public static DefaultListableBeanFactory getBeanFactory(){
        return applicationContext.getBean(DefaultListableBeanFactory.class);
    }

}
