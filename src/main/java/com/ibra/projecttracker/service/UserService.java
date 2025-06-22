package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.dto.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long developerId);
    UserDTO updateUser(Long developerId, UpdateUserRequest request);
    void deleteUser(Long developerId);

    UserDTO getLoginUser();



}
