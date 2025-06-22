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
    private Long timestamp;
    private ProjectDTO project;
    private List<ProjectDTO> projects;
    private TaskDTO task;
    private List<TaskDTO> tasks;
    private DeveloperResponseDTO developer;
    private List<DeveloperResponseDTO> developers;
    private TaskAssignmentDTO taskAssignment;
    private List<TaskAssignmentDTO> taskAssignments;
    private Page<DeveloperResponseDTO> developerPage;
    private Page<ProjectDTO> projectPage;

    private UserDTO user;
    private List<UserDTO> users;
    private String token;
    private UserDTO userRole;
}
