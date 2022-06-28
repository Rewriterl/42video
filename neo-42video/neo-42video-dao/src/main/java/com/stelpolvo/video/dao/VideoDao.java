package com.stelpolvo.video.dao;

import com.stelpolvo.video.domain.Video;
import com.stelpolvo.video.domain.VideoTag;
import com.stelpolvo.video.domain.dto.VideoCriteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VideoDao {
    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> videoTagList);

    Integer pageCountVideos(VideoCriteria videoCriteria);

    List<Video> pageListVideos(VideoCriteria videoCriteria);
}
