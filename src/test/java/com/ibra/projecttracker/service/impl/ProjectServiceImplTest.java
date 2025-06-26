//package com.ibra.projecttracker.service.impl;
//
//import com.ibra.projecttracker.dto.ProjectDTO;
//import com.ibra.projecttracker.entity.Project;
//import com.ibra.projecttracker.enums.ProjectStatus;
//import com.ibra.projecttracker.exception.ResourceNotFoundException;
//import com.ibra.projecttracker.mapper.EntityDTOMapper;
//import com.ibra.projecttracker.repository.ProjectRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProjectServiceImplTest {
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @Mock
//    private EntityDTOMapper entityDTOMapper;
//
//    @Mock
//    private AuditLogService auditLogService;
//
//    @InjectMocks
//    private ProjectServiceImpl projectService;
//
//    private Project project;
//    private ProjectDTO projectDTO;
//
//    @BeforeEach
//    void setUp() {
//        project = new Project();
//        project.setProjectId(1L);
//        project.setName("Test Project");
//        project.setDescription("Test Description");
//        project.setStatus(ProjectStatus.PLANNING);
//
//        projectDTO = new ProjectDTO();
//        projectDTO.setId(1L);
//        projectDTO.setName("Test Project");
//        projectDTO.setDescription("Test Description");
//        projectDTO.setStatus(ProjectStatus.PLANNING);
//    }
//
//    @Test
//    void createProject_ShouldReturnProjectDTO() {
//        // Given
//        when(projectRepository.save(any(Project.class))).thenReturn(project);
//        when(entityDTOMapper.mapProjectToProjectDTO(project)).thenReturn(projectDTO);
//
//        // When
//        ProjectDTO result = projectService.createProject(projectDTO);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(projectDTO.getName(), result.getName());
//        verify(projectRepository).save(any(Project.class));
//        verify(auditLogService).logProjectCreate(anyLong(), any(Project.class));
//    }
//
//    @Test
//    void getAllProjects_ShouldReturnListOfProjectDTO() {
//        // Given
//        List<Project> projects = Arrays.asList(project);
//        when(projectRepository.findAll()).thenReturn(projects);
//        when(entityDTOMapper.mapProjectToProjectDTO(project)).thenReturn(projectDTO);
//
//        // When
//        List<ProjectDTO> result = projectService.getAllProjects();
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(projectDTO.getName(), result.get(0).getName());
//    }
//
//    @Test
//    void getProjectById_ShouldReturnProjectDTO() {
//        // Given
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//        when(entityDTOMapper.mapProjectToProjectDTO(project)).thenReturn(projectDTO);
//
//        // When
//        ProjectDTO result = projectService.getProjectById(1L);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(projectDTO.getName(), result.getName());
//        verify(auditLogService).logRead("PROJECT", "1");
//    }
//
//    @Test
//    void getProjectById_ShouldThrowResourceNotFoundException() {
//        // Given
//        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(ResourceNotFoundException.class,
//                () -> projectService.getProjectById(1L));
//    }
//
//    @Test
//    void updateProject_ShouldReturnUpdatedProjectDTO() {
//        // Given
//        ProjectDTO updateDTO = new ProjectDTO();
//        updateDTO.setName("Updated Project");
//        updateDTO.setDescription("Updated Description");
//
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//        when(projectRepository.save(any(Project.class))).thenReturn(project);
//        when(entityDTOMapper.mapProjectToProjectDTO(project)).thenReturn(projectDTO);
//
//        // When
//        ProjectDTO result = projectService.updateProject(1L, updateDTO);
//
//        // Then
//        assertNotNull(result);
//        verify(projectRepository, times(2)).save(any(Project.class));
//        verify(auditLogService).logProjectUpdate(anyLong(), any(Project.class), any(Project.class));
//    }
//
//    @Test
//    void deleteProject_ShouldDeleteProject() {
//        // Given
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//
//        // When
//        projectService.deleteProject(1L);
//
//        // Then
//        verify(projectRepository).delete(project);
//        verify(auditLogService).logProjectDelete(anyLong(), any(Project.class));
//    }
//}
