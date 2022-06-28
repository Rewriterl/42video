package com.stelpolvo.video.domain.dto;

import com.stelpolvo.video.domain.Page;
import com.stelpolvo.video.domain.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCriteria extends Page<Video> {
    private String area;

    public VideoCriteria(Integer page, Integer pageSize, String area) {
        super(page, pageSize);
        this.area = area;
    }
}
