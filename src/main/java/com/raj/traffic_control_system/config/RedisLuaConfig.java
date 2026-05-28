package com.raj.traffic_control_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisLuaConfig {

    @Bean
    public DefaultRedisScript<Long> rateLimiterScript() {

        DefaultRedisScript<Long> redisScript =
                new DefaultRedisScript<>();

        redisScript.setLocation(
                new ClassPathResource("tokenBucket.lua")
        );

        redisScript.setResultType(Long.class);

        return redisScript;
    }
}