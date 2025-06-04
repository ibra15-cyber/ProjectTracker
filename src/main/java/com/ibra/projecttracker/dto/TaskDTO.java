package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.enums.TaskStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class TaskDTO {
    private Long id;

    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private TaskStatus status;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    @Positive(message = "Project ID must be positive")
    private Long projectId;

    private List<TaskAssignment> taskAssignments;
}
