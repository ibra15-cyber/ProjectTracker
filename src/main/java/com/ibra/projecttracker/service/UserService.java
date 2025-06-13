package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.AuthRequest;
import com.ibra.projecttracker.dto.UserCreateRequest;
import com.ibra.projecttracker.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDTO createUser(UserCreateRequest userCreateRequest);
    Map<String, String> loginUser(AuthRequest authRequest);
    Map<String, String> refreshToken(HttpServletRequest refreshToken);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long developerId);
    UserDTO updateUser(Long developerId, UserDTO userDTO);
    void deleteUser(Long developerId);

    UserDTO getLoginUser();
}
