package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.UserRoleDao;
import com.stelpolvo.video.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    public final UserRoleDao userRoleDao;
    public List<UserRole> getUserRolesByUserId(Long userId) {
        return userRoleDao.getUserRolesByUserId(userId);
    }
}
