package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "VideoApi")
@RequiredArgsConstructor
public class VideoApi {

    private final FileService fileService;

    @PutMapping("/video/upload")
    @ApiOperation("支持秒传的文件分片上传")
    public RespBean uploadOther(String md5, Integer sliceNo, Integer totalSliceNum, MultipartFile slice) throws Exception {
        return RespBean.ok(fileService.uploadFileBySlices(slice, md5, sliceNo, totalSliceNum));
    }

}
