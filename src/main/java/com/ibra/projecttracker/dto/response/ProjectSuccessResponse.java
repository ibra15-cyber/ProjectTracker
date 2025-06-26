package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.ProjectDTO;
import com.ibra.projecttracker.dto.ProjectListDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class ProjectSuccessResponse {
    private String message;
    private String statusCode;
    private Long timestamp;
    private ProjectListDTO project;
    private List<ProjectListDTO> projects;
    private Page<ProjectListDTO> projectPage;
    private Map<String, String> projectSummary;
}
