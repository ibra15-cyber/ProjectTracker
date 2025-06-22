//package com.ibra.projecttracker.service.impl;
//
//import com.ibra.projecttracker.dto.DeveloperResponseDTO;
//import com.ibra.projecttracker.entity.Developer;
//import com.ibra.projecttracker.enums.DevSkills;
//import com.ibra.projecttracker.exception.ResourceNotFoundException;
//import com.ibra.projecttracker.mapper.EntityDTOMapper;
//import com.ibra.projecttracker.repository.DeveloperRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DeveloperServiceImplTest {
//
//    @Mock
//    private DeveloperRepository developerRepository;
//
//    @Mock
//    private EntityDTOMapper entityDTOMapper;
//
//    @Mock
//    private AuditLogService auditLogService;
//
//    @InjectMocks
//    private DeveloperServiceImpl developerService;
//
//    private Developer developer;
//    private DeveloperResponseDTO developerResponseDTO;
//
//    @BeforeEach
//    void setUp() {
//        developer = new Developer();
//        developer.setId(1L);
//        developer.setFirstName("John");
//        developer.setLastName("Doe");
//        developer.setEmail("john@example.com");
//        developer.setSkill(DevSkills.valueOf("BACKEND"));
//
//        developerResponseDTO = new DeveloperResponseDTO();
//        developerResponseDTO.setId(1L);
//        developerResponseDTO.setName("John");
//        developerResponseDTO.set
//        developerResponseDTO.setEmail("john@example.com");
//        developerResponseDTO.setSkill(DevSkills.valueOf("BACKEND"));
//    }
//
//    @Test
//    void createDeveloper_ShouldReturnDeveloperDTO() {
//        // Given
//        when(developerRepository.save(any(Developer.class))).thenReturn(developer);
//        when(entityDTOMapper.mapDeveloperToDeveloperDTO(developer)).thenReturn(developerResponseDTO);
//
//        // When
//        DeveloperResponseDTO result = developerService.createDeveloper();
//
//        // Then
//        assertNotNull(result);
//        assertEquals(developerResponseDTO.getName(), result.getName());
//        assertEquals(developerResponseDTO.getEmail(), result.getEmail());
//        verify(developerRepository).save(any(Developer.class));
//        verify(auditLogService).logDeveloperCreate(anyLong(), any(Developer.class));
//    }
//
//    @Test
//    void getAllDevelopers_ShouldReturnListOfDeveloperDTO() {
//        // Given
//        List<Developer> developers = Arrays.asList(developer);
//        when(developerRepository.findAll()).thenReturn(developers);
//        when(entityDTOMapper.mapDeveloperToDeveloperDTO(developer)).thenReturn(developerResponseDTO);
//
//        // When
//        List<DeveloperResponseDTO> result = developerService.getAllDevelopers();
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(developerResponseDTO.getName(), result.get(0).getName());
//    }
//
//    @Test
//    void getDeveloperById_ShouldReturnDeveloperDTO() {
//        // Given
//        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
//        when(entityDTOMapper.mapDeveloperToDeveloperDTO(developer)).thenReturn(developerResponseDTO);
//
//        // When
//        DeveloperResponseDTO result = developerService.getDeveloperById(1L);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(developerResponseDTO.getName(), result.getName());
//        verify(auditLogService).logRead("TASK", "1");
//    }
//
//    @Test
//    void getDeveloperById_ShouldThrowResourceNotFoundException() {
//        // Given
//        when(developerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(ResourceNotFoundException.class,
//                () -> developerService.getDeveloperById(1L));
//    }
//
//    @Test
//    void updateDeveloper_ShouldReturnUpdatedDeveloperDTO() {
//        // Given
//        DeveloperResponseDTO updateDTO = new DeveloperResponseDTO();
//        updateDTO.setName("Jane Doe");
//        updateDTO.setEmail("jane@example.com");
//
//        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
//        when(developerRepository.save(any(Developer.class))).thenReturn(developer);
//        when(entityDTOMapper.mapDeveloperToDeveloperDTO(developer)).thenReturn(developerResponseDTO);
//
//        // When
//        DeveloperResponseDTO result = developerService.updateDeveloper(1L, updateDTO);
//
//        // Then
//        assertNotNull(result);
//        verify(developerRepository).save(developer);
//        verify(auditLogService).logDeveloperUpdate(anyLong(), any(Developer.class), any(Developer.class));
//    }
//
//    @Test
//    void deleteDeveloper_ShouldDeleteDeveloper() {
//        // Given
//        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
//
//        // When
//        developerService.deleteDeveloper(1L);
//
//        // Then
//        verify(developerRepository).delete(developer);
//        verify(auditLogService).logDeveloperDelete(anyLong(), any(Developer.class));
//    }
//
//    @Test
//    void getDevelopersPageable_ShouldReturnPageOfDeveloperDTO() {
//        // Given
//        List<Developer> developers = Arrays.asList(developer);
//        Page<Developer> developerPage = new PageImpl<>(developers);
//        when(developerRepository.findAll(any(PageRequest.class))).thenReturn(developerPage);
//        when(entityDTOMapper.mapDeveloperToDeveloperDTO(developer)).thenReturn(developerResponseDTO);
//
//        // When
//        Page<DeveloperResponseDTO> result = developerService.getDevelopersPageable(0, 10, "developerId", "asc");
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//    }
//}