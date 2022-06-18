package com.stelpolvo.video.service.handler;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public RespBean commonExceptionHandler(HttpServletRequest request, Exception e) {
        String message = e.getMessage();
        if (e instanceof ConditionException) {
            String code = ((ConditionException) e).getCode();
            return RespBean.error(code, message);
        }
        if (message == null) {
            message = "未知错误";
        }
        return RespBean.error("500", message);
    }
}
