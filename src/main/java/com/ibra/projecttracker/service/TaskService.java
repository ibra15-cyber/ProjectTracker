package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.TaskDTO;

import java.util.List;

public interface TaskService  {
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long taskId);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);
    void deleteTask(Long taskId);
    List<TaskDTO> getTasksByProjectId(Long projectId);
    List<TaskDTO> getAllTasksBySort(String sortBy, String sortDirection);
    List<TaskDTO> findOverdueTasks();
    List<Object[]> findTaskCountsGroupedByStatusAndProject();
}