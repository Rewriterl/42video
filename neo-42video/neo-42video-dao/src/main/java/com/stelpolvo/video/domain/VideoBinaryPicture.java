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
@ApiModel(value = "视频二值图记录实体")
public class VideoBinaryPicture {

    private Long id;

    @ApiModelProperty(value = "视频id")
    private Long videoId;

    @ApiModelProperty(value = "帧数")
    private Integer frameNo;

    @ApiModelProperty(value = "图片链接")
    private String url;

    @ApiModelProperty(value = "视频时间戳")
    private Long videoTimestamp;

    private Date createTime;
}
