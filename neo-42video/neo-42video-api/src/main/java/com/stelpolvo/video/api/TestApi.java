package com.stelpolvo.video.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApi {
    @GetMapping("/test")
    public String test() {
        return "2x2";
    }
}
