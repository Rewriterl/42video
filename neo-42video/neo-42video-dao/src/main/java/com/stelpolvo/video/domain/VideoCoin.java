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
@ApiModel(value = "视频用户硬币关联实体")
public class VideoCoin {

    private Long id;

    private Long videoId;

    private Long userId;

    @ApiModelProperty(value = "硬币数量")
    private Integer amount;

    private Date createTime;

    private Date updateTime;
}
