package com.stelpolvo.video.service.utils;

import com.stelpolvo.video.domain.User;
import com.stelpolvo.video.service.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final AppProperties appProperties;

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
        return validateToken(jwtToken, key, true);
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
}
