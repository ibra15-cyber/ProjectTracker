package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO);
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long projectId);
    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
    void deleteProject(Long projectId);
}
