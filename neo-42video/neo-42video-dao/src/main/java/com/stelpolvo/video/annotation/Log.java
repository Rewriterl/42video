package com.stelpolvo.video.annotation;

import com.stelpolvo.video.enums.LogActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";

    boolean enable() default true;

    LogActionType type() default LogActionType.SELECT;
}
