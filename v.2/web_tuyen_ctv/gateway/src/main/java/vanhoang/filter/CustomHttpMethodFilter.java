package vanhoang.filter;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Order(2)
@Component
public class CustomHttpMethodFilter extends HiddenHttpMethodFilter {

    private static final List<HttpMethod> ALLOWED_METHODS =
            List.of(HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (HttpMethod.GET == request.getMethod()) {
            return chain.filter(exchange);
        }
        else if (HttpMethod.POST == request.getMethod()){
            String method = request.getQueryParams().getFirst(HiddenHttpMethodFilter.DEFAULT_METHOD_PARAMETER_NAME);
            if (method == null) {
                return chain.filter(exchange);
            }
            else {
                HttpMethod httpMethod = HttpMethod.resolve(method.toUpperCase(Locale.ENGLISH));
                boolean isSupported = (httpMethod != null && ALLOWED_METHODS.contains(httpMethod));
                if (isSupported) {
                    return chain.filter(exchange.mutate().request(builder -> builder.method(httpMethod)).build());
                }
                else {
                    return this.getResponse(exchange, httpMethod);
                }
            }
        }
        else {
            return this.getResponse(exchange, request.getMethod());
        }
    }

    private Mono<Void> getResponse(ServerWebExchange exchange, HttpMethod httpMethod) {
        String message = "HttpMethod " + httpMethod + " not supported";
        DefaultDataBuffer messageBuffer = new DefaultDataBufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(messageBuffer));
    }
}
