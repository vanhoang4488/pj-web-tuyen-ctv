package vanhoang.project.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.TimeZone;

@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return appContext.getBean(clazz);
    }

    public static Calendar getTimeZone() {
        String serverTimeZone = appContext.getEnvironment().getProperty("common.db.serverTimeZone");
        return Calendar.getInstance(TimeZone.getTimeZone(serverTimeZone));
    }
}
