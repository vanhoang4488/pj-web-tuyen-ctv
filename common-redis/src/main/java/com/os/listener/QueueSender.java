package com.os.listener;

import com.os.config.BeanUtils;
import com.os.config.CommonConfig;
import com.os.tenant.TenantConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Đây là lớp trung gian chịu trách nhiệm lưu thông tin thay đổi hệ thống vào trong redis.
 * Để lớp QueueReceiveListener phát hiện và xử lý các thay đổi.
 */

@Component
@RequiredArgsConstructor
public class QueueSender {

    private final StringRedisTemplate stringRedisTemplate;

    public void onMessage(QueueName queueName, String message){
        ThreadLocal threadLocal = TenantConstants.TENANT_THREAD_LOCAL;
        String tenantId = (String) threadLocal.get();
        if(!Objects.isNull(tenantId)){
            String delimiter = TenantConstants.TENANT_MSG_TAG;
            message = message + delimiter + tenantId;
        }

        CommonConfig commonConfig = BeanUtils.getBean(CommonConfig.class);
        String key = commonConfig.getRedisPrefix() + ":" + queueName.getKey();
        stringRedisTemplate.opsForList().leftPush(key, message);
    }
}
