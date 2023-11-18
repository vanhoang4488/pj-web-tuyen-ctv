package vanhoang.project.controller.base;

import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.utils.StringUtils;

public abstract class AbstractController {

    public <T> ResponseResult<T> getResponseResult(T data){
        if (data == null || (data instanceof Boolean && !((Boolean) data))) {
            return ResponseResult.fail();
        }
        else if (data instanceof Boolean && ((Boolean) data)) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.success(data);
        }
    }

    public ResponseResult<Object> getResponseResult(String message) {
        if (!StringUtils.isNoneEmpty(message)) {
            return ResponseResult.success();
        }
        else
            return ResponseResult.fail(message);
    }
}
