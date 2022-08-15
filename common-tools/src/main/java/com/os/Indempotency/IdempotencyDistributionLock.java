package com.os.Indempotency;


import com.os.config.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.TimeUnit;

public final class IdempotencyDistributionLock {

    private final static long EX_TIME = 600;

    private StringRedisTemplate stringRedisTemplate;

    private volatile static IdempotencyDistributionLock instance;

    private IdempotencyDistributionLock(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public static IdempotencyDistributionLock getInstance(){
        if(instance == null){
            synchronized (IdempotencyDistributionLock.class){
                if(instance == null){
                    StringRedisTemplate stringRedisTemplate =
                            BeanUtils.getBean(StringRedisTemplate.class);
                    instance = new IdempotencyDistributionLock(stringRedisTemplate);
                }
            }
        }
        return instance;
    }

    public boolean lock(String lockToken){
        String token = "lock:" + lockToken;
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection
                        .set(token.getBytes(),
                                token.getBytes(),
                                Expiration.from(EX_TIME, TimeUnit.SECONDS),
                                RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
        });
    }
}
