package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.enums.DevSkills;
import lombok.Data;

import java.util.Set;

@Data
public class DeveloperDTO {
    private Long id;
    private String name;
    private String email;
    private DevSkills skill;
    private Set<TaskAssignment> taskAssignment;
}
