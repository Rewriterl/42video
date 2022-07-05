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
import com.stelpolvo.video.service.utils.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "用户")
@RestController
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    private final UserFollowingService userFollowingService;

    @ApiOperation("获取公钥")
    @GetMapping("/rsa-pub")
    public RespBean getRsaPub() {
        return RespBean.ok("获取成功",RSAUtil.getPublicKeyStr());
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/user")
    public RespBean getUser() {
        return RespBean.ok(userService.getUser());
    }

    @ApiOperation("注册")
    @PostMapping("/user")
    public RespBean addUser(@RequestBody User user) {
        userService.addUser(user);
        return RespBean.ok("添加成功");
    }

    /**
     * 自己实现的登录接口
     * SpringSecurity提供的登录接口有些重
     */
    @ApiOperation("登录")
    @PostMapping("/token")
    public RespBean login(@Valid @RequestBody LoginDto loginDto) {
        return RespBean.ok("登录成功", userService.login(loginDto));
    }

    /**
     * TODO: 使用RequestBody无法完整获取到参数。暂未找到原因，这里采用formData的形式获取参数
     */
    @ApiOperation("token续期")
    @PostMapping("/token/refresh")
    public RespBean refreshToken(@RequestHeader String authorization, @RequestParam String refreshToken) {
        Auth auth = userService.refreshToken(authorization, refreshToken);
        return RespBean.ok(auth);
    }

    @ApiOperation("更新用户基础信息")
    @PutMapping("/user")
    public RespBean updateUser(@RequestBody UserBasicInfoDto user) {
        userService.updateUser(user);
        return RespBean.ok("更新成功");
    }

    @ApiOperation("更新用户详细信息")
    @PutMapping("/info")
    public RespBean updateUserInfo(@RequestBody UserInfo userInfo) {
        userService.updateUserInfo(userInfo);
        return RespBean.ok("更新成功");
    }

    @ApiOperation("分页查询用户")
    @GetMapping("/users")
    public RespBean getUsers(@RequestParam Integer page, @RequestParam Integer pageSize, String username) {
        UserCriteria result = userService.pageGetUserInfos(new UserCriteria(page, pageSize, username));
        result.setList(userFollowingService.checkFollowingStatus(result.getList()));
        return RespBean.ok(result);
    }
}
