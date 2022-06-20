package com.stelpolvo.video.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthMenu {

    private Long id;

    private String name;

    private String code;

    private Date createTime;

    private Date updateTime;
}
