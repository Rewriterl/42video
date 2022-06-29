package com.stelpolvo.video.service.utils;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.stelpolvo.video.domain.exception.ConditionException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FastDFSUtils {

    private final FastFileStorageClient fastFileStorageClient;

    private final AppendFileStorageClient appendFileStorageClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    private static final String DEFAULT_GROUP = "group1";

    private static final String PATH_KEY = "path-key:";

    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

    private static final String UPLOADED_NO_KEY = "uploaded-no-key:";

    private static final int SLICE_SIZE = 1024 * 1024 * 2;

    @Value("${fdfs.http.storage-address}")
    private String HTTP_FASTDFS_SERVER;


    public String getFileType(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("文件类型不支持");
        }
        String filename = file.getOriginalFilename();
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public String upload(MultipartFile file) throws IOException {
        Set<MetaData> metaData = new HashSet<>();
        String fileType = getFileType(file);
        StorePath path = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaData);
        return path.getPath();
    }

    public void delete(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }

    public String sliceUploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileType = getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    public void modifySliceFile(String filePath, MultipartFile file, long offset) throws IOException {
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    public String uploadFileBySlices(MultipartFile file, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception {
        if (file == null || sliceNo == null || totalSliceNo == null) {
            throw new ConditionException("参数有误！");
        }
        String pathKey = PATH_KEY + fileMd5;
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMd5;
        String uploadedNoKey = UPLOADED_NO_KEY + fileMd5;
        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if (!StringUtil.isNullOrEmpty(uploadedSizeStr)) {
            uploadedSize = Long.valueOf(uploadedSizeStr);
        }
        if (sliceNo == 1) {
            String path = this.sliceUploadFile(file);
            if (StringUtil.isNullOrEmpty(path)) {
                throw new ConditionException("上传失败！");
            }
            redisTemplate.opsForValue().set(pathKey, path);
            redisTemplate.opsForValue().set(uploadedNoKey, "1");
        } else {
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new ConditionException("上传失败！");
            }
            this.modifySliceFile(filePath, file, uploadedSize);
            redisTemplate.opsForValue().increment(uploadedNoKey);
        }
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        Integer uploadedNo = Integer.valueOf(uploadedNoStr);
        String resultPath = "";
        if (uploadedNo.equals(totalSliceNo)) {
            resultPath = redisTemplate.opsForValue().get(pathKey);
            List<String> keyList = Arrays.asList(uploadedNoKey, pathKey, uploadedSizeKey);
            redisTemplate.delete(keyList);
        }
        return resultPath;
    }

    public void getVideoBySlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_GROUP, url);
        if (fileInfo == null) {
            throw new ConditionException("文件不存在！");
        }
        long fileSize = fileInfo.getFileSize();
        String fullUrl = HTTP_FASTDFS_SERVER + url;
        HashMap<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        String[] range;
        String rangeStr = request.getHeader("Range");
        if (StringUtil.isNullOrEmpty(rangeStr)) {
            rangeStr = "bytes=0-" + (fileSize - 1);
        }
        range = rangeStr.split("bytes=|-");
        long begin = 0;
        if (range.length >= 2) {
            begin = Long.parseLong(range[1]);
        }
        long end = fileSize - 1;
        if (range.length >= 3) {
            end = Long.parseLong(range[2]);
        }
        long length = end - begin + 1;
        String contentRange = "bytes " + begin + "-" + end + "/" + fileSize;
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType("video/mp4");
        response.setHeader("Content-Range", contentRange);
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentLength((int) length);
        HttpUtil.get(fullUrl, headers, response);
    }
}
