package com.stelpolvo.video.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("认证响应实体")
public class Auth implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("访问token")
    private String accessToken;
    @ApiModelProperty("刷新token")
    private String refreshToken;
}
