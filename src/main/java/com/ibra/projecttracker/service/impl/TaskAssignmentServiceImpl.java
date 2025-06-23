package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.entity.User;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.*;
import com.ibra.projecttracker.service.AuthService;
import com.ibra.projecttracker.service.TaskAssignmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    private final TaskRepository taskRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final DeveloperRepository developerRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;
    private final AuthService authService;

    public TaskAssignmentServiceImpl(TaskRepository taskRepository, EntityDTOMapper entityDTOMapper, DeveloperRepository developerRepository, TaskAssignmentRepository taskAssignmentRepository, AuditLogService auditLogService, UserRepository userRepository, AuthService authService) {
        this.taskRepository = taskRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.developerRepository = developerRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public TaskAssignment createTask(TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment taskAssignment = new TaskAssignment();

        Task task = taskRepository.findById(taskAssignmentDTO.getTaskId())
                .orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        taskAssignment.setTask(task);

        Developer developer = developerRepository.findById(taskAssignmentDTO.getDeveloperId())
                .orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
        taskAssignment.setDeveloper(developer);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User assignedBy = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        taskAssignment.setAssignedBy(assignedBy);

        taskAssignment.setStatus(taskAssignmentDTO.getStatus());
        taskAssignment.setAssignedOn(LocalDateTime.now());
        taskAssignment.setCompletedOn(taskAssignmentDTO.getCompletedOn());
        taskAssignment.setDeadline(task.getDeadline()); //use task's deadline

        return taskAssignmentRepository.save(taskAssignment);
    }

    @Override
    public List<TaskAssignment> getAllTaskAssignments() {
        return taskAssignmentRepository.findAll();
    }

    @Override
    public TaskAssignment getTaskAssignmentById(Long assignTaskId) {
        TaskAssignment foundTaskAssignment =  taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));
        auditLogService.logRead("TASK_ASSIGNMENT", foundTaskAssignment.getTaskAssignmentId().toString());
        return foundTaskAssignment;

    }


    @Override
    public TaskAssignment updateTask(Long assignTaskId, TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment taskAssignmentToUpdate = taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));

        if(taskAssignmentDTO.getDeveloperId() != null){
            Developer developer = developerRepository.findById(taskAssignmentDTO.getDeveloperId())
                            .orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
            taskAssignmentToUpdate.setDeveloper(developer);
        }

        if(taskAssignmentDTO.getTaskId() != null){
            Task task = taskRepository.findById(taskAssignmentDTO.getTaskId())
                    .orElseThrow(()-> new ResourceNotFoundException("Task not found"));
            taskAssignmentToUpdate.setTask(task);
        }

        if(taskAssignmentDTO.getStatus() != null) taskAssignmentToUpdate.setStatus(taskAssignmentDTO.getStatus());
        if(taskAssignmentDTO.getAssignedOn() != null) taskAssignmentToUpdate.setAssignedOn(taskAssignmentDTO.getAssignedOn());
        if(taskAssignmentDTO.getDeadline() != null) taskAssignmentToUpdate.setAssignedOn(taskAssignmentDTO.getAssignedOn());

        return taskAssignmentRepository.save(taskAssignmentToUpdate);

    }

    @Override
    public void deleteTask(Long assignTaskId) {
        TaskAssignment taskAssignmentToDelete = taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));

        taskAssignmentRepository.delete(taskAssignmentToDelete);
    }

    @Override
    public List<TaskAssignmentDTO> getAllTaskAssignmentByDeveloper(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
        List<TaskAssignment> taskAssignments = taskAssignmentRepository.findByDeveloper(developer);
        return taskAssignments.stream()
                .map(entityDTOMapper::mapTaskAssignmentToDTO)
                .collect(Collectors.toList());
    }


}
