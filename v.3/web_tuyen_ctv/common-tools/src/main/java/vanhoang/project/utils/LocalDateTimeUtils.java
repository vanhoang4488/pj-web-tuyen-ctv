package vanhoang.project.utils;

import java.util.Calendar;
import java.util.Date;

public abstract class LocalDateTimeUtils {

    public  static Date getNow() {
        Calendar calendar = BeanUtils.getCalendar();
        return calendar.getTime();
    }

    public static Long getMillisNow() {
        Calendar calendar = BeanUtils.getCalendar();
        return calendar.getTimeInMillis();
    }
}
