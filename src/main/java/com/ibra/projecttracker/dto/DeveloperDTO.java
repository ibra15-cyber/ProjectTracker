package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.enums.DevSkills;
import com.ibra.projecttracker.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
public class DeveloperDTO {
    private Long id;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;


    private String phoneNumber;

    private DevSkills skill;

    private UserRole userRole;

}

