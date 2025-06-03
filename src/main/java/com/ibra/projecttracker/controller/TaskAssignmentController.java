package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.AssignTaskRequest;
import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.repository.TaskAssignmentRepository;
import com.ibra.projecttracker.service.TaskAssignmentService;
import com.ibra.projecttracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-assignment")
public class TaskAssignmentController {

    private final TaskAssignmentService taskAssignmentService;

    public TaskAssignmentController(TaskAssignmentService taskAssignmentService) {
        this.taskAssignmentService = taskAssignmentService;
    }


    @PostMapping("/create-task-assignment")
    public ResponseEntity<Response> createTask(@RequestBody AssignTaskRequest assignTaskRequest) {
        TaskAssignment newTask = taskAssignmentService.createTask(assignTaskRequest);
        Response response = Response.builder()
                .message("Task created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .taskAssignment(newTask)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllTasks() {
        List<TaskAssignment> assignTaskRequests = taskAssignmentService.getAllTaskAssignments();
        Response response = Response.builder()
                .message("All tasks retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .taskAssignments(assignTaskRequests)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTaskById(@PathVariable("id") Long id) {
        TaskAssignment assignTaskRequest = taskAssignmentService.getTaskAssignmentById(id);
        Response response = Response.builder()
                .message("Task retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .taskAssignment(assignTaskRequest)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTask(@PathVariable("id") Long id, @RequestBody AssignTaskRequest assignTaskRequest) {
        TaskAssignment updateTask = taskAssignmentService.updateTask(id, assignTaskRequest);
        Response response = Response.builder()
                .message("Task updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .taskAssignment(updateTask)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteTask(@PathVariable("id") Long id) {
        taskAssignmentService.deleteTask(id);
        Response response = Response.builder()
                .message("Task deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }





}
