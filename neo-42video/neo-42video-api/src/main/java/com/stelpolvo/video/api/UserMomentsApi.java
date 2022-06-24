package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.UserMoment;
import com.stelpolvo.video.service.UserMomentsService;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserMomentsApi {
    private final UserMomentsService userMomentsService;

    private final UserContextHolder userContextHolder;

    @PostMapping("/moment")
    public RespBean addUserMoments(@RequestHeader("Authorization") String header, @RequestBody UserMoment userMoment) throws Exception {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        userMoment.setUserId(currentUserId);
        userMomentsService.addUserMoments(userMoment);
        return RespBean.ok("发布成功");
    }

    @GetMapping("/moment")
    public RespBean getUserSubscribedMoments(@RequestHeader("Authorization") String header) {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        return RespBean.ok(userMomentsService.getUserSubscribedMoments(currentUserId));
    }
}
