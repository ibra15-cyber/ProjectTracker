package com.ibra.projecttracker.dto;

import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private Long projectId;
    private Set<TaskAssignment> taskAssignments;
}
