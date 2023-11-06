package vanhoang.project.dto.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResult <T>{
    private int status;
    private String message;
    private T data;

    public static <T> ResponseResult<T> success() {
        return ResponseResult.builder()
                .status(ResponseStatus.SUCCESS.getStatus())
                .message(ResponseStatus.SUCCESS.getMessage())
                .build();
    }
    
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> responseResult = ResponseResult.success();
        responseResult.setData(data);
        return responseResult;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private int status;
        private String message;
        
        public Builder status(int status) {
            this.status = status;
            return this;
        }
        
        public Builder message(String message){
            this.message = message;
            return this;
        }
        
        public <T> ResponseResult<T> build() {
            ResponseResult<T> responseResult = new ResponseResult<>();
            responseResult.setStatus(this.status);
            responseResult.setMessage(this.message);
            return responseResult;
        }
    }
}
