package com.stelpolvo.video.domain;

import com.stelpolvo.video.domain.constant.UserConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户详细信息实体")
public class UserInfo implements Serializable {

    public UserInfo(Long userId) {
        this.userId = userId;
        this.avatar = UserConstant.DEFAULT_AVATAR;
        this.gender = UserConstant.GENDER_UNKNOW;
        this.birth = UserConstant.DEFAULT_BIRTH;
        this.createTime = new Date();
    }

    private Long id;

    private Long userId;

    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户签名")
    private String sign;

    @ApiModelProperty("用户性别")
    private String gender;

    @ApiModelProperty("用户生日")
    private String birth;

    @ApiModelProperty("用户详细信息创建时间")
    private Date createTime;

    @ApiModelProperty("用户详细信息修改时间")
    private Date updateTime;

    @ApiModelProperty("登录用户是否已关注此用户")
    private Boolean followed;
}
