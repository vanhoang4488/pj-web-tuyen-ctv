package com.os.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import com.os.uitls.HandleRequestDecorator;
import com.os.uitls.VerifySign;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerifySignFilter implements GlobalFilter, Ordered {

    private final VerifySign verifySign;
    private final HandleRequestDecorator handleRequestDecorator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        MediaType mediaType = request.getHeaders().getContentType();
        if(method == HttpMethod.POST && mediaType.equals(MediaType.APPLICATION_JSON)){

            ServerRequest serverRequest = ServerRequest.create(exchange,
                    HandlerStrategies.withDefaults().messageReaders());

            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                    .flatMap(body -> {
                       if(StringUtils.isNotEmpty(body)){
                           Mono<String> stringMono = verifySign.verifyBody(exchange, body);
                           if(!Objects.isNull(stringMono))
                               return stringMono;
                       }
                       return Mono.just(body);
                    });
            return handleRequestDecorator.decorator(exchange, chain, modifiedBody);
        }
        else{
            Mono<Void> mono = verifySign.verify(exchange);
            if(mono != null)
                return mono;
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
