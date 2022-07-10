package com.stelpolvo.video.dao.repository;

import com.stelpolvo.video.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, Long> {

}
