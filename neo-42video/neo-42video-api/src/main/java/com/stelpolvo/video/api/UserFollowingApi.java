package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.FollowingGroup;
import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.UserFollowing;
import com.stelpolvo.video.service.UserFollowingService;
import com.stelpolvo.video.service.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserFollowingApi {
    @Autowired
    private UserFollowingService userFollowingService;

    @PostMapping("/user-following")
    public RespBean addUserFollowing(@RequestBody UserFollowing userFollowing) {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        userFollowing.setUserId(currentUserId);
        userFollowingService.addUserFollowings(userFollowing);
        return RespBean.ok("关注成功！");
    }

    @GetMapping("/user-fans")
    public RespBean getUserFans() {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        List<UserFollowing> userFans = userFollowingService.getUserFans(currentUserId);
        return RespBean.ok(userFans);
    }

    @GetMapping("/user-followings")
    public RespBean getUserFollowings() {
        Long currentUserId = UserContextHolder.getCurrentUserId();
        List<FollowingGroup> userFollowings = userFollowingService.getUserFollowings(currentUserId);
        return RespBean.ok(userFollowings);
    }
}
