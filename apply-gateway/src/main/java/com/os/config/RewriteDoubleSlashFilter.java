package com.os.config;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class RewriteDoubleSlashFilter implements WebFilter {

    private static final String DOUBLE_SLASH = "//";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String oldPath = exchange.getRequest().getPath().toString();
        String newPath = oldPath.replaceAll(DOUBLE_SLASH, "/");
        exchange.mutate().request(request -> request.path(newPath)).build();
        return chain.filter(exchange);
    }
}
