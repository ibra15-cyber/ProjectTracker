package com.ibra.projecttracker.security.jwt;

import com.ibra.projecttracker.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private  static  final Long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 60L  *24L * 6L;

    @Value("${secretJwtString}")
    private String secretJwtString;

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretJwtString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        return generateToken(user.getEmail());
    }

    public String generateToken(String username){
        System.out.println("Generating JWT for username: " + username);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * EXPIRATION_TIME_IN_MILLISEC))
                .signWith(getKey())
                .compact();
    }

    public String getUsernameFromToken(String token){
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
}
