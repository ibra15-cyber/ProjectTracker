package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.response.ProjectResponse;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;

    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO newProject = projectService.createProject(projectDTO);
        ProjectResponse response = ProjectResponse.builder()
                .message("Project created successfully")
                .statusCode(String.valueOf(HttpStatus.CREATED))
                .project(newProject)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponse> getAllProjects() {
        List<ProjectDTO> projectDTOs = projectService.getAllProjects();
        ProjectResponse response = ProjectResponse.builder()
                .message("All projects retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .projects(projectDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable("id") Long id) {
        ProjectDTO projectDTO = projectService.getProjectById(id);
        ProjectResponse response = ProjectResponse.builder()
                .message("Project retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .project(projectDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/{id}/summary")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DEVELOPER', 'CONTRACTOR')")
    public ResponseEntity<ProjectResponse> getProjectSummary(@PathVariable("id") Long id) {
        Map<String, String> projectSummary = projectService.getProjectSummary(id);
        ProjectResponse response = ProjectResponse.builder()
                .message("Project summary retrieved successfully")
                .statusCode(HttpStatus.OK.toString())
                .projectSummary(projectSummary)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable("id") Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updateProject = projectService.updateProject(id, projectDTO);
        ProjectResponse response = ProjectResponse.builder()
                .message("Project updated successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .project(updateProject)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponse> deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        ProjectResponse response = ProjectResponse.builder()
                .message("Project deleted successfully")
                .statusCode(HttpStatus.NO_CONTENT.toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<ProjectResponse> getProjectPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<ProjectDTO> projectsPage = projectService.getProjectsPageable(page, size, sortBy, sortDirection);
        ProjectResponse response = ProjectResponse.builder()
                .message("All projects retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .projectPage(projectsPage)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<ProjectResponse> filterProject(@RequestParam(required = false) Long projectId,
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

        ProjectResponse response = ProjectResponse.builder()
                .message("Projects retrieved successfully")
                .statusCode(String.valueOf(HttpStatus.OK))
                .projects(projectDTOS)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    



}
