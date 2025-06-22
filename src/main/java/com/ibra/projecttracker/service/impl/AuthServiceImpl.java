package com.ibra.projecttracker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.dto.request.*;
import com.ibra.projecttracker.entity.*;
import com.ibra.projecttracker.exception.InvalidCredentialException;
import com.ibra.projecttracker.exception.InvalidTokenException;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.*;
import com.ibra.projecttracker.security.AuthUser;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.service.AdminService;
import com.ibra.projecttracker.service.AuthService;
import com.ibra.projecttracker.service.DeveloperService;
import com.ibra.projecttracker.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final JwtUtils jwtUtils;
    private final AuditLogService auditLogService;
    private final AuthenticationManager authenticationManager;
    private final DeveloperService developerService;
    private final ObjectMapper objectMapper;
    private final AdminService adminService;
    private final ManagerService managerService;

    public AuthServiceImpl(UserRepository userRepository, EntityDTOMapper entityDTOMapper, JwtUtils jwtUtils, AuditLogService auditLogService, AuthenticationManager authenticationManager, DeveloperService developerService, ObjectMapper objectMapper, AdminService adminService, ManagerService managerService) {
        this.userRepository = userRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.jwtUtils = jwtUtils;
        this.auditLogService = auditLogService;
        this.authenticationManager = authenticationManager;
        this.developerService = developerService;
        this.objectMapper = objectMapper;
        this.adminService = adminService;
        this.managerService = managerService;
    }


    @Override
    public UserDTO createUser(UserRegistrationRequest request) {

        if (request.getFirstName() == null || request.getLastName() == null || request.getPassword() == null ||
                request.getEmail() == null) {
            throw new IllegalArgumentException("Username, password, email, first name, and last name are required.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User createdUser = switch (request.getUserType()) {
            case "DEVELOPER" -> {
                DeveloperRegistrationDetails devDetails = objectMapper.convertValue(
                        request.getDetails(), DeveloperRegistrationDetails.class);
                yield developerService.createDeveloper(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword().toLowerCase(),
                        request.getPhoneNumber(),
                        devDetails.getSkill()
                );
            }
            case "ADMIN" -> {
                AdminRegistrationDetails adminDetails = objectMapper.convertValue(
                        request.getDetails(), AdminRegistrationDetails.class);
                yield adminService.createAdmin(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword().toLowerCase(),
                        request.getPhoneNumber(),
                        adminDetails.getAdminLevel()
                );
            }
            case "MANAGER" -> {
                ManagerRegistrationDetails managerDetails = objectMapper.convertValue(
                        request.getDetails(), ManagerRegistrationDetails.class);
                yield managerService.createManager(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword().toLowerCase(),
                        request.getPhoneNumber(),
                        managerDetails.getDepartment()

                );
            }
            default -> throw new IllegalArgumentException("Unsupported user type: " + request.getUserType());
        };

        auditLogService.logCreate(createdUser.getUserRole().name(), createdUser.getId().toString(), createdUser);

        return entityDTOMapper.mapUserToUserDTO(createdUser);
    }

    @Override
    public Map<String, String> loginUser(AuthRequest authRequest) {
        log.info("Attempting to login user with email: {}", authRequest.email());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email().toLowerCase(), authRequest.password()));
        } catch (AuthenticationException e){
            throw new InvalidCredentialException("Invalid Email or Password");
        }

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        User user = authUser.getUser();
        log.info("Authentication successful for user: {}", user.getEmail());

        String token = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(authRequest.email());

        Map<String, Long> accessTokenTime = jwtUtils.getTokenTimeDetails(token);
        Map<String, Long> refreshTokenTime = jwtUtils.getTokenTimeDetails(refreshToken);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

//        auditLogService.logLogin(user.getUserRole().name(), user.getId().toString(), user);

        AuditLog.loginLog(user.getFirstName() + " " + user.getLastName(), "{}");


        return Map.of(
                "accessToken", token,
                "refreshToken", refreshToken,
                "accessTokenExpiresIn", String.valueOf(accessTokenTime.get("remainingTimeMs") / 1000),
                "refreshTokenExpiresIn", String.valueOf(refreshTokenTime.get("remainingTimeMs") / 1000),
                "userRole", user.getUserRole().name(),
                "email", user.getEmail()
        );
    }

    @Override
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new InvalidTokenException("Refresh token must be provided in Authorization header with Bearer prefix");
        }

        String refreshToken = bearerToken.substring(7);

        String email = jwtUtils.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String newAccessToken = jwtUtils.generateToken(user.getEmail());
        String newRefreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        Map<String, Long> accessTokenTime = jwtUtils.getTokenTimeDetails(newAccessToken);
        Map<String, Long> refreshTokenTime = jwtUtils.getTokenTimeDetails(newRefreshToken);

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken,
                "accessTokenExpiresIn", String.valueOf(accessTokenTime.get("remainingTimeMs") / 1000),
                "refreshTokenExpiresIn", String.valueOf(refreshTokenTime.get("remainingTimeMs") / 1000),
                "userRole", user.getUserRole().name()
        );
    }

}
