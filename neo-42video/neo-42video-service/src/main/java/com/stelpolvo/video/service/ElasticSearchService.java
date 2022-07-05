package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.repository.VideoRepository;
import com.stelpolvo.video.domain.Video;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final VideoRepository videoRepository;

    private final UserContextHolder userContext;

    public void addVideo(Video video) {
        video.setUserId(userContext.getCurrentUserId());
        videoRepository.save(video);
    }

    public void deleteVideo(Video video) {
        videoRepository.delete(video);
    }

    public void deleteAllVideos() {
        videoRepository.deleteAll();
    }

    public void updateVideo(Video video) {
        videoRepository.save(video);
    }

    public Video getVideo(String keyword) {
        return videoRepository.findByTitleLike(keyword);
    }
}
