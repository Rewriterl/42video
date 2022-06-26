package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("关注分组关联实体")
public class FollowingGroup {

    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("关注分组类型 0:特别关注 1:私密关注 2:公开关注 3:自定义关注")
    private String type;

    private Date createTime;

    private Date updateTime;

    private List<UserInfo> followingUserInfoList;
}
