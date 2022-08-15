package com.os.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.DispatcherHandler;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name="spring.cloud.gateway.enable", matchIfMissing = true)
@EnableConfigurationProperties
@AutoConfigureBefore({HttpHandlerAutoConfiguration.class, WebFluxAutoConfiguration.class})
@AutoConfigureAfter({GatewayLoadBalancerClientAutoConfiguration.class,
        GatewayClassPathWarningAutoConfiguration.class})
@ConditionalOnClass(DispatcherHandler.class)
public class CustomGatewayAutoConfig extends GatewayAutoConfiguration {

    @Bean
    public CustomRoutePredicateHandlerMapping customRoutingPredicateMapping(
            @Qualifier("routeCacheManager") CacheManager routeCacheManager,
            FilteringWebHandler webHandler,
            RouteLocator routeLocator,
            GlobalCorsProperties globalCorsProperties,
            Environment environment){

        return new CustomRoutePredicateHandlerMapping(
                routeCacheManager,
                webHandler,
                routeLocator,
                globalCorsProperties,
                environment);
    }

    @Bean
    @ConditionalOnMissingBean(RoutePredicateHandlerMapping.class)
    public RoutePredicateHandlerMapping routePredicateHandlerMapping(
            FilteringWebHandler webHandler,
            RouteLocator routeLocator,
            GlobalCorsProperties globalCorsProperties,
            Environment environment){

        return new RoutePredicateHandlerMapping(
                webHandler,
                routeLocator,
                globalCorsProperties,
                environment);
    }
}
