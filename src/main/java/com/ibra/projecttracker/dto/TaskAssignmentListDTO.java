package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.TaskAssignmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskAssignmentListDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String taskTitle;

    private TaskAssignmentStatus status;

    @Future(message = "Due date must be in the future")
    private LocalDateTime deadline;

}