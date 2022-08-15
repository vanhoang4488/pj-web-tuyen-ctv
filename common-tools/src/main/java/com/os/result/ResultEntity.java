package com.os.result;

import com.os.config.BeanUtils;
import com.os.i18n.I18nInterceptor;
import lombok.Data;

@Data
public class ResultEntity<T>{

    /**trạng thái hoàn thành request*/
    private boolean ok;

    /**thông điệp trả về*/
    private String message;

    public ResultEntity() {}

    public ResultEntity(Builder<T> builder){
        this.ok = builder.ok;
        this.message = builder.message;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder<T> {

        private boolean ok;
        private String message;

        public Builder ok(boolean ok){
            this.ok = ok;
            return this;
        }

        public Builder message(String message){
            I18nInterceptor i18n = BeanUtils.getBean(I18nInterceptor.class);
            this.message = i18n.getMessage(message);
            return this;
        }

        public ResultEntity build(){
            return new ResultEntity(this);
        }
    }

    public static ResultEntity.Builder success(){
        return ResultEntity.builder().ok(true).message("success");
    }

    public static ResultEntity.Builder failed(){
        return ResultEntity.builder().ok(false).message("failed");
    }
}
