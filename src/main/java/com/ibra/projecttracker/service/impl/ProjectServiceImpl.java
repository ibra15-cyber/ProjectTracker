package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.ProjectRepository;
import com.ibra.projecttracker.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final AuditLogService auditLogService;

    public ProjectServiceImpl(ProjectRepository projectRepository, EntityDTOMapper entityDTOMapper, AuditLogService auditLogService) {
        this.projectRepository = projectRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.auditLogService = auditLogService;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project newProject = new Project();
        newProject.setName(projectDTO.getName());
        newProject.setDescription(projectDTO.getDescription());
        newProject.setStatus(projectDTO.getStatus());
        newProject.setCreatedAt(LocalDateTime.now());
        newProject.setDeadline(projectDTO.getDeadline());

        Project createdProject = projectRepository.save(newProject);

        auditLogService.logProjectCreate(createdProject.getProjectId(), createdProject);

        return entityDTOMapper.mapProjectToProjectDTO(createdProject);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(entityDTOMapper::mapProjectToProjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        auditLogService.logRead("PROJECT", projectId.toString());

        return entityDTOMapper.mapProjectToProjectDTO(project);
    }

    @Override
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (projectDTO.getName() != null) project.setName(projectDTO.getName());
        if (projectDTO.getDescription() != null) project.setDescription(projectDTO.getDescription());
        if (projectDTO.getDeadline() != null) project.setDeadline(projectDTO.getDeadline());
        if (projectDTO.getStatus() != null) project.setStatus(projectDTO.getStatus());

        Project updatedProject = projectRepository.save(project);

        auditLogService.logProjectUpdate(projectId, project, updatedProject);

        return entityDTOMapper.mapProjectToProjectDTO(projectRepository.save(updatedProject));
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        auditLogService.logProjectDelete(projectId, project);

        projectRepository.delete(project);

    }


}
