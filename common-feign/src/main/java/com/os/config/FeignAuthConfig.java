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
public class FeignAuthConfig extends HystrixConcurrencyStrategy {
    private HystrixConcurrencyStrategy delegate;

    public FeignAuthConfig(){
       this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();

        if(this.delegate instanceof FeignAuthConfig)
            return;

        try {
            HystrixCommandExecutionHook commandExecutionHook =
                    HystrixPlugins.getInstance().getCommandExecutionHook();
            HystrixEventNotifier eventNotifier =
                    HystrixPlugins.getInstance().getEventNotifier();
            HystrixMetricsPublisher metricsPublisher =
                    HystrixPlugins.getInstance().getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy =
                    HystrixPlugins.getInstance().getPropertiesStrategy();

            this.logConcurrentStateOfHystrixPlugins (eventNotifier, metricsPublisher, propertiesStrategy);

            HystrixPlugins.reset();
            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        }
        catch (Exception e){
            log.error("===> Generator HystrixConcurrencyStrategy failed: {}", e.getMessage(), e);
        }
    }

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

    private void logConcurrentStateOfHystrixPlugins(HystrixEventNotifier eventNotifier,
                                               HystrixMetricsPublisher metricsPublisher,
                                               HystrixPropertiesStrategy propertiesStrategy){
        if (log.isDebugEnabled()) {
            log.debug("Current Hystrix plugins configuration is [" + "concurrencyStrategy ["
                    + delegate + "]," + "eventNotifier [" + eventNotifier + "]," + "metricPublisher ["
                    + metricsPublisher + "]," + "propertiesStrategy [" + propertiesStrategy + "]," + "]");
            log.debug("Registering Sleuth Hystrix Concurrency Strategy.");
        }
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return new WrappedCallable<T>(callable, requestAttributes);
    }

    static class WrappedCallable<T> implements Callable<T> {

        private final Callable<T> target;

        private final RequestAttributes requestAttributes;

        public WrappedCallable(Callable<T> callable, RequestAttributes requestAttributes){
            this.target = callable;
            this.requestAttributes = requestAttributes;
        }

        @Override
        public T call() throws Exception {
            try{
                RequestContextHolder.setRequestAttributes(requestAttributes);
                return target.call();
            }
            finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
