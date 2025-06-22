package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.request.ManagerUpdateDetails;
import com.ibra.projecttracker.entity.Manager;

public interface ManagerService {
    Manager createManager(String firstName, String lastName, String email, String rawPassword,
                          String phoneNumber, String department);
    Manager updateManager(Long adminId, ManagerUpdateDetails managerUpdateDetails);

}
