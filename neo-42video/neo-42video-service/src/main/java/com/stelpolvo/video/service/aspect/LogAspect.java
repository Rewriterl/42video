package com.stelpolvo.video.service.aspect;

import com.stelpolvo.video.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class LogAspect {

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    @Pointcut("@annotation(com.stelpolvo.video.annotation.Log)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Log annotation = methodSignature.getMethod().getAnnotation(Log.class);
        try {
            result = joinPoint.proceed();
        } finally {
            generateLog(annotation, signature);
        }
        return result;
    }

    public void generateLog(Log annotation, Signature signature) {
        log.info("操作: {}", annotation.value());
        log.info("请求方法: {}", signature.getName());
        log.info("请求耗时: {}s", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - currentTime.get()));
    }
}
