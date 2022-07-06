package com.stelpolvo.video.domain;

import com.stelpolvo.video.domain.constant.UserConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户详细信息实体")
@Document(indexName = "user_infos")
public class UserInfo implements Serializable {

    public UserInfo(Long userId) {
        this.userId = userId;
        this.avatar = UserConstant.DEFAULT_AVATAR;
        this.gender = UserConstant.GENDER_UNKNOW;
        this.birth = UserConstant.DEFAULT_BIRTH;
        this.createTime = new Date();
    }

    @Id
    private Long id;

    private Long userId;

    @Field(type = FieldType.Text)
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户签名")
    private String sign;

    @ApiModelProperty("用户性别")
    private String gender;

    @ApiModelProperty("用户生日")
    private String birth;

    @Field(type = FieldType.Date)
    @ApiModelProperty("用户详细信息创建时间")
    private Date createTime;

    @Field(type = FieldType.Date)
    @ApiModelProperty("用户详细信息修改时间")
    private Date updateTime;

    @ApiModelProperty("登录用户是否已关注此用户")
    private Boolean followed;
}
