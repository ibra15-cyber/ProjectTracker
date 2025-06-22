package com.ibra.projecttracker.dto.response;


import com.ibra.projecttracker.dto.TaskDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TaskSuccessResponse {
    private String message;
    private String statusCode;
    private Long timestamp;

    private TaskDTO task;
    private List<TaskDTO> tasks;
}
