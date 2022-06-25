package com.stelpolvo.video.service;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import com.stelpolvo.video.dao.UserDao;
import com.stelpolvo.video.dao.UserRoleDao;
import com.stelpolvo.video.domain.Auth;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.domain.UserInfo;
import com.stelpolvo.video.domain.UserRole;
import com.stelpolvo.video.domain.constant.UserConstant;
import com.stelpolvo.video.domain.dto.LoginDto;
import com.stelpolvo.video.domain.dto.UserBasicInfoDto;
import com.stelpolvo.video.domain.dto.UserCriteria;
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.service.config.AppProperties;
import com.stelpolvo.video.service.utils.JwtUtil;
import com.stelpolvo.video.service.utils.RSAUtil;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    private final JwtUtil jwtUtil;

    private final UserRoleDao userRoleDao;

    private final AppProperties appProperties;

    private final UserContextHolder userContextHolder;

    // If you add your own @Bean of any of the auto-configured types, it replaces the default (except in the case of RedisTemplate, when the exclusion is based on the bean name, redisTemplate, not its type).
    // 因此这里使用按名称注入的方式
    @Resource
    private RedisTemplate<String, User> redisTemplate;

    @Override
    @Deprecated
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HashMap hashMap = JSON.parseObject(username, HashMap.class);
        User user = userDao.getUserByPhoneOrEmailOrUsername(hashMap.get("phone"), hashMap.get("email"), hashMap.get("username"));
        if (StringUtils.isNullOrEmpty(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        return Optional.ofNullable(user).orElseThrow(() -> new ConditionException("用户不存在"));
    }

    @Transactional
    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("请输入手机号");
        }
        User userByPhoneOrEmailOrUsername = userDao.getUserByPhoneOrEmailOrUsername(phone, user.getEmail(), user.getUsername());
        if (userByPhoneOrEmailOrUsername != null) {
            throw new ConditionException("当前手机号已注册");
        }
        if (!phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")) {
            throw new ConditionException("手机号不合法");
        }
        Date date = new Date();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(user.getPassword());
        } catch (Exception e) {
            throw new ConditionException("解密失败");
        }
        String encode = new BCryptPasswordEncoder().encode(rawPassword);
        user.setUsername(UserConstant.DEFAULT_USERNAME + new Random(System.currentTimeMillis()).nextInt());
        user.setPassword(encode);
        user.setCreateTime(date);
        if (userDao.addUser(user) <= 0) throw new ConditionException("注册失败");
        userRoleDao.addUserRole(new UserRole(user.getId(), UserConstant.DEFAULT_ROLE_ID));
        userDao.addUserInfo(new UserInfo(user.getId()));
    }

    /**
     * 更新用户基本信息
     */
    public void updateUser(UserBasicInfoDto user) {
        Optional.ofNullable(userDao.getUserById(user.getId()))
                .map(u -> {
                    try {
                        String rawPassword = RSAUtil.decrypt(user.getPassword());
                        String encode = new BCryptPasswordEncoder().encode(rawPassword);
                        u.setPassword(encode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return u;
                })
                .orElseThrow(() -> new ConditionException("用户不存在"));
        if (userDao.updateUser(user) <= 0) throw new ConditionException("修改失败");
    }

    public void updateUserInfo(UserInfo userInfo) {
        if (userDao.updateUserInfo(userInfo) <= 0) throw new ConditionException("修改失败");
    }

    public User getUserWithRolesAndInfoByUserId(Long userId) {
        User user = userDao.getUserWithRolesAndInfoById(userId);
        user.setPassword(null);
        return user;
    }

    /**
     * 单纯学习下Optional和Stream的用法，可读性比较差
     */
    public Auth login(LoginDto loginDto) {
        return Optional.of(userDao.getUserByPhoneOrEmailOrUsername(loginDto.getPhone(), loginDto.getEmail(), loginDto.getUsername()))
                .filter(u -> {
                    String rawPassword;
                    try {
                        rawPassword = RSAUtil.decrypt(loginDto.getPassword());
                    } catch (Exception e) {
                        throw new ConditionException("解密失败");
                    }
                    boolean matches = new BCryptPasswordEncoder().matches(rawPassword, u.getPassword());
                    if (matches) u.setRoles(Optional.ofNullable(userRoleDao.getUserRolesByUserId(u.getId()))
                            .orElseThrow(() -> new ConditionException("登录异常请联系管理员")));
                    return matches;
                })
                .map(u -> {
                    String accessToken = jwtUtil.createAccessToken(u);
                    jwtUtil.setAuthentication(accessToken);
                    redisTemplate.opsForValue().set(accessToken, u, appProperties.getJwt().getAccessTokenExpireTime(), TimeUnit.MILLISECONDS);
                    return new Auth(accessToken, jwtUtil.createRefreshToken(u));
                })
                .orElseThrow(() -> new ConditionException("用户名或密码错误"));
    }

    public User getUserById(Long userid) {
        return Optional.ofNullable(userDao.getUserWithInfoById(userid))
                .orElseThrow(() -> new ConditionException("用户不存在"));
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> followingIdSet) {
        return Optional.ofNullable(userDao.getUserInfoByUserIds(followingIdSet))
                .orElseThrow(() -> new ConditionException("查询失败"));
    }

    public UserCriteria pageGetUserInfos(UserCriteria userCriteria) {
        Integer total = userDao.pageCountUserInfos(userCriteria.getUsername());
        if (total > 0) {
            userCriteria.setList(userDao.pageGetUserInfos(userCriteria));
        }
        userCriteria.setTotal(total);
        return userCriteria;
    }

    public User getUser(String header) {
        return userDao.getUserWithRolesAndInfoById(userContextHolder.getCurrentUserId());
//        String jwtToken = header.replace(appProperties.getJwt().getPrefix(), "");
//        return redisTemplate.opsForValue().get(jwtToken);
    }

    public Auth refreshToken(String authorization, String refreshToken) {
        String accessToken = authorization.replace(appProperties.getJwt().getPrefix(), "");
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpire(accessToken)) {
            String newAccessToken = jwtUtil.buildAccessTokenWithRefreshToken(refreshToken);
            User user = userContextHolder.getCurrentUser(accessToken);
            redisTemplate.opsForValue().set(newAccessToken, user, appProperties.getJwt().getAccessTokenExpireTime(), TimeUnit.MILLISECONDS);
            return new Auth(newAccessToken, refreshToken);
        }
        throw new ConditionException("续期失败");
    }
}
