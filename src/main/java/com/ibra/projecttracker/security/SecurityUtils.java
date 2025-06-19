package com.ibra.projecttracker.security;

import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.service.TaskAssignmentService;
import com.ibra.projecttracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityUtils")
public class SecurityUtils {

    private final TaskAssignmentService taskAssignmentService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DeveloperRepository developerRepository;

    public SecurityUtils(TaskAssignmentService taskAssignmentService, UserService userService, UserRepository userRepository, DeveloperRepository developerRepository) {
        this.taskAssignmentService = taskAssignmentService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.developerRepository = developerRepository;
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

    public boolean isUserOwner(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        return user != null && user.getUserId().equals(userId);
    }

    public boolean isDeveloperOwner(Long developerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null) {
            return false;
        }

        Developer developer = developerRepository.findById(developerId).orElse(null);
        return developer != null && developer.getUser().getDeveloper().equals(user.getUserId());
    }

}