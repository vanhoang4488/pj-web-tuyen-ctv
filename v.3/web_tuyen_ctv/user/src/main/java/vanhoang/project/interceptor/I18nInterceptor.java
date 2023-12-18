package vanhoang.project.interceptor;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class I18nInterceptor implements HandlerInterceptor {
    public static final ThreadLocal<Locale> LOCALE_THREADLOCAL = new ThreadLocal<>();
    private static final Locale DEFAULT_LOCALE = new Locale("vi", "VN");
    private final String languageParamName;

    public I18nInterceptor(String languageParamName) {
        this.languageParamName = languageParamName;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getParameter(this.languageParamName);
        Locale locale = LocaleUtils.toLocale(lang) != null ? LocaleUtils.toLocale(lang) : DEFAULT_LOCALE;
        LOCALE_THREADLOCAL.set(locale);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        LOCALE_THREADLOCAL.set(null);
    }
}
