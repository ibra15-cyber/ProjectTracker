package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.ProjectRepository;
import com.ibra.projecttracker.repository.TaskRepository;
import com.ibra.projecttracker.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public TaskServiceImpl(TaskRepository taskRepository, EntityDTOMapper entityDTOMapper, ProjectRepository projectRepository, AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task newTask = new Task();
        newTask.setTitle(taskDTO.getTitle());
        newTask.setDescription(taskDTO.getDescription());
        newTask.setStatus(taskDTO.getStatus());
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setDeadline(taskDTO.getDeadline());
        Long projectId = taskDTO.getProjectId();
        Project foundProject = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        newTask.setProject(foundProject);
        Task savedTask = taskRepository.save(newTask);

        auditLogService.logTaskCreate(savedTask.getTaskId(), savedTask);

        return entityDTOMapper.mapTaskToTaskDTO(savedTask);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(entityDTOMapper::mapTaskToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        auditLogService.logRead("TASK", task.getTaskId().toString());

        return entityDTOMapper.mapTaskToTaskDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        log.debug(taskDTO.toString());
        Task taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        if (taskDTO.getTitle() != null) taskToUpdate.setTitle(taskDTO.getTitle());
        if (taskDTO.getDescription() != null) taskToUpdate.setDescription(taskDTO.getDescription());
        if (taskDTO.getStatus() != null) {
            taskToUpdate.setStatus(taskDTO.getStatus());
//            auditLogService.logTaskStatusChange(taskId, taskToUpdate.getStatus().toString(), taskDTO.getStatus().toString());
        }
        if (taskDTO.getDeadline() != null) {
            taskToUpdate.setDeadline(taskDTO.getDeadline());
//            auditLogService.logTaskDeadlineChange(taskId, taskToUpdate.getDeadline(), taskDTO.getDeadline());
        }
        if (taskDTO.getProjectId() != null) {
            Project project = projectRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found!"));
            taskToUpdate.setProject(project);
        }

        Task savedTask = taskRepository.save(taskToUpdate);

        auditLogService.logTaskUpdate(savedTask.getTaskId(), taskToUpdate, savedTask);

        return entityDTOMapper.mapTaskToTaskDTO(savedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task taskToDelete = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        auditLogService.logTaskDelete(taskToDelete.getTaskId(), taskToDelete);

        taskRepository.delete(taskToDelete);
    }

    @Override
    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        List<Task> tasks = taskRepository.findByProject(project);
        return tasks.stream()
                .map(entityDTOMapper::mapTaskToTaskDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getAllTasksBySort(String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        if (!isValidSortProperty(sortBy)) {
            throw new IllegalArgumentException("Invalid sort property: " + sortBy);
        }

        Sort sort = Sort.by(direction, sortBy); // Spring Data JPA handles the type mapping
        List<Task> tasks = taskRepository.findAll(sort);
        return tasks.stream()
                .map(entityDTOMapper::mapTaskToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findOverdueTasks() {
        List<Task> tasks =  taskRepository.findOverdueTasks();
        return tasks.stream().
                map(entityDTOMapper::mapTaskToTaskDTO).toList();
    }

    @Override
    public List<Object[]> findTaskCountsGroupedByStatusAndProject() {
        return taskRepository.findTaskCountsGroupedByStatusAndProject();
    }


    private boolean isValidSortProperty(String property) {
        return property != null && (
                property.equalsIgnoreCase("createdAt") ||
                        property.equalsIgnoreCase("deadline") ||
                        property.equalsIgnoreCase("status")
        );
    }
}
