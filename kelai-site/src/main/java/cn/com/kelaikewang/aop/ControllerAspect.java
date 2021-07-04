package cn.com.kelaikewang.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ControllerAspect {
    @Pointcut("execution(* cn.com.kelaikewang.controller..*.handleRequest(..))")
    public void controllerHandleRequest(){

    }
    @Around("controllerHandleRequest()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object obj = proceedingJoinPoint.proceed();
        HttpServletRequest request = (HttpServletRequest)proceedingJoinPoint.getArgs()[0];
        LiteDeviceResolver liteDeviceResolver = new LiteDeviceResolver();
        Device device = liteDeviceResolver.resolveDevice(request);
        ModelAndView modelAndView = (ModelAndView)obj;
        if (device.isMobile()){
            modelAndView.setViewName("mobile/" + modelAndView.getViewName());
        }
        return modelAndView;
    }
}
