package com.ibra.projecttracker.security.jwt;

import com.ibra.projecttracker.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

    private static final Long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 30;
    private static final Long refreshExpirationMs = 1000L * 60L * 60L * 24L; // 24 hours

    @Value("${secretJwtString}")
    private String secretJwtString;



    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretJwtString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return generateToken(user.getEmail());
    }

    public String generateToken(String username) {
        log.info("Generating JWT for username: {}", username);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISEC))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getKey())
                .compact();

    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(
                Jwts.parser()
                        .verifyWith(getKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
        );
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }


    public Map<String, Long> getTokenTimeDetails(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date issuedAt = claims.getIssuedAt();
            Date expiration = claims.getExpiration();
            long remainingTimeMs = expiration.getTime() - System.currentTimeMillis();
            long totalDurationMs = expiration.getTime() - issuedAt.getTime();

            return Map.of(
                    "remainingTimeMs", remainingTimeMs,
                    "totalDurationMs", totalDurationMs
            );
        } catch (Exception e) {
            log.error("Error calculating token time details: {}", e.getMessage());
            return Map.of();
        }
    }

}
