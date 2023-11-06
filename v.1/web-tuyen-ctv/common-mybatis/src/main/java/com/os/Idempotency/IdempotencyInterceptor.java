package com.os.Idempotency;

import com.google.gson.Gson;
import com.os.Indempotency.Idempotency;
import com.os.Indempotency.IdempotencyConstant;
import com.os.Indempotency.IdempotencyDistributionLock;
import com.os.config.BeanUtils;
import com.os.result.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class IdempotencyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception{
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Idempotency idempotency = method.getAnnotation(Idempotency.class);
            if(!Objects.isNull(idempotency)){
                Gson gson = BeanUtils.getBean(Gson.class);
                String reqToken =
                        request.getHeader(IdempotencyConstant.PARAMS_REQ_TOKEN_NAME);
                if(!StringUtils.isNotBlank(reqToken)){
                    log.info("=======> illegal access");
                    response.getWriter()
                            .print(
                                    gson.toJson(
                                            ResultEntity
                                                    .failed()
                                                    .message("illegal access")
                                                    .build()));
                    return false;
                }

                boolean status =
                        IdempotencyDistributionLock.getInstance().lock(reqToken);
                if(!status){
                    log.info("====> reqToken: {} has been executed", reqToken);
                    response.getWriter()
                            .print(
                                    gson.toJson(
                                            ResultEntity
                                                    .failed()
                                                    .message("has been executed")
                                                    .build()));
                    return false;
                }
            }
        }
        return true;
    }
}
