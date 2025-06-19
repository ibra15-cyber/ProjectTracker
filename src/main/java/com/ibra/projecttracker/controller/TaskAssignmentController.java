package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.dto.response.TaskAssignmentResponse;
import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.service.TaskAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/task-assignments")
public class TaskAssignmentController {

    private final TaskAssignmentService taskAssignmentService;
    private final EntityDTOMapper entityDTOMapper;

    public TaskAssignmentController(TaskAssignmentService taskAssignmentService, EntityDTOMapper entityDTOMapper) {
        this.taskAssignmentService = taskAssignmentService;
        this.entityDTOMapper = entityDTOMapper;
    }

    // Helper method to build responses
    private TaskAssignmentResponse buildTaskAssignmentResponse(
            String message,
            HttpStatus status,
            TaskAssignmentDTO taskAssignment,
            List<TaskAssignmentDTO> taskAssignments) {
        return TaskAssignmentResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .taskAssignment(taskAssignment)
                .taskAssignments(taskAssignments)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentResponse> createTask(@Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment newTask = taskAssignmentService.createTask(taskAssignmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildTaskAssignmentResponse(
                        "Task assigned successfully",
                        HttpStatus.CREATED,
                        entityDTOMapper.mapTaskAssignmentToDTO(newTask),
                        null));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentResponse> getAllTasks() {
        List<TaskAssignmentDTO> assignmentDTOS = taskAssignmentService.getAllTaskAssignments()
                .stream()
                .map(entityDTOMapper::mapTaskAssignmentToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "All assigned tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        assignmentDTOS));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DEVELOPER')")
    public ResponseEntity<TaskAssignmentResponse> getTaskById(@PathVariable("id") Long id) {
        TaskAssignment assignTaskRequest = taskAssignmentService.getTaskAssignmentById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "Assigned task retrieved successfully",
                        HttpStatus.OK,
                        entityDTOMapper.mapTaskAssignmentToDTO(assignTaskRequest),
                        null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment updateTask = taskAssignmentService.updateTask(id, taskAssignmentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(buildTaskAssignmentResponse(
                "Assigned task updated successfully",
                HttpStatus.OK,
                entityDTOMapper.mapTaskAssignmentToDTO(updateTask),
                null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentResponse> deleteTask(@PathVariable("id") Long id) {
        taskAssignmentService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "Assigned task deleted successfully",
                        HttpStatus.OK,
                        null,
                        null));
    }

    @GetMapping("/by-developerId/{developerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER') or (hasAuthority('DEVELOPER') and @securityUtils.isDeveloperOwner(#developerId))")
    public ResponseEntity<TaskAssignmentResponse> getTasksByDeveloperId(@PathVariable("developerId") Long developerId) {
        List<TaskAssignmentDTO> taskAssignmentByDeveloper = taskAssignmentService.getAllTaskAssignmentByDeveloper(developerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "Assigned tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        taskAssignmentByDeveloper));
    }
}