package vanhoang.project.controller.base;

import org.springframework.context.support.MessageSourceAccessor;
import vanhoang.project.dto.base.ResponseResult;
import vanhoang.project.interceptor.I18nInterceptor;
import vanhoang.project.utils.BeanUtils;
import vanhoang.project.utils.StringUtils;

import java.util.Locale;

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

    public ResponseResult<Object> getResponseResult(String messageCode) {
        if (!StringUtils.isNoneEmpty(messageCode)) {
            return ResponseResult.success();
        }
        else {
            MessageSourceAccessor messageSourceAccessor = BeanUtils.getBean(MessageSourceAccessor.class);
            Locale locale = I18nInterceptor.LOCALE_THREADLOCAL.get();
            String message = messageSourceAccessor.getMessage(messageCode, null, messageCode, locale);
            return ResponseResult.fail(message);
        }
    }
}
