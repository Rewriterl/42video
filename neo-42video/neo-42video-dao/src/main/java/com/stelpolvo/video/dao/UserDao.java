package com.stelpolvo.video.dao;

import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.domain.UserInfo;
import com.stelpolvo.video.domain.dto.UserBasicInfoDto;
import com.stelpolvo.video.domain.dto.UserCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserDao {

    User getUserWithRolesAndInfoById(Long id);

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    User getUserWithInfoById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUser(UserBasicInfoDto user);

    User getUserByPhoneOrEmailOrUsername(@Param("phone") Object phone, @Param("email") Object email, @Param("username") Object username);

    Integer updateUserInfo(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,
                               @Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("userId") Long userId,
                            @Param("createTime") Date createTime);

    List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList);

    String getRefreshTokenByUserId(Long userId);

    Integer deleteRefreshTokenByUserId(Long userId);

    Integer pageCountUserInfos(String username);

    List<UserInfo> pageGetUserInfos(UserCriteria userCriteria);
}
