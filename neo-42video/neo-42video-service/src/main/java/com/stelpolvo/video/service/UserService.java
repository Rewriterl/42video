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
import com.stelpolvo.video.domain.exception.ConditionException;
import com.stelpolvo.video.service.utils.CollectionUtil;
import com.stelpolvo.video.service.utils.JwtUtil;
import com.stelpolvo.video.service.utils.RSAUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRoleDao userRoleDao;

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
                    Optional.of(Jwts.parserBuilder().setSigningKey(JwtUtil.key).build().parseClaimsJws(accessToken).getBody())
                            .ifPresent((claims) -> {
                                List<SimpleGrantedAuthority> authorityCollection = CollectionUtil.convertObjectToList(
                                                claims.get("authorities")).stream()
                                        .map(String::valueOf)
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList());
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                        claims.getSubject(), null, authorityCollection);
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            });
                    return new Auth(accessToken, jwtUtil.createRefreshToken(u));
                })
                .orElseThrow(() -> new ConditionException("用户名或密码错误"));
    }
}
