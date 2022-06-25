package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.service.UserAuthService;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAuthApi {

    private final UserAuthService userAuthService;

    private final UserContextHolder userContextHolder;

    @GetMapping("/user/auths")
    public RespBean getUserAuthorities() {
        return RespBean.ok(userAuthService.getUserAuthorities(userContextHolder.getCurrentUserId()));
    }
}
