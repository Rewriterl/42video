package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.Video;
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
    public RespBean addVideo(Video video) {
        videoService.addVideo(video);
        return RespBean.ok();
    }

    @GetMapping("/videos")
    public RespBean pageGetVideo(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam String area) {
        VideoCriteria videoCriteria = new VideoCriteria(page, pageSize, area);
        VideoCriteria videos = videoService.pageGetVideo(videoCriteria);
        return RespBean.ok(videos);
    }

    @GetMapping("/video")
    public void getVideoBySlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        videoService.getVideoBySlices(request, response, url);
    }
}
