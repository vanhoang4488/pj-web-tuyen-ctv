package com.os.uitls;

import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HandleRequestDecorator {

    public Mono<Void> decorator(ServerWebExchange exchange,
                                GatewayFilterChain chain,
                                Mono<String> modifiedBody){
        BodyInserter bodyInserters = BodyInserters.fromPublisher(modifiedBody, String.class);
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserters.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request){
                @Override
                public HttpHeaders getHeaders(){
                    long length = headers.getContentLength();
                    if(length <= 0)
                        headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                    return headers;
                }

                @Override
                public Flux<DataBuffer> getBody() { return outputMessage.getBody();}
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        }));
    }
}
