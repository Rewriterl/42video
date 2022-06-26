package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("角色实体")
public class AuthRole implements Serializable {

    private Long id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色代码")
    private String code;

    private Date createTime;

    private Date updateTime;

    public AuthRole(String role_user) {
        this.name = role_user;
    }
}
