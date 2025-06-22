package com.ibra.projecttracker.service.impl;


import com.ibra.projecttracker.dto.request.ManagerUpdateDetails;
import com.ibra.projecttracker.entity.Manager;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;

import com.ibra.projecttracker.repository.ManagerRepository;
import com.ibra.projecttracker.service.ManagerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;
    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(AuditLogService auditLogService, PasswordEncoder passwordEncoder, ManagerRepository managerRepository) {
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
        this.managerRepository = managerRepository;
    }


    @Override
    public Manager createManager(String firstName, String lastName, String email, String rawPassword,
                                 String phoneNumber, String department) {
        Manager manager = Manager.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .password(passwordEncoder.encode(rawPassword)) // Should be encoded
                .userRole(UserRole.MANAGER)
                .department(department)
                .build();

        Manager savedManager = managerRepository.save(manager);

        auditLogService.logDeveloperCreate(savedManager.getId(), savedManager);

        return savedManager;
    }

    @Override
    public Manager updateManager(Long managerId, ManagerUpdateDetails request) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found!"));
        if (request.getDepartment() != null) manager.setDepartment(request.getDepartment());

        return managerRepository.save(manager);

    }


}
