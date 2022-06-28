package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel("文件实体")
@NoArgsConstructor
@AllArgsConstructor
public class File {

    private Long id;

    @ApiModelProperty("文件路径")
    private String url;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件md5")
    private String md5;

    @ApiModelProperty("文件上传时间")
    private Date createTime;
}
