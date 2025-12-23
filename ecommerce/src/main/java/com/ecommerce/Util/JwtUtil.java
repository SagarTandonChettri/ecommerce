package com.ecommerce.Util;

import com.ecommerce.ApiResponse.JwtResponse;
import com.ecommerce.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static javax.crypto.Cipher.SECRET_KEY;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;
    private SecretKey key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 5; // 5 hours

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Date getExpiryDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public String generateToken(String userName, Role role) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("role",role.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Consistent parser
    private JwtParser getParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean isTokenValid(String token) {
        try {
            getParser().parseClaimsJws(token);  // Use same parser
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token format: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error("JWT signature validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }


    // Optional helper methods:
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
