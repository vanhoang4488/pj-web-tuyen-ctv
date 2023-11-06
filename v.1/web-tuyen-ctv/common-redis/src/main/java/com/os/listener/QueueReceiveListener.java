package com.os.listener;

import com.os.config.BeanUtils;
import com.os.config.CommonConfig;
import com.os.tenant.TenantConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class QueueReceiveListener implements Runnable{

    private final StringRedisTemplate stringRedisTemplate;

    private final String queueName;

    private final AbstractMsgConsumer consumer;

    @Override
    public void run() {
        log.info("====> start consumer: {}", queueName);
        while(RedisMqConsumerContainer.run){
            CommonConfig commonConfig = BeanUtils.getBean(CommonConfig.class);
            String key = commonConfig.getRedisPrefix() + ":" + queueName;
            String msg = stringRedisTemplate.opsForList().rightPop(key, 30, TimeUnit.SECONDS);
            if(!Objects.isNull(msg)){
                String[] values = msg.split(TenantConstants.TENANT_MSG_TAG);
                if(values.length != 1){
                    msg = values[0];
                    String tenantId = values[1];
                }
                consumer.onMessage(msg);
            }
        }
    }
}
