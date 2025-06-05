package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.ProjectRepository;
import com.ibra.projecttracker.service.ProjectService;
import com.ibra.projecttracker.specification.ProjectSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(entityDTOMapper::mapProjectToProjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(key="#projectId", value = "projects")
    public ProjectDTO getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        auditLogService.logRead("PROJECT", projectId.toString());

        return entityDTOMapper.mapProjectToProjectDTO(project);
    }


    @Override
    @Transactional
    @CachePut(value = "projects", key = "#projectId")  //there exists a property unless to filter your output
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project projectToUpdate = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (projectDTO.getName() != null) projectToUpdate.setName(projectDTO.getName());
        if (projectDTO.getDescription() != null) projectToUpdate.setDescription(projectDTO.getDescription());
        if (projectDTO.getDeadline() != null) {
            projectToUpdate.setDeadline(projectDTO.getDeadline());
            auditLogService.logProjectDeadlineChange(projectId, projectToUpdate.getDeadline(), projectDTO.getDeadline());
        }
        if (projectDTO.getStatus() != null) {
            projectToUpdate.setStatus(projectDTO.getStatus());
            auditLogService.logProjectStatusChange(projectId, projectToUpdate.getStatus().toString(), projectDTO.getStatus().toString());
        }

        Project updatedProject = projectRepository.save(projectToUpdate);

        auditLogService.logProjectUpdate(projectId, projectToUpdate, updatedProject);

        return entityDTOMapper.mapProjectToProjectDTO(projectRepository.save(updatedProject));
    }

    @Override
    @Transactional
    @CacheEvict(value = "projects", key = "#projectId")
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        auditLogService.logProjectDelete(projectId, project);

        projectRepository.delete(project);

    }

    @Override
    public Page<ProjectDTO> getProjectsPageable(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        log.debug(pageable.toString());
        Page<Project> projectsPage =  projectRepository.findAll(pageable);
        projectsPage.forEach(System.out::println);

        return projectsPage.map(entityDTOMapper::mapProjectToProjectDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> dynamicFilterProjects(Long projectId, String name, String description,
                                                  LocalDateTime createdAt, LocalDateTime deadline,
                                                  ProjectStatus status, int pageSize, int pageNumber,
                                                  String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.DESC, sortBy));

        Specification<Project> spec = Specification.allOf();
        if (projectId != null) spec = spec.and(ProjectSpecification.hasProjectId(projectId));
        if (name != null) spec = spec.and(ProjectSpecification.hasProjectName(name));
        if (description != null) spec = spec.and(ProjectSpecification.hasDescription(description));
        if (status != null) spec = spec.and(ProjectSpecification.hasStatus(status));
        if (deadline != null) spec = spec.and(ProjectSpecification.hasDeadline(deadline));
        spec = spec.and(ProjectSpecification.dueOnOrAfter(deadline));
        spec = spec.and(ProjectSpecification.dueOnOrBefore(deadline));
        if (createdAt != null && deadline != null) spec.and(ProjectSpecification.createdBetween(createdAt, deadline));

        Page<Project> projectPages = projectRepository.findAll(spec, pageable);

        log.debug("Executing dynamic filter with specification: {}", spec);

//        projectPages.forEach(System.out::println);
        return projectPages.stream()
                .map(entityDTOMapper::mapProjectToProjectDTO)
                .collect(Collectors.toList());
    }



}
