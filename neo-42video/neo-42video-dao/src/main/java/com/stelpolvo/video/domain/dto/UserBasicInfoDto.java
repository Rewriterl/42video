package com.stelpolvo.video.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfoDto {
    private Long id;

    private String phone;

    private String email;

    private String username;

    private String password;

    private Boolean enabled;
}
