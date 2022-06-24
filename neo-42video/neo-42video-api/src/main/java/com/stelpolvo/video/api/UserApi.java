package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.Auth;
import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.domain.UserInfo;
import com.stelpolvo.video.domain.dto.LoginDto;
import com.stelpolvo.video.domain.dto.UserBasicInfoDto;
import com.stelpolvo.video.domain.dto.UserCriteria;
import com.stelpolvo.video.service.UserFollowingService;
import com.stelpolvo.video.service.UserService;
import com.stelpolvo.video.service.config.AppProperties;
import com.stelpolvo.video.service.utils.JwtUtil;
import com.stelpolvo.video.service.utils.RSAUtil;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final AppProperties appProperties;

    private final UserFollowingService userFollowingService;

    private final UserContextHolder userContextHolder;

    @Resource
    private RedisTemplate<String, User> redisTemplate;

    @GetMapping("/rsa-pub")
    public RespBean getRsaPub() {
        return RespBean.ok(RSAUtil.getPublicKeyStr());
    }

    @GetMapping("/user")
    public RespBean getUser(@RequestHeader("Authorization") String header) {
        String jwtToken = header.replace(appProperties.getJwt().getPrefix(), "");
//        return RespBean.ok(userService.getUserWithRolesAndInfoByUserId(UserContextHolder.getCurrentUserId()));
        User data = redisTemplate.opsForValue().get(jwtToken);
        System.out.println(data);
        return RespBean.ok(data);
    }

    @PostMapping("/user")
    public RespBean addUser(@RequestBody User user) {
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
            String newAccessToken = jwtUtil.buildAccessTokenWithRefreshToken(refreshToken);
            User user = userContextHolder.getCurrentUser(accessToken);
            redisTemplate.opsForValue().set(newAccessToken, user);
            return RespBean.ok(new Auth(newAccessToken, refreshToken));
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
    public RespBean getUsers(@RequestHeader("Authorization") String header, @RequestParam Integer page, @RequestParam Integer pageSize, String username) {
        UserCriteria result = userService.pageGetUserInfos(new UserCriteria(page, pageSize, username));
        result.setList(userFollowingService.checkFollowingStatus(result.getList(), userContextHolder.getCurrentUser(header).getId()));
        return RespBean.ok(result);
    }
}
