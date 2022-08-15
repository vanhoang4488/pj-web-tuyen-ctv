package com.os;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public abstract class AbstractKeyExpiredHandle {


    public String getExpiredKey(RedisCacheHandler handler, String dataId){
        String expiredKey = handler.getKey(this.getClass(), dataId);
        return expiredKey;
    }

    public abstract void handleKey(String dataId);
}
