package com.os.exception;

import com.google.gson.Gson;
import com.os.result.ResultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalException implements ErrorWebExceptionHandler {

    private final Gson gson;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        //có HttpOutputMessage thì không cần Publisher.
        if(response.isCommitted())
            return Mono.error(ex);

        //Đặt kiểu dữ liệu phản hồi là Json.
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if(ex instanceof ResponseStatusException)
            response.setStatusCode(((ResponseStatusException) ex).getStatus());

        return response.writeWith(Mono.fromSupplier(() -> {
            ResultEntity result = null;
            if(ex instanceof SignException)
                result = ((SignException) ex).getResultEntity();
            else
                result = ResultEntity.failed().message("Operation failed, request timed out").build();
            return response.bufferFactory().wrap(gson.toJson(result).getBytes());
        }));
    }
}
