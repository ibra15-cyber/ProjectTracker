package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.service.TaskAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/task-assignment")
public class TaskAssignmentController {

    private final TaskAssignmentService taskAssignmentService;
    private final EntityDTOMapper entityDTOMapper;

    public TaskAssignmentController(TaskAssignmentService taskAssignmentService, EntityDTOMapper entityDTOMapper) {
        this.taskAssignmentService = taskAssignmentService;
        this.entityDTOMapper = entityDTOMapper;
    }


    @PostMapping("/create-task-assignment")
    public ResponseEntity<Response> createTask(@Valid @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment newTask = taskAssignmentService.createTask(taskAssignmentDTO);
        Response response = Response.builder()
                .message("Task assigned successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .taskAssignment(entityDTOMapper.mapTaskAssignmentToDTO(newTask))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllTasks() {
        List<TaskAssignment> assignTaskRequests = taskAssignmentService.getAllTaskAssignments();
        List<TaskAssignmentDTO> assignmentDTOS = assignTaskRequests.stream().map(entityDTOMapper::mapTaskAssignmentToDTO).collect(Collectors.toList());
        Response response = Response.builder()
                .message("All assigned tasks retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .taskAssignments(assignmentDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") Long id) {
        TaskAssignment assignTaskRequest = taskAssignmentService.getTaskAssignmentById(id);
        Response response = Response.builder()
                .message("Assigned task retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .taskAssignment(entityDTOMapper.mapTaskAssignmentToDTO(assignTaskRequest))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable("id") Long id, @Valid  @RequestBody TaskAssignmentDTO taskAssignmentDTO) {
        TaskAssignment updateTask = taskAssignmentService.updateTask(id, taskAssignmentDTO);
        Response response = Response.builder()
                .message("Assigned task updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .taskAssignment(entityDTOMapper.mapTaskAssignmentToDTO(updateTask))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable("id") Long id) {
        taskAssignmentService.deleteTask(id);
        Response response = Response.builder()
                .message("Assigned task deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-tasks-by-developerId/{developerId}")
    public ResponseEntity<Response> getTasksByDeveloperId(@PathVariable("developerId") Long developerId) {
        List<TaskAssignmentDTO> taskAssignmentByDeveloper = taskAssignmentService.getAllTaskAssignmentByDeveloper(developerId);
        Response response = Response.builder()
                .message("assigned task retrieved successful")
                .statusCode(HttpStatus.OK.toString())
                .taskAssignments(taskAssignmentByDeveloper)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }





}
