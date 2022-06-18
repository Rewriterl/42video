package com.stelpolvo.video.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserInfo {

    public UserInfo(Long userId) {
        this.userId = userId;
        this.avatar = UserConstant.DEFAULT_AVATAR;
        this.gender = UserConstant.GENDER_UNKNOW;
        this.birth = UserConstant.DEFAULT_BIRTH;
        this.createTime = new Date();
    }

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
