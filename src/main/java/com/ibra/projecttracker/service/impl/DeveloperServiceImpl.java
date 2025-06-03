package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.service.DeveloperService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final EntityDTOMapper entityDTOMapper;

    public DeveloperServiceImpl(DeveloperRepository developerRepository, EntityDTOMapper entityDTOMapper) {
        this.developerRepository = developerRepository;
        this.entityDTOMapper = entityDTOMapper;
    }

    @Override
    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
        if (developerDTO.getName() == null || developerDTO.getName().isEmpty() ) {
            return null;
        }
        Developer developer = new Developer();
        developer.setDeveloperId(developerDTO.getId());
        developer.setName(developerDTO.getName());
        developer.setEmail(developerDTO.getEmail());
        developer.setSkills(developerDTO.getSkill());
        Developer savedDeveloper = developerRepository.save(developer);

        return entityDTOMapper.mapDeveloperToDeveloperDTO(savedDeveloper);
    }

    @Override
    public List<DeveloperDTO> getAllDevelopers() {
        if (developerRepository.count() == 0) {
            return null;
        }
        List<Developer> developers = developerRepository.findAll();
        return developers.stream()
                .map(entityDTOMapper::mapDeveloperToDeveloperDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeveloperDTO getDeveloperById(Long developerId) {

        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));
        return entityDTOMapper.mapDeveloperToDeveloperDTO(developer);
    }

    @Override
    public DeveloperDTO updateDeveloper(Long developerId, DeveloperDTO developerDTO) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));
        if(developerDTO.getName() != null ) developer.setName(developerDTO.getName());
        if(developerDTO.getEmail() != null ) developer.setEmail(developerDTO.getEmail());
        if(developerDTO.getSkill() != null) developer.setSkills(developerDTO.getSkill());

        Developer savedDeveloper = developerRepository.save(developer);
        return entityDTOMapper.mapDeveloperToDeveloperDTO(savedDeveloper);
    }

    @Override
    public void deleteDeveloper(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));
        developerRepository.delete(developer);
    }
}
