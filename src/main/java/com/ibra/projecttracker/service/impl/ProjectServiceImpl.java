package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.TaskDTO;
import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.ProjectRepository;
import com.ibra.projecttracker.repository.TaskRepository;
import com.ibra.projecttracker.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EntityDTOMapper entityDTOMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, EntityDTOMapper entityDTOMapper) {
        this.projectRepository = projectRepository;
        this.entityDTOMapper = entityDTOMapper;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        }
        Project newProject = new Project();
        newProject.setName(projectDTO.getName());
        newProject.setDescription(projectDTO.getDescription());
        newProject.setStatus(projectDTO.getStatus());
        newProject.setDeadline(projectDTO.getDeadline());

        Project createdProject = projectRepository.save(newProject);
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
        return entityDTOMapper.mapProjectToProjectDTO(project);
    }

    @Override
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if(projectDTO.getName() != null) project.setName(projectDTO.getName());
        if(projectDTO.getDescription() != null) project.setDescription(projectDTO.getDescription());
        if(projectDTO.getDeadline() != null) project.setDeadline(projectDTO.getDeadline());
        if(projectDTO.getStatus() != null) project.setStatus(projectDTO.getStatus());

        Project updatedProject = projectRepository.save(project);
        return entityDTOMapper.mapProjectToProjectDTO(projectRepository.save(updatedProject));
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        projectRepository.delete(project);

    }
}
