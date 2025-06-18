package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class AuthResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private UserDTO user;

    Map<String, String> loginResponse;

    Map<String, String> tokenRefresh;
}
