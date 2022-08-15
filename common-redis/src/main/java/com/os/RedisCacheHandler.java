package com.os;

import com.os.config.BeanUtils;
import com.os.config.CommonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheHandler {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${spring.application.name}")
    private String appName;

    public String getKey(Class<?> clazz, String... key){
        String[] keys = new String[key.length + 1];
        keys[0] = clazz.getSimpleName();
        int index = 1;
        for(String element : key)
            keys[index++] = element;
        return getKey(keys);
    }

    public String getKey(String[] keys){
        CommonConfig commonConfig = BeanUtils.getBean(CommonConfig.class);
        String redisPrefix = commonConfig.getRedisPrefix();
        return new StringBuilder(redisPrefix)
                .append(":")
                .append(appName)
                .append(":")
                .append(Arrays.stream(keys).collect(Collectors.joining(":")))
                .toString();
    }

    public void addCacheByKey(String key, String value, int time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public String getCacheByKey(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
}
