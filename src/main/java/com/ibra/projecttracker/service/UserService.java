package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.AuthRequest;
import com.ibra.projecttracker.dto.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserCreateRequest userCreateRequest);
    String loginUser(AuthRequest authRequest);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long developerId);
    UserDTO updateUser(Long developerId, UserDTO userDTO);
    void deleteUser(Long developerId);
}
