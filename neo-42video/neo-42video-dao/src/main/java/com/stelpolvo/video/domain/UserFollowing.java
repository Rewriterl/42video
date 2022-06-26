package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户与用户关注关联实体")
public class UserFollowing {

    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("关注用户id")
    private Long followingId;

    @ApiModelProperty("分组id")
    private Long groupId;

    private Date createTime;

    private UserInfo userInfo;
}
