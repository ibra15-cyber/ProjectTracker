package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.ProjectListDTO;
import com.ibra.projecttracker.enums.ProjectStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProjectService {
    ProjectListDTO createProject(ProjectDTO projectDTO);
    List<ProjectListDTO> getAllProjects();
    ProjectListDTO getProjectById(Long projectId);
    ProjectListDTO updateProject(Long projectId, ProjectDTO projectDTO);
    void deleteProject(Long projectId);
    Page<ProjectListDTO> getProjectsPageable(int page, int size, String sortBy, String sortDirection);
    List<ProjectListDTO> dynamicFilterProjects(Long projectId, String name, String description,
                                           LocalDateTime createdAt, LocalDateTime deadline,
                                           ProjectStatus status, int pageSize, int pageNumber,
                                           String sortBy);
    Map<String, String> getProjectSummary(Long projectId);
}
