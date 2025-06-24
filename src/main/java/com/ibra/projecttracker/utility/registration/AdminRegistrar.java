package com.ibra.projecttracker.utility.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.request.AdminRegistrationDetails;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminRegistrar implements UserRegistrar {

    private final ObjectMapper objectMapper;
    private final AdminService adminService;

    public AdminRegistrar(ObjectMapper objectMapper, AdminService adminService) {
        this.objectMapper = objectMapper;
        this.adminService = adminService;
    }


    @Override
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }

    @Override
    public User register(String firstName, String lastName, String email, String rawPassword, String phoneNumber, Map<String, Object> details) {
        AdminRegistrationDetails adminDetails = objectMapper.convertValue(details, AdminRegistrationDetails.class);
        return adminService.createAdmin(
                firstName,
                lastName,
                email,
                rawPassword,
                phoneNumber,
                adminDetails.getAdminLevel()
        );
    }
}