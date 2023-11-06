package com.os.uitls;

import cn.hutool.crypto.SecureUtil;
import com.os.exception.SignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VerifySign{

    private static final String SIGN_NAME = "sign";

    public Mono<Void> verify(ServerWebExchange exchange){
        Map<String, String> params = new TreeMap<>();

        addParams(exchange, params);

        return (verifySign(exchange, params, null))? null : signError();
    }

    public Mono<String> verifyBody(ServerWebExchange exchange, String body){
        Map<String, String> params = new TreeMap<>();

        addParams(exchange, params);

        return (verifySign(exchange, params, body))? null : signError();
    }

    private void addParams(ServerWebExchange exchange, Map<String, String> params){
        MultiValueMap<String, String> multiParams= exchange.getRequest().getQueryParams();
        multiParams.forEach((key, values) -> {
            if(!key.equals(SIGN_NAME)){
                String value = values.stream().collect(Collectors.joining(","));
                params.put(key, value);
            }
        });
    }

    private boolean verifySign(ServerWebExchange exchange,
                               Map<String, String> params,
                               String body){
        StringBuilder parameters = new StringBuilder();
        params.forEach((key, value) -> {
            parameters.append(key).append("=").append(value).append("&");
        });

        if(body != null)
            parameters.append(body);

        String nowSign = SecureUtil.md5(parameters.toString());
        String sign = exchange.getRequest().getQueryParams().getFirst(SIGN_NAME);
        log.info("===========> now sign: {}", nowSign);
        log.info("===========> sign: {}", sign);

        return nowSign.equals(sign);
    }

    private Mono signError(){
        return Mono.error(new SignException("sign not suitable"));
    }
}
