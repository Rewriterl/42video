package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "视频观看记录表")
public class VideoView {

    private Long id;

    private Long videoId;

    private Long userId;

    private String clientId;

    private String ip;

    private Date createTime;
}
