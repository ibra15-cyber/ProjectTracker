package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.entity.TaskAssignment;

import java.util.List;

public interface TaskAssignmentService {
    TaskAssignment createTask(TaskAssignmentDTO taskAssignmentDTO);
    List<TaskAssignment> getAllTaskAssignments();
    TaskAssignment getTaskAssignmentById(Long taskId);
    TaskAssignment updateTask(Long taskId, TaskAssignmentDTO taskAssignmentDTO);
    void deleteTask(Long taskId);
    List<TaskAssignmentDTO> getAllTaskAssignmentByDeveloper(Long developerId);
}
