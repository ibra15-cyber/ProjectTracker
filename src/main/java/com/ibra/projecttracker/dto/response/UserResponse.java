package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

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
