package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.dto.TaskAssignmentListDTO;
import com.ibra.projecttracker.dto.response.TaskAssignmentSuccessResponse;
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
    private TaskAssignmentSuccessResponse buildTaskAssignmentResponse(
            String message,
            HttpStatus status,
            TaskAssignmentDTO taskAssignment,
            List<TaskAssignmentDTO> taskAssignments,
            List<TaskAssignmentListDTO> taskAssignmentByDeveloper) {
        return TaskAssignmentSuccessResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .taskAssignment(taskAssignment)
                .taskAssignments(taskAssignments)
                .taskAssignmentList(taskAssignmentByDeveloper)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentSuccessResponse> createTask(@Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment newTask = taskAssignmentService.createTask(taskAssignmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildTaskAssignmentResponse(
                        "Task assigned successfully",
                        HttpStatus.CREATED,
                        entityDTOMapper.mapTaskAssignmentToDTO(newTask),
                        null,
                null));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentSuccessResponse> getAllTasks() {
        List<TaskAssignmentDTO> assignmentDTOS = taskAssignmentService.getAllTaskAssignments()
                .stream()
                .map(entityDTOMapper::mapTaskAssignmentToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "All assigned tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        assignmentDTOS
                , null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DEVELOPER')")
    public ResponseEntity<TaskAssignmentSuccessResponse> getTaskById(@PathVariable("id") Long id) {
        TaskAssignment assignTaskRequest = taskAssignmentService.getTaskAssignmentById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "Assigned task retrieved successfully",
                        HttpStatus.OK,
                        entityDTOMapper.mapTaskAssignmentToDTO(assignTaskRequest),
                        null, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentSuccessResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment updateTask = taskAssignmentService.updateTask(id, taskAssignmentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(buildTaskAssignmentResponse(
                "Assigned task updated successfully",
                HttpStatus.OK,
                entityDTOMapper.mapTaskAssignmentToDTO(updateTask),
                null, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskAssignmentSuccessResponse> deleteTask(@PathVariable("id") Long id) {
        taskAssignmentService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "Assigned task deleted successfully",
                        HttpStatus.OK,
                        null,
                        null, null));
    }

    @GetMapping("/by-developer/{developerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER') or (hasAuthority('DEVELOPER') and @SecurityUtils.isDeveloperOwner(#developerId))")
    public ResponseEntity<TaskAssignmentSuccessResponse> getTasksByDeveloperId(@PathVariable("developerId") Long developerId) {
        List<TaskAssignmentListDTO> taskAssignmentByDeveloper = taskAssignmentService.getAllTaskAssignmentByDeveloper(developerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskAssignmentResponse(
                        "Assigned tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        taskAssignmentByDeveloper));
    }
}