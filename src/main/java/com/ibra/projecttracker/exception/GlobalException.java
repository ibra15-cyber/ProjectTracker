package com.ibra.projecttracker.exception;

import com.ibra.projecttracker.dto.ErrorResponse;
import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.service.impl.AuditLogService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);
    private final AuditLogService auditLogService;

    public GlobalException(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialException(InvalidCredentialException ex, WebRequest request) {
        logger.warn("Invalid credentials: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.warn("Access denied: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage("Access denied: " + ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex, WebRequest request) {
        logger.warn("Authentication error: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage("Authentication failed: " + ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        logger.warn("JWT expired: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage("Token expired - please refresh your token")
                .statusCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        logger.warn("Invalid token: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

//    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
//    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception ex, WebRequest request) {
//        HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
//
//        String username = SecurityContextHolder.getContext().getAuthentication() != null ?
//                SecurityContextHolder.getContext().getAuthentication().getName() :
//                "anonymous";
//
//        auditLogService.saveAuditLog(
//                AuditLog.unauthorizedAccessLog(
//                        httpRequest.getRequestURI(),
//                        httpRequest.getMethod(),
//                        username
//                )
//        );
//
//        logger.warn("Access denied: {}", ex.getMessage());
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .errorMessage("Access denied: " + ex.getMessage())
//                .statusCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
//                .timestamp(System.currentTimeMillis())
//                .build();
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse response = ErrorResponse.builder()
                .errorMessage("An internal server error occurred: " + ex.getMessage())
                .statusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
