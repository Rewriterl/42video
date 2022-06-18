package com.stelpolvo.video.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {

    private Long id;

    private Long userId;

    private String avatar;

    private String sign;

    private String gender;

    private String birth;

    private Date createTime;

    private Date updateTime;

    private Boolean followed;
}
