package com.ibra.projecttracker.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
public class TaskAssignmentResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private TaskAssignmentDTO taskAssignment;
    private List<TaskAssignmentDTO> taskAssignments;

}
