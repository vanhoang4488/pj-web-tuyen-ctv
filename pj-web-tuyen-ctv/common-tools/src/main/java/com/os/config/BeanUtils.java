package com.os.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(BeanUtils.applicationContext == null)
           BeanUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() { return applicationContext;}

    public static <T> T getBean(Class<T> clazz) { return applicationContext.getBean(clazz);};
}
