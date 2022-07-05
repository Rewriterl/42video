package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "videos")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "视频")
public class Video {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "视频链接")
    private String url;


    @ApiModelProperty(value = "视频封面")
    private String thumbnail;

    @Field(type = FieldType.Text)
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

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "视频简介")
    private String description;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Date)
    private Date updateTime;
}
