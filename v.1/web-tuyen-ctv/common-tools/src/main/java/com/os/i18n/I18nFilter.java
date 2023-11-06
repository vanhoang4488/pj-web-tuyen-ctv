package com.os.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.*;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
@ConditionalOnExpression("'${spring.application.name}'!='${gateway.application.name}'")
public class I18nFilter implements Filter {

    private static final ThreadLocal<Locale> I18N_THREAD_LOCAL = new ThreadLocal<>();

    /**request parameter name để hệ thống xác định ngôn ngữ nào được trả về.*/
    private static final String paramName = "lang";

    @Autowired
    private MessageSource message;


    /**
     * Vì một số message ta muốn hiển thị thẳng giá trị truyền vào thay vì
     * giá chuyển đổi.
     * @param code
     * @return
     */
    public String getMessage(String code){
        Locale locale = I18N_THREAD_LOCAL.get();
        return message.getMessage(code, null, code, locale);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String param = request.getParameter(paramName);
        log.info("------>>> request atrribute lang: {}", param);
        Locale newLocale = this.parseLocaleValue(param);
        I18N_THREAD_LOCAL.set(newLocale);
        chain.doFilter(request, response);
    }

    @Nullable
    protected Locale parseLocaleValue(String localeValue) {
        return StringUtils.parseLocale(localeValue);
    }
}
