package vanhoang.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vanhoang.project.interceptor.I18nInterceptor;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class UserConfig implements WebMvcConfigurer {
    private final MessageSource messageSource;
    @Value("${language.paramName}")
    private String languageParamName;

    @Bean
    @SuppressWarnings("unused")
    public MessageSourceAccessor messageSourceAccessor() {
        Locale vnLocale = new Locale("vn", "VN");
        return new MessageSourceAccessor(this.messageSource, vnLocale);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new I18nInterceptor(this.languageParamName))
                .addPathPatterns("/**");
    }
}
