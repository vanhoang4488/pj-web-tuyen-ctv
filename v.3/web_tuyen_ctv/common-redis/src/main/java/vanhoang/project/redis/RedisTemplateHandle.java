package vanhoang.project.redis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisTemplateHandle {
    private final static long TIMEOUT_SECONDS = 1200;
    private final static String SYMBOL = ":";
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;
    @Value("environment.prefix")
    private String environmentPrefix;
    @Value("spring.application.name")
    private String applicationName;
    @Value("${token.paramName")
    private String tokenParamName;

    public <T> T getCache(Class<?> clazz, String key, Type type) {
        String valueStr = stringRedisTemplate.opsForValue().get(this.getKey(clazz, key));
        if (StringUtils.isNotEmpty(valueStr)) {
            return gson.fromJson(valueStr, type);
        }
        return null;
    }

    public <T> T getCache(Class<?> clazz, String key, Class<T> type) {
        String valueStr = stringRedisTemplate.opsForValue().get(this.getKey(clazz, key));
        if (StringUtils.isNotEmpty(valueStr)) {
            return gson.fromJson(valueStr, type);
        }
        return null;
    }

    public void addCache(Class<?> clazz, String key, Object value) {
        stringRedisTemplate.opsForValue()
                .set(this.getKey(clazz, key), gson.toJson(value), TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private String getKey(Class<?> clazz, String key) {
        return this.environmentPrefix + SYMBOL +
                this.applicationName + SYMBOL +
                clazz.getSimpleName() + SYMBOL +
                key;
    }


    /**
     * tạm thời chưa bật xác thực nên mặc định trả về 8888 - id superadmin
     */
    public Long getUserId(HttpServletRequest request) {
        String token = request.getParameter(this.tokenParamName);
        return 8888L;
    }
}
