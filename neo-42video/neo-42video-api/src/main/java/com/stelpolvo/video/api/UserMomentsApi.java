package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.UserMoment;
import com.stelpolvo.video.service.UserMomentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户动态")
@RestController
@RequiredArgsConstructor
public class UserMomentsApi {
    private final UserMomentsService userMomentsService;

    @ApiOperation("发布动态")
    @PostMapping("/moment")
    public RespBean addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        userMomentsService.addUserMoments(userMoment);
        return RespBean.ok("发布成功");
    }

    @ApiOperation("查询动态")
    @GetMapping("/moment")
    public RespBean getUserSubscribedMoments() {
        return RespBean.ok(userMomentsService.getUserSubscribedMoments());
    }
}
