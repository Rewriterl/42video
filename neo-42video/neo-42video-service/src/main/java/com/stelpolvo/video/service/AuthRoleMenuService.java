package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.AuthRoleMenuDao;
import com.stelpolvo.video.domain.AuthRoleMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthRoleMenuService {
    private final AuthRoleMenuDao authRoleMenuDao;
    public List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> roleIds) {
        return authRoleMenuDao.getAuthRoleMenusByRoleIds(roleIds);
    }
}
