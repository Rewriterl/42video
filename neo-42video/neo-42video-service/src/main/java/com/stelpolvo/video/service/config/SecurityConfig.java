package com.stelpolvo.video.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import java.io.PrintWriter;

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

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    User user = (User) authentication.getPrincipal();
                    user.setPassword(null);
                    RespBean ok = RespBean.ok("登录成功!", user);
                    String s = new ObjectMapper().writeValueAsString(ok);
                    out.write(s);
                    out.flush();
                    out.close();
                }
        );
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    RespBean respBean = RespBean.error(exception.getMessage());
                    if (exception instanceof LockedException) {
                        respBean.setMsg("账户被锁定，请联系管理员!");
                    } else if (exception instanceof CredentialsExpiredException) {
                        respBean.setMsg("密码过期，请联系管理员!");
                    } else if (exception instanceof AccountExpiredException) {
                        respBean.setMsg("账户过期，请联系管理员!");
                    } else if (exception instanceof DisabledException) {
                        respBean.setMsg("账户被禁用，请联系管理员!");
                    } else if (exception instanceof BadCredentialsException) {
                        respBean.setMsg("用户名或者密码输入错误，请重新输入!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respBean));
                    out.flush();
                    out.close();
                }
        );
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/login");
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        loginFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return loginFilter;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
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
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    String s = new ObjectMapper().writeValueAsString(RespBean.ok("注销成功"));
                    response.getWriter().write(s);
                })
                .permitAll()
                .and()
                .csrf().disable();
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
