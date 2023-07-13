package com.weiwei.greatwisdom.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author weiwei
 * @Date 2023/7/5 23:10
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedisRedissonConfig {
    private Integer database;
    private String host;
    private Integer port;
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 根据你的Redis配置，创建合适的RedisConnectionFactory实例，
        // 例如 JedisConnectionFactory 或 LettuceConnectionFactory
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);
        config.setDatabase(database);

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 其他属性的配置
        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        return Redisson.create(config);
    }
}
