package com.os.uitls;

import com.os.RedisCacheHandler;
import com.os.filter.RepeatRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultipleReqHandler {

    private static final int RANDOMSTR_LENGTH = 32;

    private final RedisCacheHandler redisCacheHandler;

    @Value("${request.max.time}")
    private int maxTime;

    public boolean handler(ServerWebExchange exchange){
        String randomStr = exchange.getRequest().getQueryParams().getFirst("randomStr");
        if(!StringUtils.isNoneBlank(randomStr) || randomStr.length() != RANDOMSTR_LENGTH){
            log.info("=====> {}", "randomStr value is not valid");
            return false;
        }

        String randomStrKey =
                redisCacheHandler.getKey(RepeatRequestFilter.class, "randomStr", randomStr);
        String old_randomStr = redisCacheHandler.getCacheByKey(randomStrKey);
        if(!Objects.isNull(old_randomStr)){
            log.info("=====> {}", "randomStr value was exists");
            return false;
        }

        redisCacheHandler.addCacheByKey(randomStrKey, randomStr, maxTime, TimeUnit.SECONDS);
        return true;
    }
}
