package com.ibra.projecttracker.dto.request;

import com.ibra.projecttracker.enums.DevSkills;
import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Map;

@Data
public class UserRegistrationRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private String userType;
    private Map<String, Object> details;
}
