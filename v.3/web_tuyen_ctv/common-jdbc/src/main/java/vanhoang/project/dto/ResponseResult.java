package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResult <T>{
    private int status;
    private String message;
    private T data;

    public static Builder builder() {
        return new Builder();
    }

    public static <T> ResponseResult<T> success() {
        return ResponseResult.builder()
                .status(ResponseStatus.SUCCESS.getStatus())
                .message(ResponseStatus.SUCCESS.getMessage())
                .build();
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> responseResult = success();
        responseResult.data = data;
        return responseResult;
    }

    public static ResponseResult<Object> fail() {
        return ResponseResult.builder()
                .status(ResponseStatus.FAIL.getStatus())
                .message(ResponseStatus.FAIL.getMessage())
                .build();
    }

    static class Builder {
        private int status;
        private String message;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public <T> ResponseResult<T> build() {
            ResponseResult<T> responseResult = new ResponseResult<>();
            responseResult.status = this.status;
            responseResult.message = this.message;
            return responseResult;
        }
    }
}
