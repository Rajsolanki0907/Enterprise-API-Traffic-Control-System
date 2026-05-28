package com.raj.traffic_control_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RateLimiterService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DefaultRedisScript<Long> rateLimiterScript;

    public boolean allowRequest(
            String username,
            String role
    ) {

        long capacity;

        long refillRate;

        switch (role) {

            case "PREMIUM":
                capacity = 100;
                refillRate = 5;
                break;

            case "ADMIN":
                return true;

            default:
                capacity = 5;
                refillRate = 1;
        }

        String tokensKey =
                "tokens:" + username + ":" + role;

        String timestampKey =
                "timestamp:" + username + ":" + role;

        Long result = redisTemplate.execute(
                rateLimiterScript,
                Arrays.asList(tokensKey, timestampKey),
                String.valueOf(refillRate),
                String.valueOf(capacity),
                String.valueOf(System.currentTimeMillis()),
                "1"
        );

        return result != null && result == 1;
    }
}