package com.stelpolvo.video.domain;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "视频用户点赞关联实体")
public class VideoLike {

    private Long id;

    private Long userId;

    private Long videoId;

    private Date createTime;
}
