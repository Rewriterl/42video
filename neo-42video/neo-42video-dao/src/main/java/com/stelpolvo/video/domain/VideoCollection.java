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
@ApiModel(value = "视频用户收藏关联实体")
public class VideoCollection {

    private Long id;

    private Long videoId;

    private Long userId;

    @ApiModelProperty(value = "收藏分组id")
    private Long groupId;

    private Date createTime;
}
