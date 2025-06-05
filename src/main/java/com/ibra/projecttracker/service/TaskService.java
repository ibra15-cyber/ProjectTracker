package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.TaskDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long taskId);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);
    void deleteTask(Long taskId);
    List<TaskDTO> getTasksByProjectId(Long projectId);
}
