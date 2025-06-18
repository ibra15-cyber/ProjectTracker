package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.request.AuthRequest;
import com.ibra.projecttracker.dto.response.AuthResponse;
import com.ibra.projecttracker.dto.request.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.debug(userCreateRequest.toString());
        UserDTO userDTO = userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse("User created successfully", HttpStatus.CREATED, userDTO, null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        Map<String, String> loginResponse = userService.loginUser(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse("User logged in successfully", HttpStatus.CREATED, null, loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        Map<String, String> tokenMap = userService.refreshToken(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildAuthResponse("Token refreshed successfully", HttpStatus.OK, null, tokenMap));
    }


    private AuthResponse buildAuthResponse(String message, HttpStatus status, UserDTO user, Map<String, String> loginResponse) {
        return AuthResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .user(user)
                .loginResponse(loginResponse)
                .build();
    }

}
