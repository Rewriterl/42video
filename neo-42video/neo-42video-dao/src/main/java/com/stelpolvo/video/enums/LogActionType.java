package com.stelpolvo.video.enums;

public enum LogActionType {
    ADD("新增"),
    SELECT("查询"),
    UPDATE("更新"),
    DELETE("删除");
    private String value;

    LogActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
