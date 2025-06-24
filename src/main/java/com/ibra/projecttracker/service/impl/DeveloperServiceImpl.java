package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.DeveloperDTO;
import com.ibra.projecttracker.dto.request.DeveloperRegistrationRequest;
import com.ibra.projecttracker.dto.request.DeveloperUpdateDetails;
import com.ibra.projecttracker.entity.Developer;
import com.ibra.projecttracker.enums.DevSkills;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.service.DeveloperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    public DeveloperServiceImpl(DeveloperRepository developerRepository, EntityDTOMapper entityDTOMapper, AuditLogService auditLogService, PasswordEncoder passwordEncoder) {
        this.developerRepository = developerRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
    }

    public Developer createDeveloper(String firstName, String lastName,String email, String encodedPassword,
                                     String phoneNumber, DevSkills skill){

        Developer developer = Developer.builder()
                .email(email.toLowerCase())
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .password(encodedPassword)
                .userRole(UserRole.DEVELOPER)
                .skill(skill)
                .build();

        return developerRepository.save(developer);
    }

    //can still create user from the /developer route refactor from master branch keep here for learning
    @Override
    public DeveloperDTO createDeveloper(DeveloperRegistrationRequest request) {
        Developer developer = Developer.builder()
                .email(request.getEmail().toLowerCase())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword())) // Should be encoded
                .userRole(UserRole.DEVELOPER)
                .skill(request.getSkill())
                .build();

        Developer savedDeveloper = developerRepository.save(developer);

        auditLogService.logDeveloperCreate(savedDeveloper.getId(), savedDeveloper);

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
    public void deleteDeveloper(Long developerId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));

        auditLogService.logDeveloperDelete(developer.getId(), developer);

        developerRepository.delete(developer);
    }



    @Override
    public Developer updateDeveloper(Long developerId, DeveloperUpdateDetails developerUpdateDetails) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found!"));
        if(developerUpdateDetails.getSkill() != null) developer.setSkill(developerUpdateDetails.getSkill());



        Developer savedDeveloper =  developerRepository.save(developer);

        auditLogService.logUpdate(savedDeveloper.getUserRole().name(), savedDeveloper.getId().toString(), developer, savedDeveloper);


        return savedDeveloper;
    }


    @Override
    public Page<DeveloperDTO> getDevelopersPageable(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Developer> developersPage = developerRepository.findAll(pageable);

        developersPage.forEach(System.out::println);

        return developersPage.map(entityDTOMapper::mapDeveloperToDeveloperDTO);
    }

    @Override
    public List<DeveloperDTO> findTop5DevelopersWithMostTasksAssigned() {
        List<Developer> developers =  developerRepository.findTop5DevelopersWithMostTasksAssigned();
        return developers.stream()
                .map(entityDTOMapper::mapDeveloperToDeveloperDTO)
                .collect(Collectors.toList());
    }
}
