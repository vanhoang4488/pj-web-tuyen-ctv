package com.os.result;

import com.os.config.BeanUtils;
import com.os.i18n.I18nFilter;
import lombok.Data;

@Data
public class ResultEntity<T>{

    /**trạng thái hoàn thành request*/
    private boolean ok;

    /**thông điệp trả về*/
    private String message;

    private T entity;

    public ResultEntity(Builder<T> builder){
        this.ok = builder.ok;
        this.message = builder.message;
        this.entity = builder.entity;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder<T> {

        private boolean ok;
        private String message;
        private T entity;

        public Builder ok(boolean ok){
            this.ok = ok;
            return this;
        }

        public Builder message(String message){
            I18nFilter i18n = BeanUtils.getBean(I18nFilter.class);
            this.message = i18n.getMessage(message);
            return this;
        }

        public Builder entity(T entity){
            this.entity = entity;
            return this;
        }

        public ResultEntity build(){
            return new ResultEntity(this);
        }
    }

    public static ResultEntity.Builder success(){
        return ResultEntity.builder().ok(true).message("common.request.success");
    }

    public static ResultEntity.Builder failed(){
        return ResultEntity.builder().ok(false).message("common.sys.error");
    }
}
