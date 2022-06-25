package com.stelpolvo.video.dao;

import com.stelpolvo.video.domain.AuthRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String code);

    List<AuthRole> getAuthRolesByUserId(Long userId);

}
