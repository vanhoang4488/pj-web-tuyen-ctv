package vanhoang.project.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String hostName;
    private int port;
    private int database;
    private int minIdle;
    private int maxIdle;
    private int maxTotal;
    private int maxWaitMillis;

    @Bean
    public JedisConnectionFactory jedisClientConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(this.hostName);
        redisStandaloneConfiguration.setPort(this.port);
        redisStandaloneConfiguration.setDatabase(this.database);
        JedisClientConfiguration jedisClientConfiguration =
                JedisClientConfiguration.builder()
                        .usePooling()
                        .poolConfig(this.jedisPoolConfig())
                        .build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(this.minIdle);
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMaxTotal(this.maxTotal);
        jedisPoolConfig.setMaxWait(Duration.ofMillis(this.maxWaitMillis));
        return jedisPoolConfig;
    }
}
