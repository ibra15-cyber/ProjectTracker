package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.*;
import com.ibra.projecttracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
   private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> loggedInUser() {
        UserDTO userDTO = userService.getLoginUser();
        UserResponse response = UserResponse.builder()
                .message("User retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .user(userDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        UserResponse response = UserResponse.builder()
                .message("All users retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .users(userDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.getUserById(id);
        UserResponse response = UserResponse.builder()
                .message("User retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .user(userDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
        UserResponse response = UserResponse.builder()
                .message("User updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .user(updatedUserDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> deleteTask(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        UserResponse response = UserResponse.builder()
                .message("User deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
