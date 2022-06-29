package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionGroup {
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("关注分组类型 0:默认关注 1:自定义关注")
    private String type;

    private Date createTime;

    private Date updateTime;
}
