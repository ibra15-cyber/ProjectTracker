package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.entity.Project;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.ProjectRepository;
import com.ibra.projecttracker.service.impl.AuditLogService;
import com.ibra.projecttracker.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EntityDTOMapper entityDTOMapper;

    @Mock
    private AuditLogService auditLogService; // Mock the audit log service

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project sampleProject;
    private ProjectDTO sampleProjectDTO;

    @BeforeEach
    void setUp() {
        sampleProject = new Project();
        sampleProject.setProjectId(1L);
        sampleProject.setName("Test Project");
        sampleProject.setDescription("Description for test project");
        sampleProject.setCreatedAt(LocalDateTime.now().minusDays(5));
        sampleProject.setDeadline(LocalDateTime.now().plusDays(10));
        sampleProject.setStatus(ProjectStatus.ACTIVE);

        sampleProjectDTO = new ProjectDTO();
        sampleProjectDTO.setId(1L);
        sampleProjectDTO.setName("Test Project");
        sampleProjectDTO.setDescription("Description for test project");
        sampleProjectDTO.setCreatedAt(LocalDateTime.now().minusDays(5));
        sampleProjectDTO.setDeadline(LocalDateTime.now().plusDays(10));
        sampleProjectDTO.setStatus(ProjectStatus.ACTIVE);
    }

    @Test
    @DisplayName("Test creating a new project")
    void testCreateProject() {
        // Given
        ProjectDTO inputDTO = new ProjectDTO();
        inputDTO.setName("New Project");
        inputDTO.setDescription("New project description");
        inputDTO.setDeadline(LocalDateTime.now().plusDays(20));
        inputDTO.setStatus(ProjectStatus.PLANNING);

        Project projectToSave = new Project();
        projectToSave.setName("New Project");
        projectToSave.setDescription("New project description");
        projectToSave.setDeadline(LocalDateTime.now().plusDays(20));
        projectToSave.setStatus(ProjectStatus.PLANNING);
        projectToSave.setCreatedAt(LocalDateTime.now()); // We expect createdAt to be set

        Project savedProject = new Project();
        savedProject.setProjectId(2L);
        savedProject.setName("New Project");
        savedProject.setDescription("New project description");
        savedProject.setCreatedAt(LocalDateTime.now());
        savedProject.setDeadline(LocalDateTime.now().plusDays(20));
        savedProject.setStatus(ProjectStatus.PLANNING);

        ProjectDTO expectedDTO = new ProjectDTO();
        expectedDTO.setId(2L);
        expectedDTO.setName("New Project");
        expectedDTO.setDescription("New project description");
        expectedDTO.setCreatedAt(LocalDateTime.now());
        expectedDTO.setDeadline(LocalDateTime.now().plusDays(20));
        expectedDTO.setStatus(ProjectStatus.PLANNING);

        when(entityDTOMapper.mapProjectToProjectDTO(any(Project.class))).thenReturn(expectedDTO);
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        // When
        ProjectDTO createdProject = projectService.createProject(inputDTO);

        // Then
        assertNotNull(createdProject);
        assertEquals(expectedDTO.getId(), createdProject.getId());
        assertEquals(expectedDTO.getName(), createdProject.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
        // Verify that audit log was called
        verify(auditLogService, times(1)).logCreate(eq("PROJECT"), eq(savedProject.getProjectId().toString()), any(Project.class));
    }

    @Test
    @DisplayName("Test getting a project by ID - Success")
    void testGetProjectByIdSuccess() {
        // Given
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(sampleProject));
        when(entityDTOMapper.mapProjectToProjectDTO(sampleProject)).thenReturn(sampleProjectDTO);

        // When
        ProjectDTO result = projectService.getProjectById(projectId);

        // Then
        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals(sampleProject.getName(), result.getName());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    @DisplayName("Test getting a project by ID - Not Found")
    void testGetProjectByIdNotFound() {
        // Given
        Long projectId = 99L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(projectId));
        verify(projectRepository, times(1)).findById(projectId);
    }

    // You can add more tests for updateProject, deleteProject, and getProjectByDynamicFilter
}
