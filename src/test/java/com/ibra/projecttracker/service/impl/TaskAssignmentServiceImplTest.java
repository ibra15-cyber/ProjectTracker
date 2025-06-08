package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.entity.Task;
import com.ibra.projecttracker.entity.TaskAssignment;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.repository.TaskAssignmentRepository;
import com.ibra.projecttracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskAssignmentServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EntityDTOMapper entityDTOMapper;

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private TaskAssignmentRepository taskAssignmentRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TaskAssignmentServiceImpl taskAssignmentService;

    private TaskAssignment taskAssignment;
    private TaskAssignmentDTO taskAssignmentDTO;
    private Task task;
    private Developer developer;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskId(1L);
        task.setTitle("Test Task");

        developer = new Developer();
        developer.setDeveloperId(1L);
        developer.setName("John Doe");

        taskAssignment = new TaskAssignment();
        taskAssignment.setTaskAssignmentId(1L);
        taskAssignment.setTask(task);
        taskAssignment.setDeveloper(developer);

        taskAssignmentDTO = new TaskAssignmentDTO();
        taskAssignmentDTO.getTaskAssignmentId();
        taskAssignmentDTO.setTaskId(1L);
        taskAssignmentDTO.setDeveloperId(1L);
    }

    @Test
    void createTask_ShouldReturnTaskAssignment() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(taskAssignmentRepository.save(any(TaskAssignment.class))).thenReturn(taskAssignment);

        // When
        TaskAssignment result = taskAssignmentService.createTask(taskAssignmentDTO);

        // Then
        assertNotNull(result);
        assertEquals(taskAssignment.getTaskAssignmentId(), result.getTaskAssignmentId());
        verify(taskAssignmentRepository).save(any(TaskAssignment.class));
    }

    @Test
    void getAllTaskAssignments_ShouldReturnListOfTaskAssignment() {
        // Given
        List<TaskAssignment> assignments = Arrays.asList(taskAssignment);
        when(taskAssignmentRepository.findAll()).thenReturn(assignments);

        // When
        List<TaskAssignment> result = taskAssignmentService.getAllTaskAssignments();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskAssignment.getTaskAssignmentId(), result.get(0).getTaskAssignmentId());
    }

    @Test
    void getTaskAssignmentById_ShouldReturnTaskAssignment() {
        // Given
        when(taskAssignmentRepository.findById(1L)).thenReturn(Optional.of(taskAssignment));

        // When
        TaskAssignment result = taskAssignmentService.getTaskAssignmentById(1L);

        // Then
        assertNotNull(result);
        assertEquals(taskAssignment.getTaskAssignmentId(), result.getTaskAssignmentId());
        verify(auditLogService).logRead("TASK_ASSIGNMENT", "1");
    }

    @Test
    void getTaskAssignmentById_ShouldThrowResourceNotFoundException() {
        // Given
        when(taskAssignmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> taskAssignmentService.getTaskAssignmentById(1L));
    }

    @Test
    void deleteTask_ShouldDeleteTaskAssignment() {
        // Given
        when(taskAssignmentRepository.findById(1L)).thenReturn(Optional.of(taskAssignment));

        // When
        taskAssignmentService.deleteTask(1L);

        // Then
        verify(taskAssignmentRepository).delete(taskAssignment);
    }

    @Test
    void getAllTaskAssignmentByDeveloper_ShouldReturnListOfTaskAssignmentDTO() {
        // Given
        List<TaskAssignment> assignments = Arrays.asList(taskAssignment);
        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(taskAssignmentRepository.findByDeveloper(developer)).thenReturn(assignments);
        when(entityDTOMapper.mapTaskAssignmentToDTO(taskAssignment)).thenReturn(taskAssignmentDTO);

        // When
        List<TaskAssignmentDTO> result = taskAssignmentService.getAllTaskAssignmentByDeveloper(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskAssignmentDTO.getTaskAssignmentId(), result.get(0).getTaskAssignmentId());
    }
}