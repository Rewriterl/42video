package com.stelpolvo.video.domain.vo;

import com.stelpolvo.video.domain.UserInfo;
import com.stelpolvo.video.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetialsVo {
    private Video video;
    private UserInfo userInfo;
}
