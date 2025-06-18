package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.*;
import com.ibra.projecttracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Helper method to build responses
    private UserResponse buildUserResponse(
            String message,
            HttpStatus status,
            UserDTO user,
            List<UserDTO> users) {
        return UserResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .user(user)
                .users(users)
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> loggedInUser() {
        UserDTO userDTO = userService.getLoginUser();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User retrieved successfully",
                        HttpStatus.OK,
                        userDTO,
                        null));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "All users retrieved successfully",
                        HttpStatus.OK,
                        null,
                        userDTOS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User retrieved successfully",
                        HttpStatus.OK,
                        userDTO,
                        null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User updated successfully",
                        HttpStatus.OK,
                        updatedUserDTO,
                        null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> deleteTask(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User deleted successfully",
                        HttpStatus.OK,
                        null,
                        null));
    }
}