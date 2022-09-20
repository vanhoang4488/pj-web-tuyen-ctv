package com.os;

import lombok.Data;

@Data
public class JedisPoolProperties {
    private int redisPoolMaxActive;
    private int redisPoolMaxWaitMillis;
    private int redisPoolMaxIdle;
    private int redisPoolMinIdle;
}
