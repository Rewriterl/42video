package com.stelpolvo.video.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowing {

    private Long id;

    private Long userId;

    private Long followingId;

    private Long groupId;

    private Date createTime;

    private UserInfo userInfo;
}
