package com.api.auth_service.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.api.auth_service.model.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration-ms}")
    private long accessExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserModel user) {
        Date agora = new Date();
        Date exp = new Date(agora.getTime() + accessExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId())) // agora o subject é o ID
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(agora)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getAllClaims(token).getSubject());
    }

    public String getEmailFromToken(String token) {
        return getAllClaims(token).get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    public boolean isValid(String token) {
        try {
            getAllClaims(token); // se conseguir parsear → é válido
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
