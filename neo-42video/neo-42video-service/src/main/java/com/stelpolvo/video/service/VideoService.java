package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.VideoDao;
import com.stelpolvo.video.domain.*;
import com.stelpolvo.video.domain.dto.VideoCriteria;
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.domain.vo.SomeCountVo;
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

    private final UserCoinService userCoinService;

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

    public SomeCountVo getVideoLikes(Long videoId) {
        Long userId = null;
        try {
            userContextHolder.getCurrentUserId();
        } catch (Exception ignored) {
        }
        Long likeNum = videoDao.getVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        return new SomeCountVo(likeNum, videoLike != null);
    }

    @Transactional
    public void addVideoCollection(VideoCollection videoCollection) {
        Long userId = userContextHolder.getCurrentUserId();
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if (videoId == null || groupId == null) {
            throw new ConditionException("参数异常！");
        }
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("非法视频！");
        }
        // 有可能是换组操作，如果原来有分组的话这里删除原来的分组再重新添加
        videoDao.deleteVideoCollection(videoId, userId);
        videoCollection.setUserId(userId);
        videoCollection.setCreateTime(new Date());
        videoDao.addVideoCollection(videoCollection);
    }

    public void deleteVideoCollection(Long videoId) {
        Long userId = userContextHolder.getCurrentUserId();
        videoDao.deleteVideoCollection(videoId, userId);
    }

    public SomeCountVo getVideoCollections(Long videoId) {
        Long userId = null;
        try {
            userId = userContextHolder.getCurrentUserId();
        } catch (Exception ignored) {
        }
        Long count = videoDao.getVideoCollections(videoId);
        VideoCollection videoCollection = videoDao.getVideoCollectionByVideoIdAndUserId(videoId, userId);
        return new SomeCountVo(count, videoCollection != null);
    }

    @Transactional
    public void addVideoCoins(VideoCoin videoCoin) {
        Long userId = userContextHolder.getCurrentUserId();
        Long videoId = videoCoin.getVideoId();
        Integer amount = videoCoin.getAmount();
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("非法视频！");
        }
        Integer userCoinsAmount = userCoinService.getUserCoinsAmount(userId);
        userCoinsAmount = userCoinsAmount == null ? 0 : userCoinsAmount;
        if (amount > userCoinsAmount) {
            throw new ConditionException("硬币数量不足！");
        }
        VideoCoin puttedCoin = videoDao.getVideoCoinByVideoIdAndUserId(videoId, userId);
        if (puttedCoin == null) {
            videoCoin.setUserId(userId);
            videoCoin.setCreateTime(new Date());
            videoDao.addVideoCoin(videoCoin);
        } else {
            Integer dbAmount = puttedCoin.getAmount();
            dbAmount += amount;
            videoCoin.setUserId(userId);
            videoCoin.setAmount(dbAmount);
            videoCoin.setUpdateTime(new Date());
            videoDao.updateVideoCoin(videoCoin);
        }
        userCoinService.updateUserCoinsAmount(userId, (userCoinsAmount - amount));
    }

    public SomeCountVo getVideoCoins(Long videoId) {
        Long userId = userContextHolder.getCurrentUserId();
        Long count = videoDao.getVideoCoinsAmount(videoId);
        VideoCoin videoCoins = videoDao.getVideoCoinByVideoIdAndUserId(videoId, userId);
        return new SomeCountVo(count, videoCoins != null);
    }
}
