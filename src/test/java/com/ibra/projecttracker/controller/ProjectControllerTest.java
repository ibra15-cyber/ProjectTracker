package com.ibra.projecttracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibra.projecttracker.config.TestConfig; // Import your test configuration
import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.Response;
import com.ibra.projecttracker.enums.ProjectStatus;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import; // Import statement
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Import(TestConfig.class) // Import your test configuration
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired // Now inject the mock bean directly from the context
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test creating a new project via POST /api/projects")
    void testCreateProject() throws Exception {
        // ... (rest of the test method remains the same)
        ProjectDTO newProjectDTO = new ProjectDTO();
        newProjectDTO.setName("Controller Test Project");
        newProjectDTO.setDescription("Description for controller test");
        newProjectDTO.setDeadline(LocalDateTime.now().plusDays(30));
        newProjectDTO.setStatus(ProjectStatus.PLANNING);

        ProjectDTO createdProjectDTO = new ProjectDTO();
        createdProjectDTO.setId(10L);
        createdProjectDTO.setName("Controller Test Project");
        createdProjectDTO.setDescription("Description for controller test");
        createdProjectDTO.setCreatedAt(LocalDateTime.now());
        createdProjectDTO.setDeadline(LocalDateTime.now().plusDays(30));
        createdProjectDTO.setStatus(ProjectStatus.PLANNING);

        Response expectedResponse = Response.builder()
                .message("Project created successfully")
                .statusCode("201")
                .project(createdProjectDTO)
                .build();

        when(projectService.createProject(any(ProjectDTO.class))).thenReturn(createdProjectDTO);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProjectDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project created successfully")))
                .andExpect(jsonPath("$.statusCode", is("201")))
                .andExpect(jsonPath("$.project.id", is(10)))
                .andExpect(jsonPath("$.project.name", is("Controller Test Project")));
    }

    @Test
    @DisplayName("Test getting a project by ID via GET /api/projects/{id}")
    void testGetProjectById() throws Exception {
        // ... (rest of the test method remains the same)
        Long projectId = 1L;
        ProjectDTO retrievedProjectDTO = new ProjectDTO();
        retrievedProjectDTO.setId(projectId);
        retrievedProjectDTO.setName("Existing Project");
        retrievedProjectDTO.setDescription("Description of existing project");
        retrievedProjectDTO.setCreatedAt(LocalDateTime.now().minusDays(10));
        retrievedProjectDTO.setDeadline(LocalDateTime.now().plusDays(5));
        retrievedProjectDTO.setStatus(ProjectStatus.ACTIVE);

        Response expectedResponse = Response.builder()
                .message("Project retrieved successfully")
                .statusCode("200")
                .project(retrievedProjectDTO)
                .build();

        when(projectService.getProjectById(projectId)).thenReturn(retrievedProjectDTO);

        mockMvc.perform(get("/api/projects/{id}", projectId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project retrieved successfully")))
                .andExpect(jsonPath("$.statusCode", is("200")))
                .andExpect(jsonPath("$.project.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.project.name", is("Existing Project")));
    }

    @Test
    @DisplayName("Test getting a project by ID - Not Found")
    void testGetProjectByIdNotFound() throws Exception {
        // ... (rest of the test method remains the same)
        Long nonExistentProjectId = 99L;
        when(projectService.getProjectById(nonExistentProjectId))
                .thenThrow(new ResourceNotFoundException("Project not found!"));

        mockMvc.perform(get("/api/projects/{id}", nonExistentProjectId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project not found!")))
                .andExpect(jsonPath("$.statusCode", is("404")));
    }
}