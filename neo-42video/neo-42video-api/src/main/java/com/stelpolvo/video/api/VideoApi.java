package com.stelpolvo.video.api;

import com.stelpolvo.video.annotation.Log;
import com.stelpolvo.video.domain.*;
import com.stelpolvo.video.domain.dto.VideoCriteria;
import com.stelpolvo.video.service.ElasticSearchService;
import com.stelpolvo.video.service.FileService;
import com.stelpolvo.video.service.VideoService;
import com.stelpolvo.video.service.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "视频")
@RequiredArgsConstructor
public class VideoApi {

    private final JwtUtil jwtUtil;

    private final FileService fileService;
    private final VideoService videoService;

    private final ElasticSearchService elasticSearchService;

    @PutMapping("/video/upload")
    @Log("分片上传")
    @ApiOperation("支持秒传的文件分片上传")
    public RespBean uploadOther(String md5, Integer sliceNo, Integer totalSliceNum, MultipartFile slice) throws Exception {
        return RespBean.ok(fileService.uploadFileBySlices(slice, md5, sliceNo, totalSliceNum));
    }

    @PostMapping("/video")
    @Log("上传视频")
    @ApiOperation("上传视频")
    public RespBean addVideo(@RequestBody Video video) {
        videoService.addVideo(video);
        elasticSearchService.addVideo(video);
        return RespBean.ok();
    }

    @GetMapping("/videos")
    @Log("分页查询视频")
    @ApiOperation("分页查询视频列表")
    public RespBean pageGetVideo(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam String area) {
        VideoCriteria videoCriteria = new VideoCriteria(page, pageSize, area);
        VideoCriteria videos = videoService.pageGetVideo(videoCriteria);
        return RespBean.ok(videos);
    }

    @GetMapping("/video")
    @Log("根据id查询视频")
    @ApiOperation("根据id分片播放视频")
    public void getVideoBySlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        videoService.getVideoBySlices(request, response, url);
    }

    @PostMapping("/video/like")
    @Log("点赞")
    @ApiOperation("点赞视频")
    public RespBean addVideoLike(@RequestParam Long videoId) {
        videoService.addVideoLike(videoId);
        return RespBean.ok();
    }

    @DeleteMapping("/video/like")
    @Log("取消点赞")
    @ApiOperation("取消点赞视频")
    public RespBean deleteVideoLike(@RequestParam Long videoId) {
        videoService.deleteVideoLike(videoId);
        return RespBean.ok();
    }

    @GetMapping("/video/like")
    @Log("查询点赞")
    @ApiOperation("查询视频点赞信息")
    public RespBean getVideoLikes(@RequestParam Long videoId) {
        return RespBean.ok(videoService.getVideoLikes(videoId));
    }

    @PostMapping("/video/collection/group")
    @Log("新增分组")
    @ApiOperation("新增分组")
    public RespBean addUserFollowingsGroup(@RequestBody CollectionGroup collectionGroup) {
        Long groupIds = videoService.addCollectionGroups(collectionGroup);
        return RespBean.ok(groupIds);
    }

    @DeleteMapping("/video/collection/group")
    @Log("删除分组")
    @ApiOperation("删除分组")
    public RespBean deleteUserFollowingsGroup(@RequestParam Long groupId) {
        videoService.deleteCollectionGroup(groupId);
        return RespBean.ok();
    }

    @PostMapping("/video/collection")
    @Log("收藏视频")
    @ApiOperation("收藏视频")
    public RespBean addVideoCollection(@RequestBody VideoCollection videoCollection) {
        videoService.addVideoCollection(videoCollection);
        return RespBean.ok();
    }

    @DeleteMapping("/video/collection")
    @Log("取消收藏")
    @ApiOperation("取消收藏视频")
    public RespBean deleteVideoCollection(@RequestParam Long videoId) {
        videoService.deleteVideoCollection(videoId);
        return RespBean.ok();
    }

    @GetMapping("/video/collection")
    @Log("查询收藏")
    @ApiOperation("查询视频收藏信息")
    public RespBean getVideoCollections(@RequestParam Long videoId) {
        return RespBean.ok(videoService.getVideoCollections(videoId));
    }

    @PostMapping("/video/coins")
    @Log("投币")
    @ApiOperation("投币")
    public RespBean addVideoCoins(@RequestBody VideoCoin videoCoin) {
        videoService.addVideoCoins(videoCoin);
        return RespBean.ok("投币成功");
    }

    @GetMapping("/video/coins")
    @Log("查询投币数量")
    @ApiOperation("查询视频投币数量")
    public RespBean getVideoCoins(@RequestParam Long videoId) {
        return RespBean.ok(videoService.getVideoCoins(videoId));
    }

    @GetMapping("/video/details")
    @Log("查询视频详情")
    @ApiOperation("查询视频详情")
    public RespBean getVideoDetails(@RequestParam Long videoId) {
        return RespBean.ok(videoService.getVideoDetails(videoId));
    }

    @PostMapping("/video/view")
    @Log("查询播放历史")
    @ApiOperation("播放历史")
    public RespBean addVideoView(@RequestBody VideoView videoView,
                                 HttpServletRequest request) {
        if (!jwtUtil.checkTokenAndSetAuth(request)) SecurityContextHolder.clearContext();
        videoService.addVideoViewHistory(videoView, request);
        return RespBean.ok();
    }

    @GetMapping("/video/view")
    @Log("查询播放量")
    @ApiOperation("查询视频播放量")
    public RespBean getVideoViewCounts(@RequestParam Long videoId) {
        Integer count = videoService.getVideoViewCounts(videoId);
        return RespBean.ok(count);
    }
}
