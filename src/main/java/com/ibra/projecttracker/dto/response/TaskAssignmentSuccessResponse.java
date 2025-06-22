package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.TaskAssignmentDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TaskAssignmentSuccessResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private TaskAssignmentDTO taskAssignment;
    private List<TaskAssignmentDTO> taskAssignments;

}
