package com.stelpolvo.video.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespBean {
    private String code;
    private String msg;
    private Object data;

    public static RespBean build() {
        return new RespBean();
    }

    public static RespBean ok(Object data) {
        return new RespBean("200", "OK", data);
    }

    public static RespBean ok() {
        return new RespBean("200", "OK", null);
    }

    public static RespBean error(String msg) {
        return new RespBean("500", msg, null);
    }

    public static RespBean error(String code, String msg) {
        return new RespBean("500", msg, null);
    }

    public RespBean() {
    }

    public RespBean(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RespBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RespBean(String code) {
        this.code = code;
    }

    public RespBean(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public static RespBean ok(String msg, Object o) {
        return new RespBean("200", msg, o);
    }
}
