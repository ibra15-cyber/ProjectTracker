package com.ibra.projecttracker.controller;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.ProjectListDTO;
import com.ibra.projecttracker.dto.response.ProjectSuccessResponse;
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

    private ProjectSuccessResponse buildProjectResponse(
            String message,
            HttpStatus status,
            ProjectListDTO project,
            List<ProjectListDTO> projects,
            Page<ProjectListDTO> projectPage,
            Map<String, String> projectSummary) {
        return ProjectSuccessResponse.builder()
                .message(message)
                .statusCode(String.valueOf(status.value()))
                .project(project)
                .projects(projects)
                .projectPage(projectPage)
                .projectSummary(projectSummary)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectSuccessResponse> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        ProjectListDTO newProject = projectService.createProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildProjectResponse(
                        "Project created successfully",
                        HttpStatus.CREATED,
                        newProject,
                        null,
                        null,
                        null));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectSuccessResponse> getAllProjects() {
        List<ProjectListDTO> projectDTOs = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "All projects retrieved successfully",
                        HttpStatus.OK,
                        null,
                        projectDTOs,
                        null,
                        null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSuccessResponse> getProjectById(@PathVariable("id") Long id) {
        ProjectListDTO projectDTO = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "Project retrieved successfully",
                        HttpStatus.OK,
                        projectDTO,
                        null,
                        null,
                        null));
    }

    @GetMapping("/{id}/summary")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'DEVELOPER', 'CONTRACTOR')")
    public ResponseEntity<ProjectSuccessResponse> getProjectSummary(@PathVariable("id") Long id) {
        Map<String, String> projectSummary = projectService.getProjectSummary(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "Project summary retrieved successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        null,
                        projectSummary));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectSuccessResponse> updateProject(@PathVariable("id") Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        ProjectListDTO updateProject = projectService.updateProject(id, projectDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "Project updated successfully",
                        HttpStatus.OK,
                        updateProject,
                        null,
                        null,
                        null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectSuccessResponse> deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "Project deleted successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        null,
                        null));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ProjectSuccessResponse> getProjectPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<ProjectListDTO> projectsPage = projectService.getProjectsPageable(page, size, sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "All projects retrieved successfully",
                        HttpStatus.OK,
                        null,
                        null,
                        projectsPage,
                        null));
    }

    @GetMapping("/filter")
    public ResponseEntity<ProjectSuccessResponse> filterProject(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(defaultValue = "projectId") String sortBy) {

        List<ProjectListDTO> projectDTOS = projectService.dynamicFilterProjects(
                projectId, name, description, createdAt, deadline, status, pageSize, pageNumber, sortBy);

        return ResponseEntity.status(HttpStatus.OK)
                .body(buildProjectResponse(
                        "Projects retrieved successfully",
                        HttpStatus.OK,
                        null,
                        projectDTOS,
                        null,
                        null));
    }
}