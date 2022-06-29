package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户权限")
@RestController
@RequiredArgsConstructor
public class UserAuthApi {

    private final UserAuthService userAuthService;

    @ApiOperation("获取用户权限")
    @GetMapping("/user/auths")
    public RespBean getUserAuthorities() {
        return RespBean.ok(userAuthService.getUserAuthorities());
    }
}
