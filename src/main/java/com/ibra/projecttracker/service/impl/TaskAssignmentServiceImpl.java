package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.repository.ProjectRepository;
import com.ibra.projecttracker.repository.TaskAssignmentRepository;
import com.ibra.projecttracker.repository.TaskRepository;
import com.ibra.projecttracker.service.TaskAssignmentService;
import com.ibra.projecttracker.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    private final TaskRepository taskRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;
    private final TaskService taskService;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final AuditLogService auditLogService;

    public TaskAssignmentServiceImpl(TaskRepository taskRepository, EntityDTOMapper entityDTOMapper, ProjectRepository projectRepository, DeveloperRepository developerRepository, TaskService taskService, TaskAssignmentRepository taskAssignmentRepository, AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.projectRepository = projectRepository;
        this.developerRepository = developerRepository;
        this.taskService = taskService;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public TaskAssignment createTask(TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment taskAssignment = new TaskAssignment();

        Task task = taskRepository.findById(taskAssignmentDTO.getTaskId())
                .orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        taskAssignment.setTask(task);

        Developer developer = developerRepository.findById(taskAssignmentDTO.getAssigneeId())
                .orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
        taskAssignment.setDeveloper(developer);

        taskAssignment.setStatus(taskAssignmentDTO.getStatus());
        taskAssignment.setAssignedOn(taskAssignmentDTO.getAssignedOn());
        taskAssignment.setCompletedOn(taskAssignmentDTO.getDueOn());

        TaskAssignment savedTaskAssignment = taskAssignmentRepository.save(taskAssignment);

        auditLogService.logTaskAssignmentCreate(savedTaskAssignment.getTaskAssignmentId(), savedTaskAssignment.getAssignedOn());

        return savedTaskAssignment;
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

        if(taskAssignmentDTO.getAssigneeId() != null){
            Developer developer = developerRepository.findById(taskAssignmentDTO.getAssigneeId())
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
        if(taskAssignmentDTO.getDueOn() != null) taskAssignmentToUpdate.setAssignedOn(taskAssignmentDTO.getAssignedOn());
        TaskAssignment updatedTaskAssignment =  taskAssignmentRepository.save(taskAssignmentToUpdate);

        auditLogService.logTaskAssignmentUpdate(updatedTaskAssignment.getTaskAssignmentId(), taskAssignmentToUpdate, updatedTaskAssignment);

        return updatedTaskAssignment;

    }

    @Override
    public void deleteTask(Long assignTaskId) {
        TaskAssignment taskAssignmentToDelete = taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));

        taskAssignmentRepository.delete(taskAssignmentToDelete);

        auditLogService.logTaskAssignmentDelete(assignTaskId, taskAssignmentToDelete);
    }
}
