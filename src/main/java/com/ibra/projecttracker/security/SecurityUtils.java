package com.ibra.projecttracker.security;

import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.service.TaskAssignmentService;
import com.ibra.projecttracker.service.UserService;
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
            return false;
        }

        Long currentUserId = currentUser.id();

        try {
            return taskAssignmentService.isTaskAssignedToDeveloperUser(taskId, currentUserId);
        } catch (Exception e) {
            System.err.println("Exception during isTaskOwner check for taskId " + taskId + " and userId " + currentUserId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}