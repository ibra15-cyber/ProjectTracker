package com.ibra.projecttracker.dto.request;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.enums.DevSkills;
import lombok.Data;

import java.util.Set;

@Data
public class AdminRegistrationDetails {
    private String adminLevel;
}
