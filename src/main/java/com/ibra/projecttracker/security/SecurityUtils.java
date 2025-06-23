package com.ibra.projecttracker.security;

import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.repository.UserRepository;
import com.ibra.projecttracker.repository.TaskRepository;
import com.ibra.projecttracker.repository.TaskAssignmentRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityUtils")
public class SecurityUtils {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository; // NEW INJECTION

    public SecurityUtils(UserRepository userRepository, TaskRepository taskRepository, TaskAssignmentRepository taskAssignmentRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    public boolean isTaskOwner(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        String currentUsername = auth.getName();
        User currentDeveloper = userRepository.findByEmail(currentUsername).orElse(null);

        if (currentDeveloper == null) {
            return false;
        }

        // Fetch the Task to ensure it exists (optional, but good practice)
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return false;
        }

        boolean result = taskAssignmentRepository.existsByTaskAndDeveloper(task, currentDeveloper);
        return result;
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.ADMIN.name()));
    }

    public boolean isUserOwner(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        return user != null && user.getId().equals(userId);
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

        if (user.getUserRole() == UserRole.DEVELOPER) {
            return user.getId().equals(developerId);
        }
        return false;
    }
}