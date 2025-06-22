package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.DeveloperResponseDTO;
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

    private DeveloperResponseDTO developer;
    private List<DeveloperResponseDTO> developers;

    private Page<DeveloperResponseDTO> developerPage;
}
