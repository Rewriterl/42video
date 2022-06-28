package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.service.utils.FastDFSUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "VideoApi")
@RequiredArgsConstructor
public class VideoApi {

    private final FastDFSUtils fastDFSUtils;

    @PutMapping("/video/upload")
    public RespBean uploadOther(String md5, Integer sliceNo, Integer totalSliceNum, MultipartFile slice) throws Exception {
        return RespBean.ok(fastDFSUtils.uploadFileBySlices(slice, md5, sliceNo, totalSliceNum));
    }
}
