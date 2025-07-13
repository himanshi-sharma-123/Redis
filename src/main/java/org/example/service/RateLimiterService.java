package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS = 5;
    private static final Duration TTL = Duration.ofMinutes(1);

    public boolean isAllowed(String userId){
        String key = "rate-limit:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            //First request: set TTL
            redisTemplate.expire(key, TTL);
        }

        return count <= MAX_REQUESTS;
    }

    public long getRemainingTTL(String userId){
        String key = "rate-limit:" + userId;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
