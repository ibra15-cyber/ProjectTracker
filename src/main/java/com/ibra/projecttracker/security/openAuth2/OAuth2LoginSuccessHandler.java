package com.ibra.projecttracker.security.openAuth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.entity.AuditLog;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.service.impl.AuditLogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Log4j2
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public OAuth2LoginSuccessHandler(JwtUtils jwtUtils, ObjectMapper objectMapper, UserRepository userRepository, AuditLogService auditLogService) {
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String email;
        String name = null;

        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            System.out.println("OIDC User: " + oidcUser.getName());
            System.out.println("OIDC User: " + oidcUser.getFullName());
            email = oidcUser.getEmail();
            name = oidcUser.getFullName();
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            email = oauth2User.getAttribute("login") + "@github.com";
            name = oauth2User.getAttribute("login");
        } else {
            email = name;
        }

        if (email == null) {
            throw new IllegalStateException("Email not found in OAuth2 response");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    auditLogService.saveAuditLog(AuditLog.loginFailedLog(email, "User not found"));
                    return new ResourceNotFoundException("User not found ");
                });

        String token = jwtUtils.generateToken(user);

        log.info("OAuth2 Login Success: User '{}' (DB Role: {}) logged in. Spring Security Context Authorities: {}",
                email, user.getUserRole().name(), authentication.getAuthorities());


        auditLogService.saveAuditLog(AuditLog.loginSuccessLog(email));




//        // For Postman testing - return JSON response
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("token", token);
//        responseData.put("email", email);
//        responseData.put("name", name);
//        responseData.put("role", user.getUserRole().name());
//        responseData.put("message", "Authentication successful");
//
//        String jsonResponse = objectMapper.writeValueAsString(responseData);
//        response.getWriter().write(jsonResponse);
//
//        System.out.println("Bearer Token generated: " + token);

//         Alternative: For web applications, redirect with token in URL fragment
//         response.sendRedirect("/home#token=" + token);

        //TODO I SHOULD CLEAN THIS UP

//         Alternative: Set token as HTTP-only cookie for web applications
         Cookie jwtCookie = new Cookie("jwt", token);
         jwtCookie.setHttpOnly(true);
         jwtCookie.setSecure(true);
         jwtCookie.setPath("/");
         jwtCookie.setMaxAge(60 * 30);
         response.addCookie(jwtCookie);

        Cookie roleCookie = new Cookie("user_role", user.getUserRole().name());
        roleCookie.setHttpOnly(true); // Allow JS to read
        roleCookie.setSecure(true);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(30 * 60); // Match JWT expiry
        response.addCookie(roleCookie);

        if (user.getUserRole() == UserRole.ADMIN) {
            response.sendRedirect("/swagger-ui/index.html");
        } else {
            response.sendRedirect("/api/v1/auth/oauth2/success");
        }
    }
}