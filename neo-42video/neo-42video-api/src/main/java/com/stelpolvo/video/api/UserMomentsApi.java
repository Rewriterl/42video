package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.UserMoment;
import com.stelpolvo.video.service.UserMomentsService;
import com.stelpolvo.video.service.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMomentsApi {
    @Autowired
    private UserMomentsService userMomentsService;

    @PostMapping("/moment")
    public RespBean addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        userMoment.setUserId(currentUserId);
        userMomentsService.addUserMoments(userMoment);
        return RespBean.ok("发布成功");
    }

    @GetMapping("/moment")
    public RespBean getUserSubscribedMoments() {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        return RespBean.ok(userMomentsService.getUserSubscribedMoments(currentUserId));
    }
}
