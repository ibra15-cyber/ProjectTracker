package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignTaskRequest {
    private Long taskId;
    private Long assigneeId;
    private LocalDateTime assignedOn;
    private TaskStatus status;
    private LocalDateTime dueOn;
}
