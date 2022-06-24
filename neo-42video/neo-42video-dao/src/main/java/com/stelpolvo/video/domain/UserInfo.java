package com.stelpolvo.video.domain;

import com.stelpolvo.video.domain.constant.UserConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    public UserInfo(Long userId) {
        this.userId = userId;
        this.avatar = UserConstant.DEFAULT_AVATAR;
        this.gender = UserConstant.GENDER_UNKNOW;
        this.birth = UserConstant.DEFAULT_BIRTH;
        this.createTime = new Date();
    }

    private Long id;

    private Long userId;

    private String username;

    private String avatar;

    private String sign;

    private String gender;

    private String birth;

    private Date createTime;

    private Date updateTime;

    private Boolean followed;
}
