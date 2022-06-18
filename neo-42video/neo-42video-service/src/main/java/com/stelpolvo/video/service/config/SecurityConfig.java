package com.stelpolvo.video.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO: 为方便期间暂时在资源服务中集成了spring security的配置，后续迁移到网关服务中进行配置
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .loginPage("/loginPage")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    User user = (User) authentication.getPrincipal();
                    String s = new ObjectMapper().writeValueAsString(RespBean.ok(user));
                    response.getWriter().write(s);
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    if (exception instanceof CredentialsExpiredException) {
                        String s = new ObjectMapper().writeValueAsString(RespBean.error("密码已过期，请联系管理员"));
                        response.getWriter().write(s);
                    } else if (exception instanceof AccountExpiredException) {
                        String s = new ObjectMapper().writeValueAsString(RespBean.error("账户已过期，请联系管理员"));
                        response.getWriter().write(s);
                    } else if (exception instanceof DisabledException) {
                        String s = new ObjectMapper().writeValueAsString(RespBean.error("账户已被禁用，请联系管理员"));
                        response.getWriter().write(s);
                    } else if (exception instanceof LockedException) {
                        String s = new ObjectMapper().writeValueAsString(RespBean.error("账号已被锁定，请联系管理员"));
                        response.getWriter().write(s);
                    } else {
                        String s = new ObjectMapper().writeValueAsString(RespBean.error("登录失败"));
                        response.getWriter().write(s);
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler((request, response, authentication) -> {

                })
                .permitAll()
                .and()
                .csrf().disable();
    }
}
