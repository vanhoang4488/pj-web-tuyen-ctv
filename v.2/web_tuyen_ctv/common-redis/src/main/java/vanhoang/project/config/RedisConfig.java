package vanhoang.project.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Setter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String hostName;
    private int port;

    private int maxIdle;
    private int minIdle;
    private int maxTotal;
    private int maxWaitMillis;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(this.hostName);
        redisConfiguration.setPort(this.port);
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisPoolingClientConfigurationBuilder =
                JedisClientConfiguration.builder().usePooling();
        jedisPoolingClientConfigurationBuilder.poolConfig(this.jedisPoolConfig());
        JedisClientConfiguration jedisClientConfiguration = jedisPoolingClientConfigurationBuilder.build();
        return new JedisConnectionFactory(redisConfiguration, jedisClientConfiguration);
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
