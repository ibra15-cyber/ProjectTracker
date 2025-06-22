package com.ibra.projecttracker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.dto.request.AdminUpdateDetails;
import com.ibra.projecttracker.dto.request.DeveloperUpdateDetails;
import com.ibra.projecttracker.dto.request.ManagerUpdateDetails;
import com.ibra.projecttracker.dto.request.UpdateUserRequest;
import com.ibra.projecttracker.entity.*;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.*;
import com.ibra.projecttracker.service.AdminService;
import com.ibra.projecttracker.service.DeveloperService;
import com.ibra.projecttracker.service.ManagerService;
import com.ibra.projecttracker.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final DeveloperService developerService;
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;
    private final AdminService adminService;
    private final ManagerService managerService;

    public UserServiceImpl(UserRepository userRepository, EntityDTOMapper entityDTOMapper, DeveloperService developerService, ObjectMapper objectMapper, AuditLogService auditLogService, AdminService adminService, ManagerService managerService) {
        this.userRepository = userRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.developerService = developerService;
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
        this.adminService = adminService;
        this.managerService = managerService;
    }


    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(entityDTOMapper::mapUserToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        auditLogService.logRead(user.getUserRole().toString(), user.getId().toString());

        return entityDTOMapper.mapUserToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail().toLowerCase());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);

//        auditLogService.logDeveloperUpdate(user.getId(), user, updatedUser);


        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            if (updatedUser instanceof Developer) {
                DeveloperUpdateDetails devUpdateDTO = objectMapper.convertValue(
                        request.getDetails(), DeveloperUpdateDetails.class);
                developerService.updateDeveloper(userId, devUpdateDTO); // Pass the original userId
            } else if (updatedUser instanceof Admin) {
                AdminUpdateDetails adminUpdateDTO = objectMapper.convertValue(
                        request.getDetails(), AdminUpdateDetails.class);
                adminService.updateAdmin(userId, adminUpdateDTO);
                log.warn("Attempted to update Admin-specific details for user ID {} but AdminService.updateAdmin is not yet implemented or wired.", userId);
            }
            if (updatedUser instanceof Manager) {
                ManagerUpdateDetails managerUpdateDTO = objectMapper.convertValue(
                        request.getDetails(), ManagerUpdateDetails.class);
                managerService.updateManager(userId, managerUpdateDTO);
                log.warn("Attempted to update Manager-specific details for user ID {} but ManagerService.updateManager is not yet implemented or wired.", userId);

            }
        }

        return entityDTOMapper.mapUserToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        userRepository.delete(user);

        auditLogService.logDeveloperDelete(user.getId(), user);
    }


    @Override
    public UserDTO getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User email is: {}", email);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        return entityDTOMapper.mapUserToUserDTO(loggedInUser);
    }
}