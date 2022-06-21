package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.*;
import com.stelpolvo.video.domain.dto.LoginDto;
import com.stelpolvo.video.domain.dto.UserBasicInfoDto;
import com.stelpolvo.video.domain.dto.UserCriteria;
import com.stelpolvo.video.service.UserFollowingService;
import com.stelpolvo.video.service.UserService;
import com.stelpolvo.video.service.config.AppProperties;
import com.stelpolvo.video.service.utils.JwtUtil;
import com.stelpolvo.video.service.utils.RSAUtil;
import com.stelpolvo.video.service.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserApi {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserFollowingService userFollowingService;

    @GetMapping("/rsa-pub")
    public RespBean getRsaPub() {
        return RespBean.ok(RSAUtil.getPublicKeyStr());
    }

    @GetMapping("/user")
    public RespBean getUser() {
        return RespBean.ok(userService.getUserWithRolesAndInfoByUserId(UserContextHolder.getCurrentUserId()));
    }

    @PostMapping("/user")
    public RespBean addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return RespBean.ok("添加成功");
    }

    /**
     * 自己实现的登录接口
     * SpringSecurity提供的登录接口有些重
     */
    @PostMapping("/token")
    public RespBean login(@Valid @RequestBody LoginDto loginDto) {
        return RespBean.ok("登录成功", userService.login(loginDto));
    }

    /**
     * TODO: 使用RequestBody无法完整获取到参数。暂未找到原因，这里采用formData的形式获取参数
     */
    @PostMapping("/token/refresh")
    public RespBean refreshToken(@RequestHeader String authorization, @RequestParam String refreshToken) {
        String accessToken = authorization.replace(appProperties.getJwt().getPrefix(), "");
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpire(accessToken)) {
            return RespBean.ok(new Auth(jwtUtil.buildAccessTokenWithRefreshToken(refreshToken), refreshToken));
        }
        return RespBean.error("刷新失败");
    }

    @PutMapping("/user")
    public RespBean updateUser(@RequestBody UserBasicInfoDto user) {
        userService.updateUser(user);
        return RespBean.ok("更新成功");
    }

    @PutMapping("/user-infos")
    public RespBean updateUserInfo(@RequestBody UserInfo userInfo) {
        userService.updateUserInfo(userInfo);
        return RespBean.ok("更新成功");
    }

    @GetMapping("/users")
    public RespBean getUsers(@RequestParam Integer page, @RequestParam Integer pageSize, String username) {
        UserCriteria result = userService.pageGetUserInfos(new UserCriteria(page, pageSize, username));
        result.setList(userFollowingService.checkFollowingStatus(result.getList(), UserContextHolder.getCurrentUserId()));
        return RespBean.ok(result);
    }
}
