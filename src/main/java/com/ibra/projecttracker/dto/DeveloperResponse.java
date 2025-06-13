package com.ibra.projecttracker.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
public class DeveloperResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private DeveloperDTO developer;
    private List<DeveloperDTO> developers;

    private Page<DeveloperDTO> developerPage;
}
