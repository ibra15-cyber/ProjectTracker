package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private ProjectStatus status;
    private List<TaskDTO> taskDTOS;
}
