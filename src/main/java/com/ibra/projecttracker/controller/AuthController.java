package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.AuthRequest;
import com.ibra.projecttracker.dto.AuthResponse;
import com.ibra.projecttracker.dto.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.security.jwt.JwtUtils;
import com.ibra.projecttracker.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthController(UserService userService, JwtUtils jwtUtils, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.debug(userCreateRequest.toString());
        UserDTO userDTO = userService.createUser(userCreateRequest);
        AuthResponse response = AuthResponse.builder()
                .message("User created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .user(userDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        Map<String, String> loginResponse = userService.loginUser(authRequest);
        AuthResponse response = AuthResponse.builder()
                .message("User created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .loginResponse(loginResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        Map<String, String> tokenMap = userService.refreshToken(request);

        AuthResponse response = AuthResponse.builder()
                .message("Token refreshed successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .tokenRefresh(tokenMap)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
