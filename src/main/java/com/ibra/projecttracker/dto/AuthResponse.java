package com.ibra.projecttracker.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class AuthResponse {
    private String message;
    private String statusCode;
    private Long timestamp;


    private UserDTO user;
    private String token;
    private UserDTO userRole;

    Map<String, String> loginResponse;

    Map<String, String> tokenRefresh;
}
