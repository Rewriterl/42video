package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.VideoDao;
import com.stelpolvo.video.domain.Video;
import com.stelpolvo.video.domain.VideoTag;
import com.stelpolvo.video.domain.dto.VideoCriteria;
import com.stelpolvo.video.domain.exception.ConditionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoDao videoDao;

    @Transactional
    public void addVideo(Video video){
        Date createTime = new Date();
        video.setCreateTime(createTime);
        videoDao.addVideos(video);
        List<VideoTag> videoTagList = video.getVideoTagList();
        Long videoId = video.getId();
        videoTagList.forEach(videoTag -> {
            videoTag.setVideoId(videoId);
            videoTag.setCreateTime(createTime);
        });
        videoDao.batchAddVideoTags(videoTagList);
    }

    public VideoCriteria pageGetVideo(VideoCriteria videoCriteria) {
        if (videoCriteria.getPage() == null || videoCriteria.getPageSize() == null) {
            throw new ConditionException("参数有误");
        }
        Integer total = videoDao.pageCountVideos(videoCriteria);
        if (total > 0) {
            videoCriteria.setList(videoDao.pageListVideos(videoCriteria));
        }
        return videoCriteria;
    }
}
