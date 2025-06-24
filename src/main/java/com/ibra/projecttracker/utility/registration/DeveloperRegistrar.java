package com.ibra.projecttracker.utility.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.dto.request.DeveloperRegistrationDetails;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.service.DeveloperService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DeveloperRegistrar implements UserRegistrar {

    private final DeveloperService developerService;
    private final ObjectMapper objectMapper;

    public DeveloperRegistrar(DeveloperService developerService, ObjectMapper objectMapper) {
        this.developerService = developerService;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.DEVELOPER;
    }

    @Override
    public User register(String firstName, String lastName, String email, String rawPassword, String phoneNumber, Map<String, Object> details) {
        DeveloperRegistrationDetails devDetails = objectMapper.convertValue(details, DeveloperRegistrationDetails.class);
        return developerService.createDeveloper(
                firstName,
                lastName,
                email,
                rawPassword,
                phoneNumber,
                devDetails.getSkill()
        );
    }
}