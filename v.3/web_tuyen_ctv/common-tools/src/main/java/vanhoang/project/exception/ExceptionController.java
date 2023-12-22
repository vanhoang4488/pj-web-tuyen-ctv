package vanhoang.project.exception;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import vanhoang.project.dto.base.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Set;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
@ConditionalOnBean(MessageSourceAccessor.class)
public class ExceptionController {
    private final MessageSourceAccessor messageSourceAccessor;
    @Value("${language.paramName:lang}")
    private String languageParamName;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SuppressWarnings("unused")
    public ResponseResult<Object>
        methodArgumentNotValidException(HttpServletRequest request,
                                        MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        String messageCode = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        String message = this.convertMessage(request, messageCode);
        return ResponseResult.fail(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SuppressWarnings("unused")
    public ResponseResult<Object>
        constraintViolationException(HttpServletRequest request,
                                     ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String messageCode = violations.stream().findFirst().orElseThrow().getMessage();
        String message = this.convertMessage(request, messageCode);
        return ResponseResult.fail(message);
    }

    private String convertMessage(HttpServletRequest request, String messageCode) {
        String lang = request.getParameter(this.languageParamName);
        Locale locale = LocaleUtils.toLocale(lang);
        return this.messageSourceAccessor.getMessage(messageCode, null, messageCode, locale);
    }
}
