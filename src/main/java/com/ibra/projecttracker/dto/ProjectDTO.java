package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.ProjectStatus;
import lombok.Data;

import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Future(message = "Deadline must be in the future")
    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    private ProjectStatus status;

    private List<TaskDTO> taskDTOS;
}
