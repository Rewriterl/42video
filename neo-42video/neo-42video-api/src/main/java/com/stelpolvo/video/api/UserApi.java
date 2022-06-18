package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.service.UserService;
import com.stelpolvo.video.service.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {
    @Autowired
    private UserService userService;

    @GetMapping("/rsa-pub")
    public RespBean getRsaPub() {
        return RespBean.ok(RSAUtil.getPublicKeyStr());
    }

    @PostMapping("/users")
    public RespBean addUser(@RequestBody User user) {
        Integer integer = userService.addUser(user);
        return integer > 0 ? RespBean.ok("添加成功", null) : RespBean.error("添加失败");
    }
}
