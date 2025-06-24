package com.ibra.projecttracker.exception.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.response.ErrorResponse; // Assuming this DTO exists
import com.ibra.projecttracker.entity.AuditLog; // Assuming this model exists
import com.ibra.projecttracker.service.impl.AuditLogService; // Assuming this service exists

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper, AuditLogService auditLogService) {
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String principalName = "anonymous";
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            principalName = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        logger.warn("Access denied for principal '{}' to resource {}: {}",
                principalName, request.getRequestURI(), accessDeniedException.getMessage());

        auditLogService.saveAuditLog(AuditLog.loginFailedLog(principalName, request.getRequestURI()));


        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage("Access denied: You do not have permission to access this resource.")
                .statusCode(String.valueOf(HttpServletResponse.SC_FORBIDDEN))
                .timestamp(System.currentTimeMillis())
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}