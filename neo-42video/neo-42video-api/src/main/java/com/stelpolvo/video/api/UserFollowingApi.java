package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.FollowingGroup;
import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.UserFollowing;
import com.stelpolvo.video.service.UserFollowingService;
import com.stelpolvo.video.service.utils.UserContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户关注")
@RestController
@RequiredArgsConstructor
public class UserFollowingApi {
    private final UserFollowingService userFollowingService;

    private final UserContextHolder userContextHolder;

    @ApiOperation("新增关注")
    @PostMapping("/following")
    public RespBean addUserFollowing(@RequestBody UserFollowing userFollowing) {
        Long currentUserId = userContextHolder.getCurrentUserId();
        userFollowing.setUserId(currentUserId);
        userFollowingService.addUserFollowings(userFollowing);
        return RespBean.ok("关注成功！");
    }

    @ApiOperation("查询全部关注我的人")
    @GetMapping("/fans")
    public RespBean getUserFans() {
        Long currentUserId = userContextHolder.getCurrentUserId();
        List<UserFollowing> userFans = userFollowingService.getUserFans(currentUserId);
        return RespBean.ok(userFans);
    }

    @ApiOperation("查询全部我关注的人")
    @GetMapping("/followings")
    public RespBean getUserFollowings() {
        Long currentUserId = userContextHolder.getCurrentUserId();
        List<FollowingGroup> userFollowings = userFollowingService.getUserFollowings(currentUserId);
        return RespBean.ok(userFollowings);
    }

    @ApiOperation("新增分组")
    @PostMapping("/groups")
    public RespBean addUserFollowingsGroup(@RequestBody FollowingGroup followingGroup) {
        Long currentUserId = userContextHolder.getCurrentUserId();
        followingGroup.setUserId(currentUserId);
        Long groupIds = userFollowingService.addUserFollowingGroups(followingGroup);
        return RespBean.ok(groupIds);
    }

    @ApiOperation("查询全部分组")
    @GetMapping("/groups")
    public RespBean getUserFollowingGroups() {
        Long currentUserId = userContextHolder.getCurrentUserId();
        List<FollowingGroup> userFollowingGroups = userFollowingService.getUserFollowingGroups(currentUserId);
        return RespBean.ok(userFollowingGroups);
    }
}
