package com.stelpolvo.video.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@Configuration
@ConfigurationProperties(prefix = "video")
public class AppProperties {
    @Getter
    @Setter
    @Valid
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {

        private String header = "Authorization";

        private String prefix = "Bearer ";

        @Min(5000L)
        private long accessTokenExpireTime = 60 * 1000L;

        @Min(3600000L)
        private long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;
    }
}
