package com.stelpolvo.video.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {
    private Long id;

    private Long userId;

    private Long videoId;

    private Float value;

    private Date createTime;
}
