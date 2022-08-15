package com.os.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class I18nInterceptor extends LocaleChangeInterceptor {

    private static final String DEFAULT_MSG = "default_message";

    @Autowired
    private MessageSource message;

    public String getMessage(String code){
        Locale locale = LocaleContextHolder.getLocale();
        return message.getMessage(code, null, DEFAULT_MSG, locale);
    }
}
