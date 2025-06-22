package com.ibra.projecttracker.service;

import com.ibra.projecttracker.dto.UserDTO;
import com.ibra.projecttracker.dto.request.AuthRequest;
import com.ibra.projecttracker.dto.request.UserRegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface AuthService {
    UserDTO createUser(UserRegistrationRequest userRegistrationRequest);
    Map<String, String> loginUser(AuthRequest authRequest);
    Map<String, String> refreshToken(HttpServletRequest refreshToken);

}
