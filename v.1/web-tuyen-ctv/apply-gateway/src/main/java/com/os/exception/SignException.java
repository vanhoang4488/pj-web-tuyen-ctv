package com.os.exception;

import com.os.result.ResultEntity;

public class SignException extends Exception{

    public SignException(String message) { super(message); }

    public SignException(Throwable ex) { super(ex); }

    public SignException(String message, Throwable ex) { super(message, ex); }

    public ResultEntity getResultEntity(){
        return ResultEntity.failed().message(this.getMessage()).build();
    }
}
