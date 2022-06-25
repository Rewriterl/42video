package com.stelpolvo.video.dao;


import com.stelpolvo.video.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleDao {

    List<UserRole> getUserRolesByUserId(Long userId);

    Integer addUserRole(UserRole userRole);
}
