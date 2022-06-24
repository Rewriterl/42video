package com.stelpolvo.video.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRole implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Date createTime;

    private Date updateTime;

    public AuthRole(String role_user) {
        this.name = role_user;
    }
}
