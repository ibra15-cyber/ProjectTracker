package com.ibra.projecttracker.security.jwt;

import com.ibra.projecttracker.exception.InvalidTokenException;
import com.ibra.projecttracker.security.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = getTokenFromRequest(request);

            if (token != null) {
                try {
                    String username = jwtUtils.getUsernameFromToken(token);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    if (!jwtUtils.isTokenValid(token, userDetails)) {
                        SecurityContextHolder.clearContext();
                        throw new InvalidTokenException("Invalid JWT token");
                    }

                    log.info("User {} successfully authenticated by JWT filter. Authorities: {}", username, userDetails.getAuthorities());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (ExpiredJwtException ex) {
                    SecurityContextHolder.clearContext();
                    logger.error("JWT expired: " + ex.getMessage(), ex);
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT expired: " + ex.getMessage());
                    return;
                } catch (InvalidTokenException ex) {
                    SecurityContextHolder.clearContext();
                    logger.error("Invalid token: " + ex.getMessage(), ex);
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token: " + ex.getMessage());
                    return;
                } catch (Exception ex) {
                    SecurityContextHolder.clearContext();
                    logger.error("Error processing JWT token: " + ex.getMessage(), ex);
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication error: " + ex.getMessage());
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error in JwtAuthFilter: " + e.getMessage(), e);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error during authentication.");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && StringUtils.startsWithIgnoreCase(token, "Bearer")) {
            return token.substring(7);
        }
        return null;
    }
}