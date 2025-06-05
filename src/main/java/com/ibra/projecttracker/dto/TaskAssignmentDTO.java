package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.TaskAssignmentStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class TaskAssignmentDTO {
    private Long taskAssignmentId;

    @Positive(message = "Task ID must be positive")
    private Long taskId;

    private String taskTitle; //might comment it out for


    @Positive(message = "Assignee ID must be positive")
    private Long developerId;
    private String developerName;

    private LocalDateTime assignedOn; // This will be set automatically in service

    private TaskAssignmentStatus status;

    @Future(message = "Due date must be in the future")
    private LocalDateTime deadline;

    private LocalDateTime completedOn;
}