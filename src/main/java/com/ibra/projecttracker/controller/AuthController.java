package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.request.AuthRequest;
import com.ibra.projecttracker.dto.response.AuthSuccessResponse;
import com.ibra.projecttracker.dto.request.UserRegistrationRequest;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.service.AuthService;
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

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthSuccessResponse> createUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.debug(userRegistrationRequest.toString());
        UserDTO userDTO = authService.createUser(userRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse("User created successfully", HttpStatus.CREATED, userDTO, null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthSuccessResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        Map<String, String> loginResponse = authService.loginUser(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse("User logged in successfully", HttpStatus.CREATED, null, loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthSuccessResponse> refreshToken(HttpServletRequest request) {
        Map<String, String> tokenMap = authService.refreshToken(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildAuthResponse("Token refreshed successfully", HttpStatus.OK, null, tokenMap));
    }


    private AuthSuccessResponse buildAuthResponse(String message, HttpStatus status, UserDTO user, Map<String, String> loginResponse) {
        return AuthSuccessResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .user(user)
                .loginResponse(loginResponse)
                .build();
    }

}
