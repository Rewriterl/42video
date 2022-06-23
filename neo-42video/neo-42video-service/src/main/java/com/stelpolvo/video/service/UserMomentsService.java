package com.stelpolvo.video.service;

import com.alibaba.fastjson2.JSONObject;
import com.stelpolvo.video.dao.UserMomentsDao;
import com.stelpolvo.video.domain.UserMoment;
import com.stelpolvo.video.domain.constant.MomentConstant;
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.service.utils.RocketMQUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMomentsService {

    @Autowired
    private UserMomentsDao userMomentsDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void addUserMoments(UserMoment userMoment) throws Exception {
        userMoment.setCreateTime(new Date());
        userMomentsDao.addUserMoments(userMoment);
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        Message msg = new Message(MomentConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
        RocketMQUtil.syncSendMsg(producer, msg);
    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        Long size = Optional.ofNullable(redisTemplate.opsForList().size(key)).orElseThrow(() -> new ConditionException("暂无新动态"));
        List<String> messages = redisTemplate.opsForList().leftPop(key, size);
        return messages.stream().map(message -> JSONObject.parseObject(message, UserMoment.class)).collect(Collectors.toList());
    }
}
