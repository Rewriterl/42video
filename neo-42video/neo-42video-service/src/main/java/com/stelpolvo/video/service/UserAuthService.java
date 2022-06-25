package com.stelpolvo.video.service;

import com.stelpolvo.video.domain.AuthRoleElementOperation;
import com.stelpolvo.video.domain.AuthRoleMenu;
import com.stelpolvo.video.domain.UserAuthorities;
import com.stelpolvo.video.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRoleService userRoleService;

    private final AuthRoleService authRoleService;

    public UserAuthorities getUserAuthorities(Long userId) {
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        Set<Long> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<AuthRoleElementOperation> roleElementOperations = authRoleService.getRoleElementOperations(roleIds);
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getRoleMenusByRoleIds(roleIds);
        return new UserAuthorities(roleElementOperations, authRoleMenuList);
    }

    public void addUserDefaultRole(Long id) {

    }
}
