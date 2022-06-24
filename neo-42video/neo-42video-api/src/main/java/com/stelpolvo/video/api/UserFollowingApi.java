package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.FollowingGroup;
import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.UserFollowing;
import com.stelpolvo.video.service.UserFollowingService;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserFollowingApi {
    private final UserFollowingService userFollowingService;

    private final UserContextHolder userContextHolder;

    @PostMapping("/following")
    public RespBean addUserFollowing(@RequestBody UserFollowing userFollowing, @RequestHeader("Authorization") String header) {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        userFollowing.setUserId(currentUserId);
        userFollowingService.addUserFollowings(userFollowing);
        return RespBean.ok("关注成功！");
    }

    @GetMapping("/fans")
    public RespBean getUserFans(@RequestHeader("Authorization") String header) {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        List<UserFollowing> userFans = userFollowingService.getUserFans(currentUserId);
        return RespBean.ok(userFans);
    }

    @GetMapping("/followings")
    public RespBean getUserFollowings(@RequestHeader("Authorization") String header) {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        List<FollowingGroup> userFollowings = userFollowingService.getUserFollowings(currentUserId);
        return RespBean.ok(userFollowings);
    }

    @PostMapping("/groups")
    public RespBean addUserFollowingsGroup(@RequestHeader("Authorization") String header, @RequestBody FollowingGroup followingGroup) {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        followingGroup.setUserId(currentUserId);
        Long groupIds = userFollowingService.addUserFollowingGroups(followingGroup);
        return RespBean.ok(groupIds);
    }

    @GetMapping("/groups")
    public RespBean getUserFollowingGroups(@RequestHeader("Authorization") String header) {
        Long currentUserId = userContextHolder.getCurrentUser(header).getId();
        List<FollowingGroup> userFollowingGroups = userFollowingService.getUserFollowingGroups(currentUserId);
        return RespBean.ok(userFollowingGroups);
    }
}
