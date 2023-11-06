package com.os.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.server.ServerWebExchange;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.Optional;


/*
 * Thực tế chứng minh:
 * lớp tìm route phù hợp, mặc định (lookupRoute) của Gateway bị suy giảm
 * nhiều về hiều hiệu năng khi số lượng request gửi lên lớn.
 *
 */
@Slf4j
@Configuration
public class CustomRoutePredicateHandlerMapping extends RoutePredicateHandlerMapping {

    private final Cache specialCache;

    public CustomRoutePredicateHandlerMapping(
            @Qualifier("routeCacheManager") CacheManager cacheManager,
            FilteringWebHandler webHandler,
            RouteLocator routeLocator,
            GlobalCorsProperties globalCorsProperties,
            Environment environment){

        super(webHandler, routeLocator, globalCorsProperties, environment);
        specialCache = cacheManager.getCache("specialRouteCache");
    }

    @Override
    public Mono<Route> lookupRoute(ServerWebExchange exchange){

        String specialPath = exchange.getRequest().getPath().toString();

        /*
         * Giải pháp:
         * Sử dụng CaffeineCache để lưu các route tương ứng với từng path của request.
         * 1 Request gửi lên: sẽ được kiểm tra trong CaffeineCache trước, để tìm kiếm route:
         * - Có => sử dụng.
         * - Không => dùng lookupRoute mặc định xác định route => Sau đó, lưu route đó vào
         * CaffeineCache với key = path của request đã thay "/" -> "_"
         */

        // 1 vài loại request mới được thực hiện thao tác kể trên.
        if(checkPath(specialPath)){
            return CacheMono
                    .lookup(
                            key -> Mono.justOrEmpty(specialCache.get(key, Route.class)).map(Signal::next),
                            toKey(specialPath))
                    .onCacheMissResume(super.lookupRoute(exchange))
                    .andWriteWith(
                            (key, signal) -> Mono.fromRunnable(
                                    () -> Optional.ofNullable(signal.get())
                                            .ifPresent(value -> specialCache.put(key, value))
                            )
                    );
        }

        return super.lookupRoute(exchange);
    }

    private boolean checkPath(String path) {
        String[] validPaths = new String[]{"/admin", "/api"};
        for(String validPath : validPaths)
            if(path.startsWith(validPath))  return true;

        return false;
    }

    private String toKey(String specialPath){
        String toKey = specialPath.replaceAll("/" , "_");
        log.info("------>>> route: {}", toKey);
        return toKey;
    }
}
