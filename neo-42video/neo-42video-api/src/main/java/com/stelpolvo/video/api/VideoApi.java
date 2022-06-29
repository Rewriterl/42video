package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.*;
import com.stelpolvo.video.domain.dto.VideoCriteria;
import com.stelpolvo.video.service.FileService;
import com.stelpolvo.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "视频")
@RequiredArgsConstructor
public class VideoApi {

    private final FileService fileService;
    private final VideoService videoService;

    @PutMapping("/video/upload")
    @ApiOperation("支持秒传的文件分片上传")
    public RespBean uploadOther(String md5, Integer sliceNo, Integer totalSliceNum, MultipartFile slice) throws Exception {
        return RespBean.ok(fileService.uploadFileBySlices(slice, md5, sliceNo, totalSliceNum));
    }

    @PostMapping("/video")
    @ApiOperation("添加视频")
    public RespBean addVideo(Video video) {
        videoService.addVideo(video);
        return RespBean.ok();
    }

    @GetMapping("/videos")
    @ApiOperation("分页查询视频列表")
    public RespBean pageGetVideo(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam String area) {
        VideoCriteria videoCriteria = new VideoCriteria(page, pageSize, area);
        VideoCriteria videos = videoService.pageGetVideo(videoCriteria);
        return RespBean.ok(videos);
    }

    @GetMapping("/video")
    @ApiOperation("根据id分片播放视频")
    public void getVideoBySlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        videoService.getVideoBySlices(request, response, url);
    }

    @PostMapping("/video/like")
    @ApiOperation("点赞视频")
    public RespBean addVideoLike(@RequestParam Long videoId) {
        videoService.addVideoLike(videoId);
        return RespBean.ok();
    }

    @DeleteMapping("/video/like")
    @ApiOperation("取消点赞视频")
    public RespBean deleteVideoLike(@RequestParam Long videoId) {
        videoService.deleteVideoLike(videoId);
        return RespBean.ok();
    }

    @GetMapping("/video/like")
    @ApiOperation("查询视频点赞信息")
    public RespBean getVideoLikes(@RequestParam Long videoId) {
        return RespBean.ok(videoService.getVideoLikes(videoId));
    }

    @PostMapping("/video/collection/group")
    @ApiOperation("新增分组")
    public RespBean addUserFollowingsGroup(@RequestBody CollectionGroup collectionGroup) {
        Long groupIds = videoService.addCollectionGroups(collectionGroup);
        return RespBean.ok(groupIds);
    }

    @DeleteMapping("/video/collection/group")
    @ApiOperation("删除分组")
    public RespBean deleteUserFollowingsGroup(@RequestParam Long groupId) {
        videoService.deleteCollectionGroup(groupId);
        return RespBean.ok();
    }

    @PostMapping("/video/collection")
    @ApiOperation("收藏视频")
    public RespBean addVideoCollection(@RequestBody VideoCollection videoCollection){
        videoService.addVideoCollection(videoCollection);
        return RespBean.ok();
    }

    @DeleteMapping("/video/collection")
    @ApiOperation("取消收藏视频")
    public RespBean deleteVideoCollection(@RequestParam Long videoId){
        videoService.deleteVideoCollection(videoId);
        return RespBean.ok();
    }

    @GetMapping("/video/collection")
    @ApiOperation("查询视频收藏信息")
    public RespBean getVideoCollections(@RequestParam Long videoId){
        return RespBean.ok(videoService.getVideoCollections(videoId));
    }

    @PostMapping("/video/coins")
    @ApiOperation("投币")
    public RespBean addVideoCoins(@RequestBody VideoCoin videoCoin){
        videoService.addVideoCoins(videoCoin);
        return RespBean.ok("投币成功");
    }

    @GetMapping("/video/coins")
    @ApiOperation("查询视频投币数量")
    public RespBean getVideoCoins(@RequestParam Long videoId){
        return RespBean.ok(videoService.getVideoCoins(videoId));
    }

    @GetMapping("/video/details")
    @ApiOperation("查询视频详情")
    public RespBean getVideoDetails(@RequestParam Long videoId){
        return RespBean.ok(videoService.getVideoDetails(videoId));
    }
}
