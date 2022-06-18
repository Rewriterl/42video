package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.UserDao;
import com.stelpolvo.video.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDao.getUserByPhoneOrEmailOrUsername(null, null, username);
        if (userDetails == null) {
            throw new ConditionException("用户不存在");
        }
        return userDetails;
    }
}
