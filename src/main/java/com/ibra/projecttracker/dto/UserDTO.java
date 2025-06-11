package com.ibra.projecttracker.dto;


import com.ibra.projecttracker.enums.UserRole;
import lombok.Builder;

@Builder
public record UserDTO(Long id, String firstName,
                      String lastName, String email,
                      String phoneNumber,
                      Enum<UserRole> userRole) {

}
