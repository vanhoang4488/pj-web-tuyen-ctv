package com.os;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.pool.max-active}")
    private int redisPoolMaxActive;

    @Value("${spring.redis.pool.max-wait-millis}")
    private int redisPoolMaxWaitMillis;

    @Value("${spring.redis.pool.max-idle}")
    private int redisPoolMaxIdle;

    @Value("${string.redis.pool.min-idle}")
    private int redisPoolMinIdle;

    public JedisPoolConfig jedisPoolConfig(JedisPoolProperties jedisPoolProperties){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        // số lượng tối đa kết nối.
        jedisPoolConfig.setMaxTotal(jedisPoolProperties.getRedisPoolMaxActive());

        // số lượng tối thiều kết nối.
        jedisPoolConfig.setMinIdle(jedisPoolConfig.getMinIdle());

        // thời gian tối đa để 1 kết nối vượt mức tối thiểu không có kết nối.
        jedisPoolConfig.setMaxWaitMillis(jedisPoolProperties.getRedisPoolMaxWaitMillis());

        return jedisPoolConfig;
    }

    public JedisConnectionFactory jedisConnectionFactory(String host,
                                                         int port,
                                                         String password,
                                                         int database,
                                                         JedisPoolConfig jedisPoolConfig){
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jccb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)
                JedisClientConfiguration.builder();

        jccb.poolConfig(jedisPoolConfig);
        JedisClientConfiguration jedisClientConfiguration = jccb.build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    public JedisPoolProperties getJedisPoolProperties(){
        JedisPoolProperties jedisPoolProperties = new JedisPoolProperties();
        jedisPoolProperties.setRedisPoolMaxActive(redisPoolMaxActive);
        jedisPoolProperties.setRedisPoolMaxWaitMillis(redisPoolMaxWaitMillis);
        jedisPoolProperties.setRedisPoolMaxIdle(redisPoolMaxIdle);
        jedisPoolProperties.setRedisPoolMinIdle(redisPoolMinIdle);
        return jedisPoolProperties;
    }

    @Bean(name = "stringRedisTemplate")
    @Primary
    public StringRedisTemplate stringRedisTemplate(){
        JedisPoolProperties jedisPoolProperties = getJedisPoolProperties();
        return new StringRedisTemplate(
                jedisConnectionFactory(host, port, password, database,
                        this.jedisPoolConfig(jedisPoolProperties)));
    }

    @Bean(name = "redisMessageListenerContainer")
    @Primary
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        JedisPoolProperties jedisPoolProperties = getJedisPoolProperties();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(
                jedisConnectionFactory(host, port, password, database,
                        this.jedisPoolConfig(jedisPoolProperties)));
        return container;
    }
}
