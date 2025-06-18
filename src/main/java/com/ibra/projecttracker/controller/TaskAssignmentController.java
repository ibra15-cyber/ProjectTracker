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


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<TaskAssignmentResponse> createTask(@Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment newTask = taskAssignmentService.createTask(taskAssignmentDTO);
        TaskAssignmentResponse response = TaskAssignmentResponse.builder()
                .message("Task assigned successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .taskAssignment(entityDTOMapper.mapTaskAssignmentToDTO(newTask))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<TaskAssignmentResponse> getAllTasks() {
        List<TaskAssignment> assignTaskRequests = taskAssignmentService.getAllTaskAssignments();
        List<TaskAssignmentDTO> assignmentDTOS = assignTaskRequests.stream().map(entityDTOMapper::mapTaskAssignmentToDTO).collect(Collectors.toList());
        TaskAssignmentResponse response = TaskAssignmentResponse.builder()
                .message("All assigned tasks retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .taskAssignments(assignmentDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskAssignmentResponse> getTaskById(@PathVariable("id") Long id) {
        TaskAssignment assignTaskRequest = taskAssignmentService.getTaskAssignmentById(id);
        TaskAssignmentResponse response = TaskAssignmentResponse.builder()
                .message("Assigned task retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .taskAssignment(entityDTOMapper.mapTaskAssignmentToDTO(assignTaskRequest))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskAssignmentResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment updateTask = taskAssignmentService.updateTask(id, taskAssignmentDTO);
        TaskAssignmentResponse response = TaskAssignmentResponse.builder()
                .message("Assigned task updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .taskAssignment(entityDTOMapper.mapTaskAssignmentToDTO(updateTask))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskAssignmentResponse> deleteTask(@PathVariable("id") Long id) {
        taskAssignmentService.deleteTask(id);
        TaskAssignmentResponse response = TaskAssignmentResponse.builder()
                .message("Assigned task deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-developerId/{developerId}")
    public ResponseEntity<TaskAssignmentResponse> getTasksByDeveloperId(@PathVariable("developerId") Long developerId) {
        List<TaskAssignmentDTO> taskAssignmentByDeveloper = taskAssignmentService.getAllTaskAssignmentByDeveloper(developerId);
        TaskAssignmentResponse response = TaskAssignmentResponse.builder()
                .message("assigned task retrieved successful")
                .statusCode(HttpStatus.OK.toString())
                .taskAssignments(taskAssignmentByDeveloper)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
