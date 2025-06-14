package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.DevSkills;
import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserCreateRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private DevSkills devSkills;
}
