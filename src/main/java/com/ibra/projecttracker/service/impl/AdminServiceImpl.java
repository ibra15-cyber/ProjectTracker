package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.request.AdminUpdateDetails;
import com.ibra.projecttracker.entity.Admin;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.exception.ResourceNotFoundException;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.AdminRepository;
import com.ibra.projecttracker.repository.DeveloperRepository;
import com.ibra.projecttracker.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {


    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AuditLogService auditLogService, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }


    @Override
    public Admin createAdmin(String firstName, String lastName, String email, String rawPassword,
                             String phoneNumber, String adminLevel) {
        Admin admin = Admin.builder()
                .email(email.toLowerCase())
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .password(passwordEncoder.encode(rawPassword))
                .userRole(UserRole.ADMIN)
                .adminLevel(adminLevel)
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        auditLogService.logDeveloperCreate(savedAdmin.getId(), savedAdmin);

        return savedAdmin;
    }

    @Override
    public Admin updateAdmin(Long adminId, AdminUpdateDetails request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found!"));
        if(request.getAdminLevel() != null) admin.setAdminLevel(request.getAdminLevel());

        return adminRepository.save(admin);
    }


}
