package com.stelpolvo.video.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String phone;
    private String email;
    private String username;
    @NotNull
    private String password;
}
