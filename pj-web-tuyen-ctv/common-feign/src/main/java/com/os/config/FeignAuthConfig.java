package com.os.config;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.hystrix.SetterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

@Slf4j
@Configuration
public class FeignAuthConfig{

    @Bean
    public RequestInterceptor RequestHeaderInterceptor(){
        return new RequestHeaderInterceptor();
    }

    @Bean
    public SetterFactory customFeignSetterFactory(){ return new DependencySetterFactory();}

    @Bean
    public ErrorDecoder specialErrorDecoder(){
        return new SpecialErrorDecoder();
    }


}
