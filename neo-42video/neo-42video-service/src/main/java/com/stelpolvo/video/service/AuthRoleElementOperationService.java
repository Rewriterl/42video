package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.AuthRoleElementOperationDao;
import com.stelpolvo.video.domain.AuthRoleElementOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthRoleElementOperationService {

    private final AuthRoleElementOperationDao authRoleElementOperationDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIds) {
        return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(roleIds);
    }
}
