package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.AssignTaskRequest;
import com.ibra.projecttracker.entity.TaskAssignment;

import java.util.List;

public interface TaskAssignmentService {
    TaskAssignment createTask(AssignTaskRequest assignTaskRequest);
    List<TaskAssignment> getAllTaskAssignments();
    TaskAssignment getTaskAssignmentById(Long taskId);
    TaskAssignment updateTask(Long taskId, AssignTaskRequest assignTaskRequest);
    void deleteTask(Long taskId);
}
