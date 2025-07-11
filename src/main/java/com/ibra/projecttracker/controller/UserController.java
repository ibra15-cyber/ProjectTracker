package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.*;
import com.ibra.projecttracker.dto.request.UpdateUserRequest;
import com.ibra.projecttracker.dto.response.UserSuccessResponse;
import com.ibra.projecttracker.service.TaskAssignmentService;
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
    private final TaskAssignmentService taskAssignmentService;

    public UserController(UserService userService, TaskAssignmentService taskAssignmentService) {
        this.userService = userService;
        this.taskAssignmentService = taskAssignmentService;
    }

    private UserSuccessResponse buildUserResponse(
            String message,
            HttpStatus status,
            UserDTO user,
            List<UserDTO> users,
            List<TaskAssignmentDTO> taskAssignments,
            List<TaskAssignmentListDTO> taskAssignmentList) {
        return UserSuccessResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .user(user)
                .users(users)
                .taskAssignments(taskAssignments)
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserSuccessResponse> loggedInUser() {
        UserDTO userDTO = userService.getLoginUser();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User retrieved successfully",
                        HttpStatus.OK,
                        userDTO,
                        null, null, null));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserSuccessResponse> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "All users retrieved successfully",
                        HttpStatus.OK,
                        null,
                        userDTOS,null, null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (isAuthenticated() and @securityUtils.isUserOwner(#id))")
    public ResponseEntity<UserSuccessResponse> getUserById(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User retrieved successfully",
                        HttpStatus.OK,
                        userDTO,
                        null, null, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (isAuthenticated() and @SecurityUtils.isUserOwner(#id))")
    public ResponseEntity<UserSuccessResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User updated successfully",
                        HttpStatus.OK,
                        updatedUserDTO,
                        null ,null, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserSuccessResponse> deleteTask(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "User deleted successfully",
                        HttpStatus.OK,
                        null,
                        null, null, null));
    }


    @GetMapping("/{id}/tasks")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER') or (hasAuthority('DEVELOPER') and @SecurityUtils.isDeveloperOwner(#id))")
    public ResponseEntity<UserSuccessResponse> getTaskAssignedToDeveloper(@PathVariable("id") Long id) {
        List<TaskAssignmentListDTO> taskAssignmentByDeveloper = taskAssignmentService.getAllTaskAssignmentByDeveloper(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildUserResponse(
                        "Task retrieved successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        null
                , taskAssignmentByDeveloper));
    }
}