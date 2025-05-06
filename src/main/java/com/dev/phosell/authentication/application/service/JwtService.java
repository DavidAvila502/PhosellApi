package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// TODO: ADD isRoleValid method.

@Service
public class JwtService {
    private static final String ROLE_CLAIM = "role";

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${security.jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Getter
    @Value("${security.jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshTokenExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseAndValidateToken(token);
            return isUsernameValid(claims, userDetails) && isExpirationValid(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String buildToken(UserDetails userDetails, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        addRoleClaimIfApplicable(userDetails, claims);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseAndValidateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void addRoleClaimIfApplicable(UserDetails userDetails, Map<String, Object> claims) {
        if (userDetails instanceof CustomUserDetails) {
            claims.put(ROLE_CLAIM, ((CustomUserDetails) userDetails).getRole().name());
        }
    }

    private boolean isUsernameValid(Claims claims, UserDetails userDetails) {
        return claims.getSubject().equals(userDetails.getUsername());
    }

    private boolean isExpirationValid(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.after(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
