package com.stelpolvo.video.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();//序列化为String
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);//序列化为Json
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        this.redisTemplate = redisTemplate;
    }
}
