package vanhoang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import vanhoang.filter.CustomHttpMethodFilter;

@Configuration
public class GatewayConfig {

    @Bean
    @Order(1)
    public WebFilter corsFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            if (!CorsUtils.isCorsRequest(request)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }
            else if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            else {
                return chain.filter(exchange);
            }
        };
    }
}
