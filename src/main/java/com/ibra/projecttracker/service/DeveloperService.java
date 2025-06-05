package com.ibra.projecttracker.service;


import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.entity.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeveloperService {
    DeveloperDTO createDeveloper(DeveloperDTO developerDTO);
    List<DeveloperDTO> getAllDevelopers();
    DeveloperDTO getDeveloperById(Long developerId);
    DeveloperDTO updateDeveloper(Long developerId, DeveloperDTO developerDTO);
    void deleteDeveloper(Long developerId);
    Page<DeveloperDTO> getDevelopersPageable(int page, int size, String sortBy, String sortDirection);
    List<DeveloperDTO> findTop5DevelopersWithMostTasksAssigned();

}
