package com.stelpolvo.video.service.utils;

import com.stelpolvo.video.domain.exception.ConditionException;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContextHolder {
    public static Long getCurrentUserId() {
        try {
            return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        } catch (NullPointerException e) {
            throw new ConditionException("请先登录");
        }
    }
}
