package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.TaskAssignmentStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class TaskAssignmentDTO {
    private Long taskAssignmentId;
    @NotNull(message = "Task ID is required")
    @Positive(message = "Task ID must be positive")
    private Long taskId;

    private String taskTitle; //might comment it out for


    @NotNull(message = "Assignee ID is required")
    @Positive(message = "Assignee ID must be positive")
    private Long assigneeId;
    private String developerName;

    private LocalDateTime assignedOn; // This will be set automatically in service

    @NotNull(message = "Status is required")
    private TaskAssignmentStatus status;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueOn;

    private LocalDateTime completedOn;
}