package com.stelpolvo.video.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accessToken;
    private String refreshToken;
}
