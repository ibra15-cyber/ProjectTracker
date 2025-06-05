package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @PostMapping("/create-project")
    public ResponseEntity<Response> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        System.out.println(projectDTO);
        ProjectDTO newProject = projectService.createProject(projectDTO);
        Response response = Response.builder()
                .message("Project created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .project(newProject)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllProjects() {
        List<ProjectDTO> projectDTOs = projectService.getAllProjects();
        Response response = Response.builder()
                .message("All projects retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .projects(projectDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProjectById(@PathVariable("id") Long id) {
        ProjectDTO projectDTO = projectService.getProjectById(id);
        Response response = Response.builder()
                .message("Project retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .project(projectDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateProject(@PathVariable("id") Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updateProject = projectService.updateProject(id, projectDTO);
        Response response = Response.builder()
                .message("Project updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .project(updateProject)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        Response response = Response.builder()
                .message("Project deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterProject(@RequestParam(required = false) Long projectId,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  LocalDateTime createdAt,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  LocalDateTime deadline,
                                                  @RequestParam(required = false) ProjectStatus status,
                                                  @RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "100") int pageSize,
                                                  @RequestParam(defaultValue = "projectId") String sortBy) {

        List<ProjectDTO> projectDTOS = projectService.dynamicFilterProjects(projectId, name, description,
                createdAt, deadline, status, pageSize, pageNumber, sortBy);

        Response response = Response.builder()
                .message("Projects retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .projects(projectDTOS)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
