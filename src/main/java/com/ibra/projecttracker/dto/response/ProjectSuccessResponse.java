package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.ProjectDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class ProjectResponse {
    private String message;
    private String statusCode;
    private Long timestamp;
    private ProjectDTO project;
    private List<ProjectDTO> projects;
    private Page<ProjectDTO> projectPage;
    private Map<String, String> projectSummary;
}
