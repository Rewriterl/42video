package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "视频标签关联表")
public class VideoTag {

    private Long id;

    private Long videoId;

    private Long tagId;

    private Date createTime;
}
