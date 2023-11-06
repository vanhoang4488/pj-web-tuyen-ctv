package com.os.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GatewayConfig{

    @Bean
    public WebFilter webFilter(){
        return (ServerWebExchange exchange, WebFilterChain chain) ->{
            ServerHttpRequest request = exchange.getRequest();
            if(CorsUtils.isCorsRequest(request)){
                ServerHttpResponse response = exchange.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, headers.getOrigin());
                if(request.getMethod() == HttpMethod.OPTIONS){
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    public RewriteDoubleSlashFilter rewriteDoubleSlashFilter(){
        return new RewriteDoubleSlashFilter();
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer(){
        return new DefaultServerCodecConfigurer();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter(){
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
                return chain.filter(exchange);
            }
        };
    }

    @EventListener(ApplicationStartedEvent.class)
    public void startFinish(){
        log.info("============>{}<===========", "apply-gateway starts successfully");
    }
}
