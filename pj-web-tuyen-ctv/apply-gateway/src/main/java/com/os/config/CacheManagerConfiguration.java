package com.os.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@AutoConfigureBefore(CustomGatewayAutoConfig.class)
public class CacheManagerConfiguration {

    @Bean
    @Primary
    public CacheManager defaultCacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        CaffeineSpec spec = CaffeineSpec
                .parse("initialCapacity=64,maximumSize=512,expireAfterWrite=300s");
        cacheManager.setCacheNames(null);
        return cacheManager;
    }

    @Bean
    public CacheManager routeCacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        CaffeineSpec spec = CaffeineSpec
                .parse("initialCapacity=512,maximumSize=2048,expireAfterWrite=3000s");
        cacheManager.setCacheNames(null);
        return cacheManager;
    }
}
