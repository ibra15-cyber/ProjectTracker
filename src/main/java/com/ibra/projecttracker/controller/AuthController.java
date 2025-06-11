package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.AuthRequest;
import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.dto.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserDTO userDTO = userService.createUser(userCreateRequest);
        Response response = Response.builder()
                .message("User created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .user(userDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        String token = userService.loginUser(authRequest);
        Response response = Response.builder()
                .message("User created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .token(token)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
