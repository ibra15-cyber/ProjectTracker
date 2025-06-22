package com.ibra.projecttracker.service.impl;

import com.ibra.projecttracker.dto.ContractorDTO;
import com.ibra.projecttracker.entity.Contractor;
import com.ibra.projecttracker.enums.UserRole;
import com.ibra.projecttracker.mapper.EntityDTOMapper;
import com.ibra.projecttracker.repository.ContractorRepository;
import com.ibra.projecttracker.service.ContractorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ContractorServiceImpl implements ContractorService {

    private final EntityDTOMapper entityDTOMapper;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;
    private final ContractorRepository contractorRepository;

    public ContractorServiceImpl(EntityDTOMapper entityDTOMapper, AuditLogService auditLogService, PasswordEncoder passwordEncoder, ContractorRepository contractorRepository) {
        this.entityDTOMapper = entityDTOMapper;
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
        this.contractorRepository = contractorRepository;
    }


    @Override
    public ContractorDTO createContractor(String firstName, String lastName, String email, String rawPassword,
                                    String phoneNumber, String companyName, String companyAddress) {
        Contractor contractor = Contractor.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .password(passwordEncoder.encode(rawPassword)) // Should be encoded
                .userRole(UserRole.CONTRACTOR)
                .companyName(companyName)
                .companyAddress(companyAddress)
                .build();

        Contractor savedContractor = contractorRepository.save(contractor);

        //TODO : to be handled in the user service
        auditLogService.logDeveloperCreate(savedContractor.getId(), savedContractor);

        return entityDTOMapper.mapContractorToContractorDTO(savedContractor);
    }


}
