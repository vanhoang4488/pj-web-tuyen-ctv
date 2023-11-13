package vanhoang.project.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import vanhoang.project.dto.base.ResponseResult;

@Aspect
public class ControllerAspect {

    @AfterReturning(pointcut = "@within(org.springframework.web.bind.annotation.*)", returning = "returnValue")
    public ResponseResult<Object> responseResultJoinpoint(Object returnValue) {
        if (returnValue == null) {
            return ResponseResult.fail();
        }
        else {
            return ResponseResult.success(returnValue);
        }
    }
}
