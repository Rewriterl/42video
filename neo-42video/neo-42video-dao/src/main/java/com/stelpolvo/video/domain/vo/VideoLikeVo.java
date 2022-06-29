package com.stelpolvo.video.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoLikeVo {
    private Long count;
    private boolean like;
}
