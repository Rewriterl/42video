package com.stelpolvo.video.api;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.service.ElasticSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试")
@RestController
@RequiredArgsConstructor
public class TestApi {

    private final ElasticSearchService elasticSearchService;
    @ApiOperation("测试返回")
    @GetMapping("/test")
    @PreAuthorize("@el.check('lv2')")
    public String test() {
        return "2x1";
    }

    @GetMapping("/test2")
    public RespBean test2() {
        return RespBean.ok(elasticSearchService.getVideo("测"));
    }
}
