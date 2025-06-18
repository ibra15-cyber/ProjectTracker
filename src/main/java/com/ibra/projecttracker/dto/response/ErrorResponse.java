package com.ibra.projecttracker.dto.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
    private String errorMessage;
    private String statusCode;
    private Long timestamp;
}
