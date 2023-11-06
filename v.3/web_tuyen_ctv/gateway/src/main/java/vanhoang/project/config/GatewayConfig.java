package vanhoang.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Bean
    @Order(1)
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            ServerHttpResponse serverHttpResponse = exchange.getResponse();
            if (!CorsUtils.isCorsRequest(serverHttpRequest)) {
                serverHttpResponse.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return Mono.empty();
            }
            else if (HttpMethod.OPTIONS == serverHttpRequest.getMethod()) {
                serverHttpResponse.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            else {
                return chain.filter(exchange);
            }
        };
    }
}
