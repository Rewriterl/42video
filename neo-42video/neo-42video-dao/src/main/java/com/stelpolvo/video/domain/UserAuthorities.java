package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户权限实体")
public class UserAuthorities {

    @ApiModelProperty("用户元素操作权限列表")
    List<AuthRoleElementOperation> roleElementOperationList;

    @ApiModelProperty("用户菜单权限列表")
    List<AuthRoleMenu> roleMenuList;
}
