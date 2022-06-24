package com.stelpolvo.video.service.config;

import com.stelpolvo.video.service.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!checkToken(request)) SecurityContextHolder.clearContext();
        filterChain.doFilter(request, response);
    }

    private boolean checkToken(HttpServletRequest request) {
        boolean result;
        String header = request.getHeader(appProperties.getJwt().getHeader());
        if (header == null) return false;
        String jwtToken = header.replace(appProperties.getJwt().getPrefix(), "");
        result = header.startsWith(appProperties.getJwt().getPrefix())
                && jwtUtil.validateAccessToken(jwtToken);
        if (result) {
            jwtUtil.setAuthentication(jwtToken);
        }
        return result;
    }
}
