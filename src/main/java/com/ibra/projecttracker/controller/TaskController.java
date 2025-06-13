package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.TaskResponse;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO newTask = taskService.createTask(taskDTO);
        TaskResponse response = TaskResponse.builder()
                .message("Task created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .task(newTask)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<TaskResponse> getAllTasks() {
        List<TaskDTO> taskDTOs = taskService.getAllTasks();
        TaskResponse response = TaskResponse.builder()
                .message("All tasks retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .tasks(taskDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        TaskResponse response = TaskResponse.builder()
                .message("Task retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .task(taskDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updateTask = taskService.updateTask(id, taskDTO);
        TaskResponse response = TaskResponse.builder()
                .message("Task updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .task(updateTask)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        TaskResponse response = TaskResponse.builder()
                .message("Task deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-projectId/{projectId}")
    public ResponseEntity<TaskResponse> getTasksByProjectId(@PathVariable("projectId") Long projectId) {
        List<TaskDTO> projectDTOS = taskService.getTasksByProjectId(projectId);
        TaskResponse response = TaskResponse.builder()
                .message("Tasks retrieved successful")
                .statusCode(HttpStatus.OK.toString())
                .tasks(projectDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/sort")
    public ResponseEntity<TaskResponse> getAllTasks(
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        List<TaskDTO> tasks = taskService.getAllTasksBySort(sortBy, sortDirection);
        TaskResponse response = TaskResponse.builder()
                .message("All tasks retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .tasks(tasks)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<TaskResponse> getOverdueTasks() {
        List<TaskDTO> taskDTOS = taskService.findOverdueTasks();
        TaskResponse response = TaskResponse.builder()
                .message("Tasks retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .tasks(taskDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/countsByStatus")
    public List<Object[]> getTaskCountsByStatus() {
        return taskService.findTaskCountsGroupedByStatusAndProject();
    }


}
