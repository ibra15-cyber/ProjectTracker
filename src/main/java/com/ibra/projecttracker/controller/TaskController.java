package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping("/create-task")
    public ResponseEntity<Response> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO newTask = taskService.createTask(taskDTO);
        Response response = Response.builder()
                .message("Task created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .task(newTask)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllTasks() {
        List<TaskDTO> taskDTOs = taskService.getAllTasks();
        Response response = Response.builder()
                .message("All tasks retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .tasks(taskDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        Response response = Response.builder()
                .message("Task retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .task(taskDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updateTask = taskService.updateTask(id, taskDTO);
        Response response = Response.builder()
                .message("Task updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .task(updateTask)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        Response response = Response.builder()
                .message("Task deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get-tasks-by-projectId/{projectId}")
    public ResponseEntity<Response> getTasksByProjectId(@PathVariable("projectId") Long projectId) {
        List<TaskDTO> projectDTOS = taskService.getTasksByProjectId(projectId);
        Response response = Response.builder()
                .message("Tasks retrieved successful")
                .statusCode(HttpStatus.OK.toString())
                .tasks(projectDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
