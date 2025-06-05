package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.service.DeveloperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final AuditLogService auditLogService;

    public DeveloperServiceImpl(DeveloperRepository developerRepository, EntityDTOMapper entityDTOMapper, AuditLogService auditLogService) {
        this.developerRepository = developerRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.auditLogService = auditLogService;
    }

    @Override
    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
        Developer developer = new Developer();
        developer.setDeveloperId(developerDTO.getId());
        developer.setName(developerDTO.getName());
        developer.setEmail(developerDTO.getEmail());
        developer.setSkills(developerDTO.getSkill());
        Developer savedDeveloper = developerRepository.save(developer);

        auditLogService.logDeveloperCreate(savedDeveloper.getDeveloperId(), savedDeveloper);

        return entityDTOMapper.mapDeveloperToDeveloperDTO(savedDeveloper);
    }

    @Override
    public List<DeveloperDTO> getAllDevelopers() {
        List<Developer> developers = developerRepository.findAll();
        return developers.stream()
                .map(entityDTOMapper::mapDeveloperToDeveloperDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeveloperDTO getDeveloperById(Long developerId) {

        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));

        auditLogService.logRead("TASK", developerId.toString());

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

        auditLogService.logDeveloperUpdate(savedDeveloper.getDeveloperId(), developer, savedDeveloper);

        return entityDTOMapper.mapDeveloperToDeveloperDTO(savedDeveloper);
    }

    @Override
    public void deleteDeveloper(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));

        auditLogService.logDeveloperDelete(developer.getDeveloperId(), developer);

        developerRepository.delete(developer);
    }

    @Override
    public Page<DeveloperDTO> getDevelopersPageable(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Developer> developersPage = developerRepository.findAll(pageable);

        developersPage.forEach(System.out::println);

        return developersPage.map(entityDTOMapper::mapDeveloperToDeveloperDTO);
    }
}
