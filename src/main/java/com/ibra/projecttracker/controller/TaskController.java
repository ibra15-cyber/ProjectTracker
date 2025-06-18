package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.response.TaskResponse;
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

    // Helper method to build responses
    private TaskResponse buildTaskResponse(
            String message,
            HttpStatus status,
            TaskDTO task,
            List<TaskDTO> tasks) {
        return TaskResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .task(task)
                .tasks(tasks)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO newTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildTaskResponse(
                        "Task created successfully",
                        HttpStatus.CREATED,
                        newTask,
                        null));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponse> getAllTasks() {
        List<TaskDTO> taskDTOs = taskService.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "All tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        taskDTOs));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DEVELOPER')")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "Task retrieved successfully",
                        HttpStatus.OK,
                        taskDTO,
                        null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAnyAuthority('DEVELOPER') and @securityUtils.isTaskOwner(#id))")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updateTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "Task updated successfully",
                        HttpStatus.OK,
                        updateTask,
                        null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "Task deleted successfully",
                        HttpStatus.OK,
                        null,
                        null));
    }

    @GetMapping("/by-projectId/{projectId}")
    public ResponseEntity<TaskResponse> getTasksByProjectId(@PathVariable("projectId") Long projectId) {
        List<TaskDTO> projectDTOS = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "Tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        projectDTOS));
    }

    @GetMapping("/sort")
    public ResponseEntity<TaskResponse> getAllTasks(
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        List<TaskDTO> tasks = taskService.getAllTasksBySort(sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "All tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        tasks));
    }

    @GetMapping("/overdue")
    public ResponseEntity<TaskResponse> getOverdueTasks() {
        List<TaskDTO> taskDTOS = taskService.findOverdueTasks();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildTaskResponse(
                        "Overdue tasks retrieved successfully",
                        HttpStatus.OK,
                        null,
                        taskDTOS));
    }

    @GetMapping("/countsByStatus")
    public List<Object[]> getTaskCountsByStatus() {
        return taskService.findTaskCountsGroupedByStatusAndProject();
    }
}