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
@ApiModel("页面元素操作实体")
public class AuthElementOperation {

    private Long id;

    @ApiModelProperty("页面元素名")
    private String elementName;

    @ApiModelProperty("页面元素操作代码")
    private String elementCode;

    @ApiModelProperty("操作类型 0:可点击 1:可见")
    private String operationType;

    private Date createTime;

    private Date updateTime;
}
