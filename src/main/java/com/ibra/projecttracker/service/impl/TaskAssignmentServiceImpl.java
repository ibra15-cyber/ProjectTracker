package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.AssignTaskRequest;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Project;
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
import java.util.stream.Collectors;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    private final TaskRepository taskRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;
    private final TaskService taskService;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public TaskAssignmentServiceImpl(TaskRepository taskRepository, EntityDTOMapper entityDTOMapper, ProjectRepository projectRepository, DeveloperRepository developerRepository, TaskService taskService, TaskAssignmentRepository taskAssignmentRepository) {
        this.taskRepository = taskRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.projectRepository = projectRepository;
        this.developerRepository = developerRepository;
        this.taskService = taskService;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    @Override
    public TaskAssignment createTask(AssignTaskRequest assignTaskRequest) {
        TaskAssignment taskAssignment = new TaskAssignment();

        Task task = taskRepository.findById(assignTaskRequest.getTaskId())
                .orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        taskAssignment.setTask(task);

        Developer developer = developerRepository.findById(assignTaskRequest.getAssigneeId())
                .orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
        taskAssignment.setDeveloper(developer);

        taskAssignment.setStatus(assignTaskRequest.getStatus());
        taskAssignment.setAssignedOn(assignTaskRequest.getAssignedOn());
        taskAssignment.setCompletedOn(assignTaskRequest.getDueOn());

        return taskAssignmentRepository.save(taskAssignment);
    }

    @Override
    public List<TaskAssignment> getAllTaskAssignments() {
        return taskAssignmentRepository.findAll();
    }

    @Override
    public TaskAssignment getTaskAssignmentById(Long assignTaskId) {
        return taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));
    }


    @Override
    public TaskAssignment updateTask(Long assignTaskId, AssignTaskRequest assignTaskRequest) {
        TaskAssignment taskAssignment = taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));

        if(assignTaskRequest.getAssigneeId() != null){
            Developer developer = developerRepository.findById(assignTaskRequest.getAssigneeId())
                            .orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
            taskAssignment.setDeveloper(developer);
        }

        if(assignTaskRequest.getTaskId() != null){
            Task task = taskRepository.findById(assignTaskRequest.getTaskId())
                    .orElseThrow(()-> new ResourceNotFoundException("Task not found"));
            taskAssignment.setTask(task);
        }

        if(assignTaskRequest.getStatus() != null) taskAssignment.setStatus(assignTaskRequest.getStatus());
        if(assignTaskRequest.getAssignedOn() != null) taskAssignment.setAssignedOn(assignTaskRequest.getAssignedOn());
        if(assignTaskRequest.getDueOn() != null) taskAssignment.setAssignedOn(assignTaskRequest.getAssignedOn());
        return taskAssignmentRepository.save(taskAssignment);

    }

    @Override
    public void deleteTask(Long assignTaskId) {
        TaskAssignment taskAssignment = taskAssignmentRepository.findById(assignTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskAssignment not found"));
         taskAssignmentRepository.delete(taskAssignment);
    }
}
