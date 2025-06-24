package com.ibra.projecttracker.exception.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.response.ErrorResponse;
import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.service.impl.AuditLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper, AuditLogService auditLogService) {
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String principalIdentifier;
        String requestEmail = request.getParameter("email");
        if (requestEmail != null && !requestEmail.isEmpty()) {
            principalIdentifier = requestEmail;
        } else {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                principalIdentifier = "JWT_AUTH_FAILURE_FOR_" + request.getRequestURI();
            } else {
                principalIdentifier = "UNAUTHENTICATED_ACCESS_TO_" + request.getRequestURI();
            }
        }

        logger.warn("Authentication failed for principal '{}' on request {}: {}",
                principalIdentifier, request.getRequestURI(), authException.getMessage());

        auditLogService.saveAuditLog(AuditLog.loginFailedLog(principalIdentifier, authException.getMessage()));


        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage("Authentication failed: " + authException.getMessage())
                .statusCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED))
                .timestamp(System.currentTimeMillis())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}