package com.os.config;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import feign.Target;
import feign.hystrix.SetterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class DependencySetterFactory implements SetterFactory {

    @Value("${hystrix.threadpool.core.size}")
    private int coreSize;

    @Value("${hystrix.threadpool.maximum.size}")
    private int maximumSize;

    @Value("${hystrix.threadpool.maxQueue.size}")
    private int maxQueueSize;

    @Override
    public HystrixCommand.Setter create(Target<?> target, Method method) {
        String groupKey = target.name();

        HystrixThreadPoolProperties.Setter setter = HystrixThreadPoolProperties.Setter();
        setter.withCoreSize(coreSize);
        setter.withMaximumSize(maximumSize);
        setter.withMaxQueueSize(maxQueueSize);
        setter.withQueueSizeRejectionThreshold(maxQueueSize);
        setter.withAllowMaximumSizeToDivergeFromCoreSize(true);

        return HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                .andThreadPoolPropertiesDefaults(setter);
    }
}
