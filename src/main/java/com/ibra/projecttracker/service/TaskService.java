package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.Task;

import java.util.List;

public interface TaskService  {
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long taskId);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);
    void deleteTask(Long taskId);
    List<TaskDTO> getTasksByProjectId(Long projectId);
    List<Task> getAllTasksBySort(String sortBy, String sortDirection);
}