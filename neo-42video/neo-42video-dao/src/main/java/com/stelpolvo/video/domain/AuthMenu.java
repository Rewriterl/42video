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
@ApiModel("菜单实体")
public class AuthMenu {

    private Long id;

    @ApiModelProperty("菜单名")
    private String name;

    @ApiModelProperty("菜单代码")
    private String code;

    private Date createTime;

    private Date updateTime;
}
