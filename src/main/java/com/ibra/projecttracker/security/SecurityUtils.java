package com.ibra.projecttracker.security;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.service.TaskAssignmentService;
import com.ibra.projecttracker.service.UserService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final TaskAssignmentService taskAssignmentService;
    private final UserService userService;

    public SecurityUtils(TaskAssignmentService taskAssignmentService, UserService userService) {
        this.taskAssignmentService = taskAssignmentService;
        this.userService = userService;
    }

    public boolean isTaskOwner(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        UserDTO currentUser = userService.getLoginUser();

        if (currentUser == null || currentUser.id() == null) {
            // Add robust logging here if this happens for an authenticated user.
            // System.err.println("Error: Authenticated user has no valid UserDTO or ID. Principal: " + auth.getPrincipal());
            return false;
        }

        Long currentUserId = currentUser.id();

        try {
            // This method call remains the same, but its implementation in TaskAssignmentServiceImpl
            // will now use your chosen @Query method.
            return taskAssignmentService.isTaskAssignedToDeveloperUser(taskId, currentUserId);
        } catch (Exception e) {
            // Log any exceptions that occur during the query execution
            System.err.println("Exception during isTaskOwner check for taskId " + taskId + " and userId " + currentUserId + ": " + e.getMessage());
            e.printStackTrace(); // Print stack trace for full details
            return false;
        }
    }
}