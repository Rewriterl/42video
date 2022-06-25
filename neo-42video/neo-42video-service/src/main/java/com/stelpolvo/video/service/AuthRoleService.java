package com.stelpolvo.video.service;

import com.stelpolvo.video.domain.AuthRoleElementOperation;
import com.stelpolvo.video.domain.AuthRoleMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthRoleService {

    public final AuthRoleElementOperationService authRoleElementOperationService;

    public final AuthRoleMenuService authRoleMenuService;

    public List<AuthRoleElementOperation> getRoleElementOperations(Set<Long> roleIds){
        return authRoleElementOperationService.getRoleElementOperationsByRoleIds(roleIds);
    }

    public List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> roleIds) {
        return authRoleMenuService.getRoleMenusByRoleIds(roleIds);
    }
}
