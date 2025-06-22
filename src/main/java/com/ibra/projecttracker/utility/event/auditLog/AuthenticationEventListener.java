package com.ibra.projecttracker.utility.event.auditLog;


import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.service.impl.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListener {

    private final AuditLogService auditLogService;

    public AuthenticationEventListener(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        String username = authentication.getName();

        log.info("Authentication successful for user: {}", username);
        auditLogService.saveAuditLog(AuditLog.loginSuccessLog(username));
    }

    @EventListener
    public void onInteractiveAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        String username = authentication.getName();

        log.info("Interactive authentication successful for user: {}", username);
        auditLogService.saveAuditLog(AuditLog.loginSuccessLog(username));
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        AuthenticationException exception = event.getException();
        Authentication authentication = event.getAuthentication();
        String username = authentication.getName();

        log.warn("Authentication failed for user: {} - Reason: {}", username, exception.getMessage());

        String reason = determineFailureReason(exception);
        auditLogService.saveAuditLog(AuditLog.loginFailedLog(username, reason));
    }

    private String determineFailureReason(AuthenticationException exception) {
        String exceptionClass = exception.getClass().getSimpleName();
        return switch (exceptionClass) {
            case "BadCredentialsException" -> "Invalid password";
            case "UsernameNotFoundException" -> "User not found";
            case "AccountExpiredException" -> "Account expired";
            case "CredentialsExpiredException" -> "Credentials expired";
            case "DisabledException" -> "Account disabled";
            case "LockedException" -> "Account locked";
            default -> "Authentication failed: " + exception.getMessage();
        };
    }
}

