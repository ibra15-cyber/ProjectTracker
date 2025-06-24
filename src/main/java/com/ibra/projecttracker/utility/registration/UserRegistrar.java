package com.ibra.projecttracker.utility.registration;

import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.enums.UserRole;

import java.util.Map;

public interface UserRegistrar {
    UserRole getUserRole();
    User register(
            String firstName,
            String lastName,
            String email,
            String rawPassword,
            String phoneNumber,
            Map<String, Object> details
    );
}
