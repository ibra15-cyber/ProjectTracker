package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.enums.ProjectStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long projectId);
    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
    void deleteProject(Long projectId);
    Page<ProjectDTO> getProjectsPageable(int page, int size, String sortBy, String sortDirection);
    List<ProjectDTO> dynamicFilterProjects(Long projectId, String name, String description,
                                           LocalDateTime createdAt, LocalDateTime deadline,
                                           ProjectStatus status, int pageSize, int pageNumber,
                                           String sortBy);
}
