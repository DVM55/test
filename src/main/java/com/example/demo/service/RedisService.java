package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final DefaultRedisScript<Long> flashSaleOrderScript;
    private final DefaultRedisScript<Long> flashSaleRollbackScript;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setWithTTL(String key, Object value, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, duration, unit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long executeFlashSaleOrder(String stockKey, String userBoughtKey, int quantity, int limitPerUser) {
        return redisTemplate.execute(
                flashSaleOrderScript,
                Arrays.asList(stockKey, userBoughtKey),
                quantity,
                limitPerUser
        );
    }

    public Long rollbackFlashSaleOrder(String stockKey, String userBoughtKey, int quantity) {
        return redisTemplate.execute(
                flashSaleRollbackScript,
                Arrays.asList(stockKey, userBoughtKey),
                quantity
        );
    }
}
