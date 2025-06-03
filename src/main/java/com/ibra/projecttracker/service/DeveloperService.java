package com.ibra.projecttracker.service;


import com.ibra.projecttracker.dto.DeveloperDTO;

import java.util.List;

public interface DeveloperService {
    DeveloperDTO createDeveloper(DeveloperDTO developerDTO);
    List<DeveloperDTO> getAllDevelopers();
    DeveloperDTO getDeveloperById(Long developerId);
    DeveloperDTO updateDeveloper(Long developerId, DeveloperDTO developerDTO);
    void deleteDeveloper(Long developerId);
}
