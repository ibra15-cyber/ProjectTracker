package com.ibra.projecttracker.security;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.service.TaskAssignmentService;
import com.ibra.projecttracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("securityUtils")
public class SecurityUtils {

    private final TaskAssignmentService taskAssignmentService;
    private final UserService userService;

    public SecurityUtils(TaskAssignmentService taskAssignmentService, UserService userService) {
        this.taskAssignmentService = taskAssignmentService;
        this.userService = userService;
    }

    public boolean isTaskOwner(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        UserDTO currentUser = userService.getLoginUser();
        List<TaskAssignmentDTO> userAssignments =
            taskAssignmentService.getAllTaskAssignmentByDeveloper(currentUser.id());
        
        return userAssignments.stream()
            .anyMatch(assignment -> assignment.getTaskId().equals(taskId));
    }
}