package vanhoang.project.utils;

import java.util.regex.Pattern;

public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    private final static Pattern EMAIL_PATTERN = Pattern.compile("\\w[a-zA-Z0-9._]\\{3, 15}@gmail.com");

    public static boolean checkEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
