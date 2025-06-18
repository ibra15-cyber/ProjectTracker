package com.ibra.projecttracker.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
public class UserResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private String token;
    private UserDTO user;
    private List<UserDTO> users;
}
