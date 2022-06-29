package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.VideoDao;
import com.stelpolvo.video.domain.Video;
import com.stelpolvo.video.domain.VideoLike;
import com.stelpolvo.video.domain.VideoTag;
import com.stelpolvo.video.domain.dto.VideoCriteria;
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.domain.vo.VideoLikeVo;
import com.stelpolvo.video.service.utils.FastDFSUtils;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoDao videoDao;

    private final FastDFSUtils fastDFSUtils;

    private final UserContextHolder userContextHolder;

    @Transactional
    public void addVideo(Video video) {
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

    public void getVideoBySlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        fastDFSUtils.getVideoBySlices(request, response, url);
    }

    public void addVideoLike(Long videoId) {
        Long userId = userContextHolder.getCurrentUserId();
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("非法视频");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (videoLike != null) {
            throw new ConditionException("已点赞");
        }
        videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());
        videoDao.addVideoLike(videoLike);
    }

    public void deleteVideoLike(Long videoId) {
        Long userId = userContextHolder.getCurrentUserId();
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("非法视频");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (videoLike == null) {
            throw new ConditionException("尚未点赞");
        }
        videoDao.deleteVideoLike(videoId, userId);
    }

    public VideoLikeVo getVideoLikes(Long videoId) {
        Long userId = userContextHolder.getCurrentUserId();
        Long likeNum = videoDao.getVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        return new VideoLikeVo(likeNum, videoLike != null);
    }
}
