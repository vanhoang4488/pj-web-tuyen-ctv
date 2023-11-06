package vanhoang.project.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Order(2)
@Component
public class CustomHiddenMethodFilter extends HiddenHttpMethodFilter {
    private static final List<HttpMethod> ALLOWED_METHODS =
            List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE);

    private static final String PARAM_METHOD_NAME = "_method";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String valueParamMethodName = serverHttpRequest.getQueryParams().getFirst(PARAM_METHOD_NAME);
        if (HttpMethod.POST == serverHttpRequest.getMethod() &&
                StringUtils.isNoneEmpty(valueParamMethodName)) {
            HttpMethod paramMethod = HttpMethod.resolve(valueParamMethodName);
            if (ALLOWED_METHODS.contains(paramMethod)) {
                return chain.filter(exchange.mutate().request(builder -> builder.method(paramMethod)).build());
            }
            else {
                ServerHttpResponse serverHttpResponse = exchange.getResponse();
                serverHttpResponse.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
                return Mono.empty();
            }
        }
        else {
            return chain.filter(exchange);
        }
    }
}
