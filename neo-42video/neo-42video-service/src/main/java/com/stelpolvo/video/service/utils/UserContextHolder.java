package com.stelpolvo.video.service.utils;

import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.service.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContextHolder {
    private final RedisTemplate redisTemplate;
    private final AppProperties appProperties;

    public User getCurrentUser(String header) {
        try {
            String jwtToken = header.replace(appProperties.getJwt().getPrefix(), "");
            return (User) redisTemplate.opsForValue().get(jwtToken);
        } catch (NullPointerException e) {
            throw new ConditionException("请先登录");
        }
    }

    public Long getCurrentUserId(){
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

}
