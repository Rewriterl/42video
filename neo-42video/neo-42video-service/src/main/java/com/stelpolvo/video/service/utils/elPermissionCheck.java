package com.stelpolvo.video.service.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "el")
public class elPermissionCheck {
    public Boolean check(String... permissions) {
        System.out.println(Arrays.toString(permissions));
        List<String> elPermissions = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return elPermissions.contains("ADMIN") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
