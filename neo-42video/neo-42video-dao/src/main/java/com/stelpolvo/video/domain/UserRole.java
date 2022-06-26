package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户角色管理实体")
public class UserRole {
    private Long id;

    @ApiModelProperty("关联的用户id")
    private Long userId;

    @ApiModelProperty("关联的角色id")
    private Long roleId;

    @ApiModelProperty("角色名")
    private String roleName;

    @ApiModelProperty("角色代码")
    private String roleCode;

    private Date createTime;

    public UserRole(Long userId,Long roleId){
        this.userId = userId;
        this.roleId = roleId;
    }
}
