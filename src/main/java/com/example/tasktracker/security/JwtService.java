package com.example.tasktracker.security;

import com.example.tasktracker.user.User;
import com.example.tasktracker.user.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

// Сервис работы с JWT токенами
@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    // Генерация токена для пользователя
    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Получение email из токена
    public String getEmailFromToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    // Получение роли из токена
    public UserRole getRoleFromToken(String token) {
        String role = (String) parseClaims(token).getBody().get("role");
        return UserRole.valueOf(role);
    }

    // Проверка валидности токена
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token).getBody();
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Разбор токена
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}
