package com.ibra.projecttracker.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
public class Response {
    private String message;
    private String statusCode;
    private ProjectDTO project;
    private List<ProjectDTO> projects;
    private TaskDTO task;
    private List<TaskDTO> tasks;
    private DeveloperDTO developer;
    private List<DeveloperDTO> developers;
    private TaskAssignmentDTO taskAssignment;
    private List<TaskAssignmentDTO> taskAssignments;
    private Page<DeveloperDTO> developerPage;
    private Page<ProjectDTO> projectPage;
}
