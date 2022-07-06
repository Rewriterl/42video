package com.stelpolvo.video.service.utils;

import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.service.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final AppProperties appProperties;
    @Resource
    private RedisTemplate<String, User> redisTemplate;

    public void setAuthentication(String accessToken) {
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
    }

    public String createJWTToken(UserDetails userDetails, long timeToExpire) {
        return createJWTToken(userDetails, timeToExpire, key);
    }

    public String createJWTToken(UserDetails userDetails, long timeToExpire, Key signKey) {
        return Jwts
                .builder()
                .setId("42video")
                .setSubject(((User) userDetails).getId().toString())
                .claim("authorities", Optional.ofNullable(userDetails.getAuthorities())
                        .orElse(new ArrayList<>()).stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + timeToExpire))
                .signWith(signKey, SignatureAlgorithm.HS512).compact();
    }

    public String createAccessToken(UserDetails userDetails) {
        return createJWTToken(userDetails, appProperties.getJwt().getAccessTokenExpireTime());
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJWTToken(userDetails, appProperties.getJwt().getRefreshTokenExpireTime(), refreshKey);
    }

    public boolean validateAccessToken(String jwtToken) {
        User user = redisTemplate.opsForValue().get(jwtToken);
        return user != null;
//        return validateToken(jwtToken, key, true);
    }

    public boolean validateRefreshToken(String jwtToken) {
        return validateToken(jwtToken, refreshKey, true);
    }

    public boolean validateAccessTokenWithoutExpire(String jwtToken) {
        return validateToken(jwtToken, key, false);
    }

    public boolean validateRefreshTokenWithoutExpire(String jwtToken) {
        return validateToken(jwtToken, refreshKey, false);
    }

    public boolean validateToken(String jwtToken, Key signKey, boolean isExpiredInvalid) {
        try {
            Jwts.parserBuilder().setSigningKey(signKey).build().parse(jwtToken);
            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                return !isExpiredInvalid;
            }
            return false;
        }
    }

    public String buildAccessTokenWithRefreshToken(String jwtToken) {
        return parseClaims(jwtToken, refreshKey)
                .map(claims -> Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(new Date(System.currentTimeMillis() + appProperties.getJwt().getAccessTokenExpireTime()))
                        .signWith(key, SignatureAlgorithm.HS512).compact())
                .orElseThrow();
    }

    public Optional<Claims> parseClaims(String jwtToken, Key signKey) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(jwtToken).getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validateWithoutExpiration(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(JwtUtil.key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTokenAndSetAuth(HttpServletRequest request) {
        boolean result;
        String header = request.getHeader(appProperties.getJwt().getHeader());
        if (header == null) return false;
        String jwtToken = header.replace(appProperties.getJwt().getPrefix(), "");
        result = header.startsWith(appProperties.getJwt().getPrefix())
                && validateAccessToken(jwtToken);
        if (result) {
            setAuthentication(jwtToken);
        }
        return result;
    }
}
