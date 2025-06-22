package com.ibra.projecttracker.dto.request;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.enums.DevSkills;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Set;

@Data
public class DeveloperUpdateDetails {
    @Enumerated(EnumType.STRING)
    private DevSkills skill;
    private Set<TaskAssignment> taskAssignment;
}
