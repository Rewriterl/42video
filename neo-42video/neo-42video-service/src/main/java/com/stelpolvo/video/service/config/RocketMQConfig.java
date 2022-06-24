package com.stelpolvo.video.service.config;

import com.alibaba.fastjson2.JSONObject;
import com.stelpolvo.video.domain.UserFollowing;
import com.stelpolvo.video.domain.UserMoment;
import com.stelpolvo.video.domain.constant.MomentConstant;
import com.stelpolvo.video.service.UserFollowingService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RocketMQConfig {
    @Value("${rocketmq.producer.moments.name}")
    private String nameServer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserFollowingService userFollowingService;

    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(MomentConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServer);
        producer.start();
        return producer;
    }

    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception {
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer(MomentConstant.GROUP_MOMENTS);
        pushConsumer.setNamesrvAddr(nameServer);
        pushConsumer.subscribe(MomentConstant.TOPIC_MOMENTS, "*");
        // 参数类型还没记住，这里就先不用lambda表达式了
        pushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                String message = new String(messageExt.getBody());
                UserMoment userMoment = JSONObject.parseObject(message, UserMoment.class);
                List<UserFollowing> userFans = userFollowingService.getUserFans(userMoment.getUserId());
                // 每个用户的粉丝可能不止关注了一个up，所以用list存每个用户的收到的moment
                userFans.forEach(userFollowing -> redisTemplate.opsForList().leftPush("subscribed-" + userFollowing.getUserId(), message));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        pushConsumer.start();
        return pushConsumer;
    }
}
