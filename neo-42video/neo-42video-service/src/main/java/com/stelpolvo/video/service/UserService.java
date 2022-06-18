package com.stelpolvo.video.service;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import com.stelpolvo.video.dao.UserDao;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.domain.UserConstant;
import com.stelpolvo.video.domain.UserInfo;
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.service.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HashMap hashMap = JSON.parseObject(username, HashMap.class);
        if (hashMap.get("phone") != null) {
            return userDao.getUserByPhoneOrEmailOrUsername(hashMap.get("phone"), null, null);
        } else if (hashMap.get("email") != null) {
            return userDao.getUserByPhoneOrEmailOrUsername(null, hashMap.get("email"), null);
        } else if (hashMap.get("username") != null) {
            return userDao.getUserByPhoneOrEmailOrUsername(null, null, hashMap.get("username"));
        }
        UserDetails userDetails = userDao.getUserByPhoneOrEmailOrUsername(null, null, username);
        if (userDetails == null) {
            throw new ConditionException("用户不存在");
        }
        return userDetails;
    }

    @Transactional
    public Integer addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("请输入手机号");
        }
        User userByPhoneOrEmailOrUsername = userDao.getUserByPhoneOrEmailOrUsername(phone, null, null);
        if (userByPhoneOrEmailOrUsername != null) {
            throw new ConditionException("当前手机号已注册");
        }
        Date date = new Date();
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("解密失败");
        }
        String encode = new BCryptPasswordEncoder().encode(rawPassword);
        user.setUsername(UserConstant.DEFAULT_USERNAME + new Random(System.currentTimeMillis()).nextInt());
        user.setPassword(encode);
        user.setCreateTime(date);
        Integer integer = userDao.addUser(user);
        userDao.addUserInfo(new UserInfo(user.getId()));

        return integer;
    }
}
