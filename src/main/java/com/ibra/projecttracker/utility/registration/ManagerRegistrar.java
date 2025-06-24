package com.ibra.projecttracker.utility.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.request.ManagerRegistrationDetails;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.service.ManagerService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManagerRegistrar implements UserRegistrar {

    private final ManagerService managerService;
    private final ObjectMapper objectMapper;

    public ManagerRegistrar(ManagerService managerService, ObjectMapper objectMapper) {
        this.managerService = managerService;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.MANAGER;
    }

    @Override
    public User register(String firstName, String lastName, String email, String rawPassword, String phoneNumber, Map<String, Object> details) {
        ManagerRegistrationDetails managerDetails = objectMapper.convertValue(details, ManagerRegistrationDetails.class);
        return managerService.createManager(
                firstName,
                lastName,
                email,
                rawPassword,
                phoneNumber,
                managerDetails.getDepartment()
        );
    }
}