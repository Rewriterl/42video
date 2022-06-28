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
@ApiModel(value = "视频")
public class Video {

    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "视频链接")
    private String url;

    @ApiModelProperty(value = "视频封面")
    private String thumbnail;

    @ApiModelProperty(value = "视频标题")
    private String title;

    @ApiModelProperty(value = "视频类型 0自制 1转载")
    private String type;

    @ApiModelProperty(value = "视频时长")
    private String duration;

    @ApiModelProperty(value = "视频分区")
    private String area;

    @ApiModelProperty(value = "视频标签列表")
    private List<VideoTag> videoTagList;

    @ApiModelProperty(value = "视频简介")
    private String description;

    private Date createTime;

    private Date updateTime;
}
