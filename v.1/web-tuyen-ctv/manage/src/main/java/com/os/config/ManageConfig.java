package com.os.config;

import com.os.Idempotency.IdempotencyInterceptor;
import com.os.i18n.I18nFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class ManageConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new IdempotencyInterceptor()).addPathPatterns("/**");
    }
}
