package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.FileDao;
import com.stelpolvo.video.domain.File;
import com.stelpolvo.video.service.utils.FastDFSUtils;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileDao fileDao;

    private final FastDFSUtils fastDFSUtil;

    public String uploadFileBySlices(MultipartFile slice, String fileMD5, Integer sliceNo,
                                     Integer totalSliceNo) throws Exception {
        File file = fileDao.getFileByMD5(fileMD5);
        if (file != null) {
            return file.getUrl();
        }
        String url = fastDFSUtil.uploadFileBySlices(slice, fileMD5, sliceNo, totalSliceNo);
        if (!StringUtil.isNullOrEmpty(url)) {
            file = new File();
            file.setCreateTime(new Date());
            file.setMd5(fileMD5);
            file.setUrl(url);
            file.setType(fastDFSUtil.getFileType(slice));
            fileDao.addFile(file);
        }
        return url;
    }
}
