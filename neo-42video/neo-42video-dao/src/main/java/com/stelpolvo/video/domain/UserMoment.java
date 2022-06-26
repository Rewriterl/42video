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
@ApiModel("用户动态关联实体")
public class UserMoment {

    private Long id;

    @ApiModelProperty("关联的用户id")
    private Long userId;

    @ApiModelProperty("动态类型：1-视频，2-评论，3-点赞")
    private String type;

    @ApiModelProperty("关联的视频id")
    private Long contentId;

    private Date createTime;

    private Date updateTime;
}
