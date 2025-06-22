package com.ibra.projecttracker.service;


import com.ibra.projecttracker.dto.ContractorDTO;

public interface ContractorService {
    ContractorDTO createContractor(String firstName, String lastName, String email, String rawPassword,
                           String phoneNumber, String companyName, String companyAddress);
//    ContractorDTO updateContractor(Long adminId, ContractorUpdateDetails contractorUpdateDetails);

}
