package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.domain.dto.LoginDto;
import com.stelpolvo.video.service.UserService;
import com.stelpolvo.video.service.utils.RSAUtil;
import com.stelpolvo.video.service.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserApi {
    @Autowired
    private UserService userService;

    @GetMapping("/rsa-pub")
    public RespBean getRsaPub() {
        return RespBean.ok(RSAUtil.getPublicKeyStr());
    }

    @GetMapping("/users")
    public RespBean getUser() {
        return RespBean.ok(userService.getUserWithRolesAndInfoByUserId(UserContextHolder.getCurrentUserId()));
    }

    @PostMapping("/users")
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
}
