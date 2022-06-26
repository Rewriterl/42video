package com.stelpolvo.video.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试")
@RestController
public class TestApi {
    @ApiOperation("测试返回")
    @GetMapping("/test")
    @PreAuthorize("@el.check('lv2')")
    public String test() {
        return "2x2";
    }
}
