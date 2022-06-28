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
@ApiModel(value = "视频用户评论关联实体")
public class VideoComment {

    private Long id;

    private Long videoId;

    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "回复用户id")
    private Long replyUserId;

    @ApiModelProperty(value = "评论根节点id")
    private Long rootId;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "评论子节点")
    private List<VideoComment> childList;

    @ApiModelProperty(value = "用户信息")
    private UserInfo userInfo;

    @ApiModelProperty(value = "回复用户信息")
    private UserInfo replyUserInfo;
}
