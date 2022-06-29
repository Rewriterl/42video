package com.stelpolvo.video.service;

import com.stelpolvo.video.domain.AuthRoleElementOperation;
import com.stelpolvo.video.domain.AuthRoleMenu;
import com.stelpolvo.video.domain.UserAuthorities;
import com.stelpolvo.video.domain.UserRole;
import com.stelpolvo.video.service.utils.UserContextHolder;
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

    private final UserContextHolder userContextHolder;

    public UserAuthorities getUserAuthorities() {
        Long userId = userContextHolder.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        Set<Long> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<AuthRoleElementOperation> roleElementOperations = authRoleService.getRoleElementOperations(roleIds);
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getRoleMenusByRoleIds(roleIds);
        return new UserAuthorities(roleElementOperations, authRoleMenuList);
    }

    public void addUserDefaultRole(Long id) {

    }
}
