package com.os;

import com.os.config.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    private Map<String, AbstractKeyExpiredHandle> result;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("-------->>> key expired = {}", message.toString());
        if(Objects.isNull(result)){

            Map<String,AbstractKeyExpiredHandle> resultMap = BeanUtils.getApplicationContext()
                    .getBeansOfType(AbstractKeyExpiredHandle.class);
            if(!Objects.isNull(resultMap) && !resultMap.isEmpty()){
                result = new HashMap<>(10);
                resultMap.forEach((k,v)-> {
                    result.put(v.getClass().getName(),v);
                });
            }
        }
    }
}
