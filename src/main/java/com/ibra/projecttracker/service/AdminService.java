package com.ibra.projecttracker.service;



import com.ibra.projecttracker.dto.request.AdminUpdateDetails;
import com.ibra.projecttracker.entity.Admin;



public interface AdminService {
    Admin createAdmin(String firstName, String lastName, String email, String rawPassword,
                      String phoneNumber, String adminLevel);

    Admin updateAdmin(Long adminId, AdminUpdateDetails adminUpdateDetails);

}
