package com.stelpolvo.video.dao;

import com.stelpolvo.video.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentsDao {

    Integer addUserMoments(UserMoment userMoment);
}
