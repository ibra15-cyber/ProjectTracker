package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.*;
import com.ibra.projecttracker.service.TaskService;
import com.ibra.projecttracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
   private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        Response response = Response.builder()
                .message("All users retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .users(userDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.getUserById(id);
        Response response = Response.builder()
                .message("User retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .user(userDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
        Response response = Response.builder()
                .message("User updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .user(updatedUserDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        Response response = Response.builder()
                .message("User deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
