package com.os.filter;

import com.google.gson.Gson;
import com.os.RedisCacheHandler;
import com.os.result.ResultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.os.uitls.MultipleReqHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepeatRequestFilter implements GlobalFilter, Ordered {
    private final Gson gson;
    private final MultipleReqHandler multipleReqHandler;

    @Value("${request.max.time}")
    private int maxTime;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //Todo xác định ip

        //xử lý randomStr
        String randomStr = request.getQueryParams().getFirst("randomStr");
        log.info("=======> randomStr: {}", randomStr);
        if(!multipleReqHandler.handler(exchange))
            return this.resultMono(exchange);

        //xử lý timestamp
        String timestampStr = request.getQueryParams().getFirst("timestamp");
        if(!StringUtils.isNoneBlank(timestampStr) || timestampStr.length() != 13)
            return this.resultMono(exchange);

        long timestamp = Long.parseLong(timestampStr);
        long current_timestamp = System.currentTimeMillis();
        long timeDiff = Math.abs(current_timestamp - timestamp);
        long maxTimeDiff = maxTime * 60 * 1000;
        if(timeDiff > maxTimeDiff){
            log.info("==============>>>Time out<<<=============");
            log.info("timestamp: {}", timestamp);
            log.info("current_timestamp: {}", current_timestamp);
            log.info("timeDiff: {}", timeDiff);
            return this.resultMono(exchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -5;
    }

    private Mono<Void> resultMono(ServerWebExchange exchange){
        ResultEntity result = ResultEntity.failed().message("TIMEOUT").build();
        String jsonResult = gson.toJson(result);
        byte[] uppedContent = new String(jsonResult.getBytes(),
                Charset.forName("UTF-8")).getBytes(StandardCharsets.UTF_8);
        Mono<DataBuffer> mono = Mono.just(exchange.getResponse().bufferFactory().wrap(uppedContent));
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        //giải quyết vấn đề bị cắt xén dữ liệu.
        exchange.getResponse().getHeaders()
                .add("Content-Type", "application/json;charset=utf-8");
        return exchange.getResponse().writeWith(mono);
    }
}
