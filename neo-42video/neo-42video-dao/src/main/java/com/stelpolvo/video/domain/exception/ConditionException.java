package com.stelpolvo.video.domain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String code;

    public ConditionException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public ConditionException(String name) {
        super(name);
        code = "500";
    }
}
