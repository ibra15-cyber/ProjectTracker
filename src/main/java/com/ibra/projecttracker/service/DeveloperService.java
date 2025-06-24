package com.ibra.projecttracker.service;


import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.dto.request.DeveloperRegistrationRequest;
import com.ibra.projecttracker.dto.request.DeveloperUpdateDetails;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.enums.DevSkills;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeveloperService {
    Developer createDeveloper(String firstName, String lastName, String email, String rawPassword,
                              String phoneNumber,
                              DevSkills devSkills);

    Developer updateDeveloper(Long developerId, DeveloperUpdateDetails developerUpdateDetails);
    Page<DeveloperDTO> getDevelopersPageable(int page, int size, String sortBy, String sortDirection);
    List<DeveloperDTO> findTop5DevelopersWithMostTasksAssigned();
    List<DeveloperDTO> getAllDevelopers();

    DeveloperDTO getDeveloperById(Long developerId);
    void deleteDeveloper(Long developerId);
}
