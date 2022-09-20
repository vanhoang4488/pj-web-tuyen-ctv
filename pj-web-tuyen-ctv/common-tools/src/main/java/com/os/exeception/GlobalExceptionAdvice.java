package com.os.exeception;

import com.os.result.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResultEntity bindingExceptionAdvice(Throwable ex){
        log.error("=====> bindingException: {}", ex.getMessage(), ex);
        BeanPropertyBindingResult bindingResult = null;
        if(ex instanceof BindException){
            bindingResult =
                (BeanPropertyBindingResult)((BindException) ex).getBindingResult();
        }
        else {
            bindingResult =
                    (BeanPropertyBindingResult) ((MethodArgumentNotValidException) ex).getBindingResult();
        }

        String message = "";
        if(!Objects.isNull(bindingResult)){
            message = bindingResult.getFieldError().getDefaultMessage();
        }
        else{
            message = "common.sys.exception.unknow";
        }
        return ResultEntity.failed().message(message).build();
    }

    @ExceptionHandler(value = {Exception.class})
    public ResultEntity otherExeption(Throwable ex){
        log.error("=====> other exception: {}", ex.getMessage(), ex);
        return ResultEntity.failed()
                .message("common.sys.error")
                .build();
    }
}
